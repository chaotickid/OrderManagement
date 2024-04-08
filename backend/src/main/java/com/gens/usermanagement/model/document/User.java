package com.gens.usermanagement.model.document;

import com.gens.common.model.MetaData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    private String email;

    private String password;

    private String role;

    private String uuid = UUID.randomUUID().toString();

    private String lastLoginAt;

    private String isAccountVerified;

    private String isEmailVerified;

    private String isAccountLocked;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private PrimaryDetails primaryDetails;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private MetaData metaData;


}