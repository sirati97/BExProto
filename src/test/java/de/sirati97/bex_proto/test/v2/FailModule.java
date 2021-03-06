package de.sirati97.bex_proto.test.v2;

import de.sirati97.bex_proto.v2.IPacketDefinition;
import de.sirati97.bex_proto.v2.PacketCollection;
import de.sirati97.bex_proto.v2.service.modular.IModuleHandshake;
import de.sirati97.bex_proto.v2.service.modular.ModularService;
import de.sirati97.bex_proto.v2.service.modular.Module;
import de.sirati97.bex_proto.v2.service.modular.internal.ICallback;

/**
 * Created by sirati97 on 13.04.2016.
 */
public class FailModule extends Module implements IModuleHandshake{
    public FailModule() {
        super((short) 0);
    }

    @Override
    protected IPacketDefinition createPacket() {
        return new PacketCollection();
    }

    @Override
    public Object createData(ModularService connection) {
        return null;
    }

    @Override
    public void onHandshake(ModularService connection, final ICallback callback) {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                    callback.error(new IllegalAccessException("THIS CANNOT HAPPEN"));
                } catch (InterruptedException e) {
                    callback.error(e);
                }
            }
        };
        t.start();
    }

    @Override
    public void onHandshakeServerSide(ModularService connection, ICallback callback) throws Throwable {

    }

    @Override
    public boolean completeHandshake(ModularService connection) throws Throwable {
        return false;
    }

    @Override
    public boolean hasHighHandshakePriority() {
        return false;
    }
}
