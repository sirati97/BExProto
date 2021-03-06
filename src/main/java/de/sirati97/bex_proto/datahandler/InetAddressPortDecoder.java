package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;

import java.net.InetAddress;

public class InetAddressPortDecoder extends DecoderBase<InetAddressPort> {

	@Override
	public InetAddressPort decode(CursorByteBuffer dat, boolean header) {
		InetAddress address = Types.InetAddress.getDecoder().decode(dat);
		int port = Types.Integer.getDecoder().decode(dat);
		
		return new InetAddressPort(address, port);
	}

}
