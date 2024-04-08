package com.infinity.usermanagement.model.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private String pinCode;
    private String houseNo;
    private String area;
    private String landMark;
    private String city;
    private String state;
    private String country;
    private boolean isPrimary;
}
