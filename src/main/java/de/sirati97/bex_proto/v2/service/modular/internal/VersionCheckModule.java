package de.sirati97.bex_proto.v2.service.modular.internal;

import de.sirati97.bex_proto.datahandler.Types;
import de.sirati97.bex_proto.util.IConnection;
import de.sirati97.bex_proto.v2.Packet;
import de.sirati97.bex_proto.v2.PacketDefinition;
import de.sirati97.bex_proto.v2.IPacketHandler;
import de.sirati97.bex_proto.v2.ReceivedPacket;
import de.sirati97.bex_proto.v2.service.modular.HandshakeMismatchVersionException;
import de.sirati97.bex_proto.v2.service.modular.IModuleHandshake;
import de.sirati97.bex_proto.v2.service.modular.ModularService;

/**
 * Created by sirati97 on 24.04.2016.
 */
public class VersionCheckModule extends InternalModule<VersionCheckModule.VersionCheckData> implements IPacketHandler, IModuleHandshake{
    private static class VersionCheckPacketDefinition extends PacketDefinition {
        public VersionCheckPacketDefinition(short id, IPacketHandler executor) {
            super(id, executor, Types.Integer);
        }
    }
    static class VersionCheckData {
        public boolean success = false;
        public boolean clientSide;
        public ICallback callback;
    }
    private VersionCheckPacketDefinition packetDefinition;
    private final int minVersion;
    private final int currentVersion;
    private final String protocolName;


    public VersionCheckModule(int minVersion, int currentVersion, String protocolName) {
        super((short)-4);
        this.minVersion = minVersion;
        this.currentVersion = currentVersion;
        this.protocolName = protocolName;
    }

    @Override
    protected VersionCheckPacketDefinition createPacket() {
        return packetDefinition==null?(packetDefinition=new VersionCheckPacketDefinition(getId(), this)):packetDefinition;
    }

    @Override
    public VersionCheckData createData(ModularService connection) {
        return new VersionCheckData();
    }

    private void sendVersion(IConnection connection) {
        Packet p = new Packet(getPacket(), currentVersion);
        p.sendTo(connection);
    }

    @Override
    public void execute(ReceivedPacket packet) {
        int remoteVersion = packet.get(0);
        VersionCheckData data = getModuleData(packet.getSender());
        if (remoteVersion < minVersion) {
            data.callback.error(new HandshakeMismatchVersionException(protocolName, remoteVersion, minVersion));
        } else {
            data.success = true;
            if (!data.clientSide) {
                sendVersion(packet.getSender());
            }
            data.callback.callback();
        }
    }

    @Override
    public void onHandshake(ModularService connection, ICallback callback) throws Throwable {
        VersionCheckData data = getOrCreateModuleData(connection);
        data.callback = callback;
        data.clientSide = true;
        sendVersion(connection);
    }

    @Override
    public void onHandshakeServerSide(ModularService connection, ICallback callback) throws Throwable {
        VersionCheckData data = getOrCreateModuleData(connection);
        data.callback = callback;
        data.clientSide = false;
    }

    @Override
    public boolean completeHandshake(ModularService connection) throws Throwable {
        return removeModuleData(connection).success;
    }

    @Override
    public boolean hasHighHandshakePriority() {
        return true;
    }

    @Override
    public VersionCheckPacketDefinition getPacket() {
        return (VersionCheckPacketDefinition) super.getPacket();
    }
}
