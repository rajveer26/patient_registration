package com.soul.patient.scalar;

import com.netflix.graphql.dgs.DgsScalar;
import graphql.GraphQLContext;
import graphql.execution.CoercedVariables;
import graphql.language.Value;
import graphql.schema.Coercing;
import graphql.schema.CoercingSerializeException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingParseLiteralException;
import graphql.language.StringValue;

import java.util.Base64;
import java.util.Locale;

@DgsScalar(name="Binary")
public class BinaryScalar implements Coercing<byte[], String> {
	
	@Override
	public String serialize(@org.jetbrains.annotations.NotNull Object dataFetcherResult, @org.jetbrains.annotations.NotNull GraphQLContext graphQLContext, @org.jetbrains.annotations.NotNull Locale locale) throws CoercingSerializeException{
		
		if (dataFetcherResult instanceof byte[]) {
			return Base64.getEncoder().encodeToString((byte[]) dataFetcherResult);
		}
		throw new CoercingSerializeException("Expected byte[] but got " + dataFetcherResult.getClass().getName());
	}
	
	@Override
	public byte[] parseValue(@org.jetbrains.annotations.NotNull Object input, @org.jetbrains.annotations.NotNull GraphQLContext graphQLContext, @org.jetbrains.annotations.NotNull Locale locale) throws CoercingParseValueException{
		
		if (input instanceof String) {
			return Base64.getDecoder().decode((String) input);
		}
		throw new CoercingParseValueException("Expected String but got " + input.getClass().getName());
	}
	
	@Override
	public byte[] parseLiteral(@org.jetbrains.annotations.NotNull Value input, @org.jetbrains.annotations.NotNull CoercedVariables variables, @org.jetbrains.annotations.NotNull GraphQLContext graphQLContext, @org.jetbrains.annotations.NotNull Locale locale) throws CoercingParseLiteralException{
		if (input instanceof StringValue) {
			return Base64.getDecoder().decode(((StringValue) input).getValue());
		}
		throw new CoercingParseLiteralException("Expected AST type 'StringValue' but got " + input.getClass().getName());
	}
}

