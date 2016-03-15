package de.sirati97.bex_proto.v1.command;

import de.sirati97.bex_proto.util.ByteBuffer;
import de.sirati97.bex_proto.datahandler.Stream;
import de.sirati97.bex_proto.v1.network.NetConnection;

public class CommandSBase implements CommandBase {
	private CommandBase command;
	private short id;
	private CommandBase parent;
	

	public CommandSBase(CommandBase command) {
		this(command, (short) 0);
	}
	
	public CommandSBase(CommandBase command, short id) {
		this.command = command;
		this.id = id;
		command.setParent(this);
	}

	@Override
	public Void extract(ByteBuffer dat) {
		return command.extract(dat);
	}

	@Override
	public short getId() {
		return id;
	}

	@Override
	public void setId(short id) {
		this.id = id;
	}
	
	public CommandBase getCommand() {
		return command;
	}
	
	protected void onSend(NetConnection... connections){}
	
	@Override
	public void send(Stream stream, NetConnection... connections) {
		onSend();
		getParent().send(stream, connections);
	}

	@Override
	public Stream generateSendableStream(Stream stream, ConnectionInfo receiver) {
		return getParent().generateSendableStream(stream, receiver);
	}

	@Override
	public void setParent(CommandBase parent) {
		this.parent = parent;
	}

	@Override
	public CommandBase getParent() {
		return parent;
	}

}