package com.soul.emr.scalar;

import com.netflix.graphql.dgs.DgsScalar;
import graphql.GraphQLContext;
import graphql.execution.CoercedVariables;
import graphql.language.Value;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.Locale;

@DgsScalar(name = "Upload")
public class UploadScalar implements Coercing<MultipartFile, String> {

	@Override
	public String serialize(@NotNull Object dataFetcherResult, @org.jetbrains.annotations.NotNull GraphQLContext graphQLContext, @org.jetbrains.annotations.NotNull Locale locale) throws CoercingSerializeException {
		throw new CoercingSerializeException("Upload scalar is not serializable");
	}

	@Override
	public MultipartFile parseValue(@org.jetbrains.annotations.NotNull Object input, @org.jetbrains.annotations.NotNull GraphQLContext graphQLContext, @org.jetbrains.annotations.NotNull Locale locale) throws CoercingParseValueException {
		if (input instanceof MultipartFile) {
			return (MultipartFile) input;
		}
		throw new CoercingParseValueException("Expected a MultipartFile object.");
	}

	@Override
	public MultipartFile parseLiteral(@org.jetbrains.annotations.NotNull Value input, @org.jetbrains.annotations.NotNull CoercedVariables variables, @org.jetbrains.annotations.NotNull GraphQLContext graphQLContext, @org.jetbrains.annotations.NotNull Locale locale) throws CoercingParseLiteralException {
		throw new CoercingParseLiteralException("Parsing literal of Upload scalar is not supported.");
	}
}
