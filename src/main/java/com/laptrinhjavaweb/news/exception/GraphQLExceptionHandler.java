package com.laptrinhjavaweb.news.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;

@Component
public class GraphQLExceptionHandler extends DataFetcherExceptionResolverAdapter {

    private static final Logger log = LoggerFactory.getLogger(GraphQLExceptionHandler.class);

    @Override
    protected GraphQLError resolveToSingleError(Throwable ex, DataFetchingEnvironment env) {

        // ✅ Nếu là AppException — lỗi có mã code riêng
        if (ex instanceof AppException appEx) {
            ErrorCode errorCode = appEx.getErrorCode();

            // 🧾 Ghi log cảnh báo (warning)
            log.warn("GraphQL AppException: {} - {}", errorCode, errorCode.getMessage(), ex);

            return GraphqlErrorBuilder.newError(env)
                    .message(errorCode.getMessage())
                    .errorType(graphql.ErrorType.DataFetchingException)
                    .build();
        }

        // ⚠️ Các lỗi không xác định — log chi tiết stacktrace
        log.error("GraphQL Internal Error: {}", ex.getMessage(), ex);

        return GraphqlErrorBuilder.newError(env)
                .message("Đã xảy ra lỗi nội bộ, vui lòng thử lại sau.")
                .errorType(graphql.ErrorType.DataFetchingException)
                .build();
    }
}
