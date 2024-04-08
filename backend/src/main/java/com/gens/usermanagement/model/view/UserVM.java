package com.gens.usermanagement.model.view;

import com.gens.usermanagement.model.document.PrimaryDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVM {

    private String id;

    private String email;

    private String password;

    private String givenName;

    private String familyName;

    private String role;

    private PrimaryDetails primaryDetails;
}