package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.bytebuffer.ByteBuffer;

public class NullableStream implements Stream {
	private Object data;
	private TypeBase base;
	
	public NullableStream(TypeBase base, Object data) {
		this.base = base;
		this.data = data;
	}

	@Override
	public ByteBuffer getByteBuffer() {
		if (data == null) {
			return Type.Boolean.createStream(true).getByteBuffer();
		} else {
			return new MultiStream(Type.Boolean.createStream(false), base.createStream(data)).getByteBuffer();
		}
	}

}
