package de.sirati97.bex_proto.network.adv;

import de.sirati97.bex_proto.StreamReader;
import de.sirati97.bex_proto.command.CommandBase;
import de.sirati97.bex_proto.command.CommandRegisterBase;
import de.sirati97.bex_proto.command.CommandSender;
import de.sirati97.bex_proto.command.CommandWrapper;
import de.sirati97.bex_proto.network.AsyncHelper;
import de.sirati97.bex_proto.network.NetConnection;
import de.sirati97.bex_proto.network.NetServer;

public class AdvServer extends NetServer implements AdvCreator{
	private CommandRegisterBase register;
	private ServerRegCommand serverRegCommand;
	private ConnectionManager connectionManager = new ConnectionManager();
	private CloseConnectionCommand closeConnectionCommand;
	
	public AdvServer(AsyncHelper asyncHelper, int port, CommandBase command) {
		super(asyncHelper, port, new StreamReader(new CommandSender(new CommandRegisterBase())));
		CommandSender sender = (CommandSender) getStreamReader().getExtractor();
		register = (CommandRegisterBase) sender.getCommand();
		register.register(new CommandWrapper(command, (short) 0));
		register.register(serverRegCommand= new ServerRegCommand(connectionManager));
		register.register(closeConnectionCommand=new CloseConnectionCommand());
	}

	public ServerRegCommand getServerRegCommand() {
		return serverRegCommand;
	}
	
	
	public CloseConnectionCommand getCloseConnectionCommand() {
		return closeConnectionCommand;
	}
	
	
	@Override
	protected void onConnected(NetConnection connection) {
		getServerRegCommand().send("H", false, 0, connection);
	}

	@Override
	public AdvConnection getServerSideConnection(String clientName, boolean generic, int id) {
		for (AdvConnection connection: connectionManager.getConnections(clientName)) {
			if (connection.getId() == id) {
				return connection;
			}
		}
		return null;
	}
	
	public ConnectionManager getConnectionManager() {
		return connectionManager;
	}
	
	@Override
	public void onSocketClosed(NetConnection connection) {
		super.onSocketClosed(connection);
		connectionManager.unregister(connection);
	}
}