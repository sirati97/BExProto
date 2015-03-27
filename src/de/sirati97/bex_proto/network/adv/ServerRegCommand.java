package de.sirati97.bex_proto.network.adv;

import de.sirati97.bex_proto.network.NetConnection;

public class ServerRegCommand extends RegCommand {
	private ConnectionManager clientManager;
	
	public ServerRegCommand(ConnectionManager clientManager) {
		this.clientManager = clientManager;
	}
	
	@Override
	public void receive(String name, Boolean generic, Integer id, Void arg4, Void arg5, Void arg6, Void arg7, Void arg8, Void arg9, Void arg10, NetConnection sender) {
		if (sender.isRegistered())return;
		synchronized (sender) {
			if (sender.isRegistered())return;
			AdvConnection connection = new AdvConnection(sender, name, generic);
			clientManager.register(connection);
			sender.setRegistered(true);
			System.out.println("Registered new connection" + connection.varsToString());
			send("I", connection.isGeneric(), connection.getId(), sender);
		}
	}
}
