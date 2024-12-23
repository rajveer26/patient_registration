package com.soul.patient.scalar;

import com.netflix.graphql.dgs.DgsScalar;
import graphql.GraphQLContext;
import graphql.execution.CoercedVariables;
import graphql.language.StringValue;
import graphql.language.Value;
import graphql.schema.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

@DgsScalar(name="LocalDate")
public class LocalDateScalar implements Coercing <LocalDate, String>{
	
	@Override
	public String serialize(@org.jetbrains.annotations.NotNull Object dataFetcherResult, @org.jetbrains.annotations.NotNull GraphQLContext graphQLContext, @org.jetbrains.annotations.NotNull Locale locale) throws CoercingSerializeException{
		if (dataFetcherResult instanceof LocalDate) {
			return ((LocalDate) dataFetcherResult).format(DateTimeFormatter.ISO_LOCAL_DATE);
		} else {
			throw new CoercingSerializeException("Expected a LocalDate object.");
		}
	}
	
	@Override
	public LocalDate parseValue(@org.jetbrains.annotations.NotNull Object input, @org.jetbrains.annotations.NotNull GraphQLContext graphQLContext, @org.jetbrains.annotations.NotNull Locale locale) throws CoercingParseValueException{
		try{
			if (input instanceof String) {
				return LocalDate.parse((String) input, DateTimeFormatter.ISO_LOCAL_DATE);
			} else {
				throw new CoercingParseValueException("Expected a string representation of a LocalDate.");
			}
		}
		catch(DateTimeParseException e){
			throw new CoercingParseValueException("Invalid LocalDate value: " + input, e);
		}
	}
	
	@Override
	public LocalDate parseLiteral(@org.jetbrains.annotations.NotNull Value input, @org.jetbrains.annotations.NotNull CoercedVariables variables, @org.jetbrains.annotations.NotNull GraphQLContext graphQLContext, @org.jetbrains.annotations.NotNull Locale locale) throws CoercingParseLiteralException{
		if (input instanceof StringValue) {
			try{
				return LocalDate.parse(((StringValue) input).getValue(), DateTimeFormatter.ISO_LOCAL_DATE);
			}
			catch(DateTimeParseException e){
				throw new CoercingParseLiteralException("Invalid LocalDate value: " + input, e);
			}
		}
		throw new CoercingParseLiteralException("Expected a StringValue literal.");
	}
	
}
