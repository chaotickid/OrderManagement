package com.infinity.common.exceptionHandling;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExceptionResponse {

    private String errorCode;

    private String error;

    private String httpStatus;

    private String href;

    private String methodType;

    private String timeStamp;

    private String traceId;

    private String spanId;
}