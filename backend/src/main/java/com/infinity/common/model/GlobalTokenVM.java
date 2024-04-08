package com.infinity.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlobalTokenVM {
    private String sub;
    private String email;
    private String secretKey;
    private double iat;
    private double exp;
    private String role;
    private String customerId;
}