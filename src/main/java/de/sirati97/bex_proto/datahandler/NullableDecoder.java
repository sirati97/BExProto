package de.sirati97.bex_proto.datahandler;

import de.sirati97.bex_proto.util.CursorByteBuffer;

public class NullableDecoder<T> extends DecoderBase<T> {
	private IType base;

	public NullableDecoder(IType base) {
		this.base = base;
	}

	@Override
	public T decode(CursorByteBuffer dat, boolean header) {
		boolean isNull = (Boolean) Types.Boolean.getDecoder().decode(dat);
		if (isNull) {
			return null;
		} else {
			return (T) base.getDecoder().decode(dat);
		}
	}

}
