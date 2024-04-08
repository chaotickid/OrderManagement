package com.infinity.common.exceptionHandling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({CustomResponseException.class})
    public final ResponseEntity<Object> handleAllExceptions(CustomResponseException ex,
                                                            WebRequest request) {
        log.error("Internal server error.", ex);
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getErrorCode(), ex.getError(), String.valueOf(ex.getHttpStatus()),
                ex.getHref(), ex.getMethodType(), ex.getTimeStamp(), ex.getTraceId(), ex.getSpanId());
        exceptionResponse.setError(ex.getError());
        return new ResponseEntity<>(exceptionResponse,
                ex.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex,
                                                            WebRequest request) {
        log.error("Internal server error.", ex);
        return new ResponseEntity<>("",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}