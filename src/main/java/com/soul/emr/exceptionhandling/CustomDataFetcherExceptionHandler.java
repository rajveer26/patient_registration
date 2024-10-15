package com.soul.emr.exceptionhandling;

import com.netflix.graphql.dgs.exceptions.DgsBadRequestException;
import com.netflix.graphql.dgs.exceptions.DgsEntityNotFoundException;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.execution.DataFetcherExceptionHandler;
import graphql.execution.DataFetcherExceptionHandlerParameters;
import graphql.execution.DataFetcherExceptionHandlerResult;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

@Component
public class CustomDataFetcherExceptionHandler implements DataFetcherExceptionHandler{
	
	@Override
	public CompletableFuture <DataFetcherExceptionHandlerResult> handleException(DataFetcherExceptionHandlerParameters handlerParameters){
		Throwable exception = handlerParameters.getException();
		DataFetchingEnvironment environment = handlerParameters.getDataFetchingEnvironment();
		
		GraphQLError graphqlError = createGraphQLError(exception, environment);
		
		DataFetcherExceptionHandlerResult result = DataFetcherExceptionHandlerResult.newResult().error(graphqlError).build();
		
		return CompletableFuture.completedFuture(result);
	}
	
	private GraphQLError createGraphQLError(Throwable exception, DataFetchingEnvironment environment){
		HashMap <String, Object> extensions = new HashMap <>();
		String message;
		
		switch (exception) {
			case DgsBadRequestException dgsBadRequestException -> {
				extensions.put("code", 400);
				message = dgsBadRequestException.getMessage();
			}
			
			case DgsEntityNotFoundException dgsEntityNotFoundException -> {
				extensions.put("code", 422);
				message = dgsEntityNotFoundException.getMessage();
			}
			case AccessDeniedException unauthorized -> {

				extensions.put("code", 401);
				message = unauthorized.getMessage();
			}

			default -> {
				extensions.put("code", 500);
				message = exception.getMessage();
			}
		}
		return GraphqlErrorBuilder.newError()
				.message(message)
				.path(environment.getExecutionStepInfo().getPath())
				.location(environment.getField().getSourceLocation())
				.extensions(extensions)
				.build();
	}
}
