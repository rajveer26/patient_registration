package com.soul.emr.scalar;

import com.netflix.graphql.dgs.DgsScalar;
import graphql.GraphQLContext;
import graphql.execution.CoercedVariables;
import graphql.language.StringValue;
import graphql.language.Value;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@DgsScalar(name="LocalDateTime")
public class LocalDateTimeScalar implements Coercing <LocalDateTime, String>{
	
	@Override
	public String serialize(@org.jetbrains.annotations.NotNull Object dataFetcherResult, @org.jetbrains.annotations.NotNull GraphQLContext graphQLContext, @org.jetbrains.annotations.NotNull Locale locale) throws CoercingSerializeException{
		if (dataFetcherResult instanceof LocalDateTime) {
			return ((LocalDateTime) dataFetcherResult).format(DateTimeFormatter.ISO_DATE_TIME);
		} else {
			throw new CoercingSerializeException("Not a valid DateTime");
		}
	}
	
	@Override
	public LocalDateTime parseValue(@org.jetbrains.annotations.NotNull Object input, @org.jetbrains.annotations.NotNull GraphQLContext graphQLContext, @org.jetbrains.annotations.NotNull Locale locale) throws CoercingParseValueException{
		return LocalDateTime.parse(input.toString(), DateTimeFormatter.ISO_DATE_TIME);
	}
	
	@Override
	public LocalDateTime parseLiteral(@org.jetbrains.annotations.NotNull Value input, @org.jetbrains.annotations.NotNull CoercedVariables variables, @org.jetbrains.annotations.NotNull GraphQLContext graphQLContext, @org.jetbrains.annotations.NotNull Locale locale) throws CoercingParseLiteralException{
		if (input instanceof StringValue) {
			return LocalDateTime.parse(((StringValue) input).getValue(), DateTimeFormatter.ISO_DATE_TIME);
		}
		
		throw new CoercingParseLiteralException("Value is not a valid ISO date time");
	}
	
}