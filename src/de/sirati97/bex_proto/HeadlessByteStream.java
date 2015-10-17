package de.sirati97.bex_proto;

public class HeadlessByteStream implements Stream {
	private byte[] stream;
	
	public HeadlessByteStream(byte[] stream) {
		this.stream = stream;
	}
	
	@Override
	public byte[] getBytes() {
		return stream;
	}

}