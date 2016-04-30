package de.sirati97.bex_proto.test.v2;

import de.sirati97.bex_proto.datahandler.Type;
import de.sirati97.bex_proto.threading.AdvThreadAsyncHelper;
import de.sirati97.bex_proto.util.logging.ILogger;
import de.sirati97.bex_proto.util.logging.SysOutLogger;
import de.sirati97.bex_proto.v2.Packet;
import de.sirati97.bex_proto.v2.PacketDefinition;
import de.sirati97.bex_proto.v2.PacketExecutor;
import de.sirati97.bex_proto.v2.ReceivedPacket;
import de.sirati97.bex_proto.v2.io.TestIOHandler;
import de.sirati97.bex_proto.v2.module.ModularArtifConnection;
import de.sirati97.bex_proto.v2.module.ModuleHandler;
import de.sirati97.bex_proto.v2.module.internal.BouncyCastleHelper;
import org.junit.Test;

import java.security.MessageDigest;
import java.util.Set;

import static org.junit.Assert.fail;

/**
 * Created by sirati97 on 13.04.2016.
 */
public class HandshakeTest implements PacketExecutor{
    private final Object receiveMutex = new Object();
    private boolean received = false;

    @Test
    public void start() throws Throwable {
        ILogger log = new SysOutLogger();
        Long timestamp;
        AdvThreadAsyncHelper helper = new AdvThreadAsyncHelper(6);
        try {
            try {
                log.info("Handshake test preparing");
                PacketDefinition definition = new PacketDefinition((short)0, this, Type.String_Utf_8);
                ModuleHandler moduleHandler = new ModuleHandler(definition, helper, log);
                //moduleHandler.register(new FailModule()); // - will fail every handshake
                //moduleHandler.register(new FailModule2()); // - will fail every handshake
                //moduleHandler.register(new StressModule()); //will send 2^15 packets
                TestIOHandler pipe1 = new TestIOHandler();
                TestIOHandler pipe2 = new TestIOHandler();
                ModularArtifConnection connection1 = new ModularArtifConnection("TestCon1", pipe1, moduleHandler);
                ModularArtifConnection connection2 = new ModularArtifConnection("TestCon2", pipe2, moduleHandler);
                MessageDigest md = MessageDigest.getInstance(BouncyCastleHelper.HASH_ALGORITHM);
                connection1.setHashAlgorithm(md);
                connection2.setHashAlgorithm(md);
                pipe1.receiver = pipe2;
                pipe2.receiver = pipe1;
                log.info("Handshake test start");
                connection2.expectConnection();
                timestamp = System.nanoTime();
                connection1.connect();
                timestamp = System.nanoTime()-timestamp;
                Packet p = new Packet(definition, "Testing...");
                p.sendTo(connection1);
            } catch (Throwable e) {
                e.printStackTrace();
                throw e;

            }

            synchronized (receiveMutex) {
                if (!received) {
                    receiveMutex.wait(2000);
                    if (!received) {
                        fail("Test Packet was not received after 2s");
                    }
                }
            }

            Set<Thread> threadSet = helper.getActiveThreads();
            for (int i=0;i<100;i++) {
                if (threadSet.size() > 0) {
                    Thread.sleep(1);
                } else {
                    break;
                }
            }
            if (threadSet.size() > 0) {
                log.info("Still active threads: ");
                for (Thread t:threadSet) {
                    log.info(t.getName() + " state=" + t.getState().toString());
                }
                fail("There are still thread(s) active: " + threadSet.size());
            }
            log.info("Handshake test successful. Handshake took " + (timestamp/1000000) + "ms");
        } finally {
            helper.stop();
        }

    }

    @Override
    public void execute(ReceivedPacket packet) {
        synchronized (receiveMutex) {
            received = true;
            receiveMutex.notifyAll();
        }
        System.out.println(packet.get(0));
    }
}