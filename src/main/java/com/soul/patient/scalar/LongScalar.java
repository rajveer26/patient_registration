package com.soul.patient.scalar;

import com.netflix.graphql.dgs.DgsScalar;
import graphql.GraphQLContext;
import graphql.execution.CoercedVariables;
import graphql.language.IntValue;
import graphql.language.StringValue;
import graphql.language.Value;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.Locale;

@DgsScalar(name = "Long")
public class LongScalar implements Coercing<Long, Long> {
	
	@Override
	public Long serialize(@NotNull Object dataFetcherResult, @NotNull GraphQLContext graphQLContext, @NotNull Locale locale) throws CoercingSerializeException {
		return switch (dataFetcherResult) {
			case Long l -> l;
			case Integer i -> i.longValue();
			case BigInteger bigInteger -> bigInteger.longValue();
			default -> throw new CoercingSerializeException("Expected a Long object.");
		};
	}
	
	@Override
	public Long parseValue(Object input, @NotNull GraphQLContext graphQLContext, @NotNull Locale locale) throws CoercingParseValueException {
		try {
			return Long.parseLong(input.toString());
		} catch (NumberFormatException e) {
			throw new CoercingParseValueException("Invalid Long value: " + input, e);
		}
	}
	
	@Override
	public Long parseLiteral(@NotNull Value input, @NotNull CoercedVariables variables, @NotNull GraphQLContext graphQLContext, @NotNull Locale locale) throws CoercingParseLiteralException {
		if (input instanceof IntValue) {
			return ((IntValue) input).getValue().longValue();
		} else if (input instanceof StringValue) {
			try {
				return Long.parseLong(((StringValue) input).getValue());
			} catch (NumberFormatException e) {
				throw new CoercingParseLiteralException("Invalid Long value: " + input, e);
			}
		}
		throw new CoercingParseLiteralException("Expected an IntValue or StringValue literal.");
	}
}

