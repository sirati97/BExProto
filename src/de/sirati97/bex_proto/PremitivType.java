package de.sirati97.bex_proto;

public abstract class PremitivType extends Type {
	@Override public boolean isPremitive() {return true;}
	public abstract Object toPremitiveArray(Object obj);
	public abstract Object toObjectArray(Object obj);
}