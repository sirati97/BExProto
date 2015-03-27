package de.sirati97.bex_proto.network.adv;

import de.sirati97.bex_proto.network.NetConnection;

public class ClientRegCommand extends RegCommand {
	@Override
	public void receive(String name, Boolean generic, Integer id, Void arg4,
			Void arg5, Void arg6, Void arg7, Void arg8, Void arg9, Void arg10,
			NetConnection sender) {
		if (!sender.isRegistered())return;
		
		if ("H".equals(name)) {
			AdvClient client = (AdvClient) sender;
			send(client.getClientName(), client.isGeneric(), 0, sender);
			System.out.println("Register client");
		} else if ("I".equals(name)) {
			AdvClient client = (AdvClient) sender;
			client.setId(id);
			System.out.println("Set client id to " + id);
			sender.setRegistered(true);
		}
	}
}
