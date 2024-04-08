package com.gens.permissionmanagement.repo;

import com.gens.permissionmanagement.model.Permissions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permissions, Integer> {

    Optional<Permissions> findByRoleAndPermission(String role, String permission);

    Optional<Permissions> findByPermission(String permission);

}