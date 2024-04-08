package com.gens.permissionmanagement.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Permissions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String role;

    private String permission; //moduleName_featureName_<PermissionName>
}