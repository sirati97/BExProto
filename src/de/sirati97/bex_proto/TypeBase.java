package de.sirati97.bex_proto;


public interface TypeBase {
	boolean isArray();
	boolean isPremitive();
	Stream createStream(Object obj);
	StreamExtractor<? extends Object> getExtractor();
	Object[] createArray(int lenght);
}