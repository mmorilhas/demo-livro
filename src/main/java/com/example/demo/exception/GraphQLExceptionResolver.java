package com.example.demo.exception;

import org.springframework.graphql.ExecutionGraphQlRequest;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;

@Component
public class GraphQLExceptionResolver extends DataFetcherExceptionResolverAdapter {

	/*
	 * @Override protected GraphQLError resolveToSingleError(Throwable ex,
	 * ExecutionGraphQlRequest request) { return GraphqlErrorBuilder.newError()
	 * .message(ex.getMessage()) .path(request.getExecutionInput().getExecutionId())
	 * .build(); }
	 */
}
