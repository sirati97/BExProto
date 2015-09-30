package de.sirati97.bex_proto;

public class TypeExtractor implements StreamExtractor<TypeBase> {
	@Override
	public TypeBase extract(ExtractorDat dat) {
		boolean isDerived = (Boolean) Type.Boolean.getExtractor().extract(dat);
		if (isDerived) {
			String baseTypeName = (String) Type.String_US_ASCII.getExtractor().extract(dat);
			ArrayType arrayType = new ArrayType(Type.Byte);
			byte[] derivedIds = (byte[]) arrayType.toPremitiveArray(arrayType.getExtractor().extract(dat));
			
			TypeBase result = Type.get(baseTypeName);
			for (byte id:derivedIds) {
				result = DerivedTypeBase.Register.get(id).create(result);
			}
			return result;
			
		} else {
			String baseTypeName = (String) Type.String_US_ASCII.getExtractor().extract(dat);
			return Type.get(baseTypeName);
			
		}
	}

}