package de.sirati97.bex_proto.network.adv;

import de.sirati97.bex_proto.Type;
import de.sirati97.bex_proto.command.BEx2Command;
import de.sirati97.bex_proto.network.NetConnection;

public class PingCommand extends BEx2Command<Long, Boolean> {

	public PingCommand(int id) {
		super((short)id, Type.Integer, Type.Boolean);
	}

	
	@Override
	public void receive(Long timestamp, Boolean pong, NetConnection sender) {
		if (pong) {
			sender.receivedPong((int)(System.nanoTime()-timestamp)/1000);
		} else {
			send(timestamp, true, sender);
		}
	}
	
	public void ping(NetConnection sender) {
		send(System.nanoTime(), false, sender);
	}
}
