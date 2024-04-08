package com.gens.usermanagement.model.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String customerId;

    private String givenName;

    private String familyName;

    @Email(message = "invalid email format detected for parameter 'email'")
    @Column(unique = true)
    private String email;

    @NotNull
    private String password;

    private String role;

    private String uuid = UUID.randomUUID().toString();

    private String lastLoginAt;

    private String isAccountVerified;

    private String isEmailVerified;

    private String isAccountLocked;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private PrimaryDetails primaryDetails;

}