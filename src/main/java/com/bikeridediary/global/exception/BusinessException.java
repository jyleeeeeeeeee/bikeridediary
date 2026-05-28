package com.bikeridediary.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Base exception for all business logic errors.
 * Use ErrorCode enum to define error cases.
 */
@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public HttpStatus getHttpStatus() {
        return errorCode.getHttpStatus();
    }
}
