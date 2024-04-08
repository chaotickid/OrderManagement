package com.infinity.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthorizationVM {
    private String email;
    private String serviceProviderId;
    private String customerId;
    private String secretKey;
    private String role;
}