package com.gens.common.exceptionHandling;

import lombok.Getter;

@Getter
public enum ErrorCodeEnum {

    //user-management [ER1000-ER2999]
    ER1000("ER1000", "User creation failed failed"),
    ER1001("ER1001", "User is already present"),
    ER1002("ER1002", "Either email id or password is blank"),
    ER1003("ER1003", "User not found"),
    ER1004("ER1004", "Invalid username or password"),
    ER1005("ER1005", "Invalid secret key, Authentication failed"),
    ER1006("ER1006", "Customer id is null blank"),

    //order-management [ER3000-ER4999]


    //external error

    //general error
    GN1000("GN1000", "General error");

    private String errorCode;

    private String error;

    ErrorCodeEnum(String errorCode, String error) {
        this.errorCode = errorCode;
        this.error = error;
    }
}
