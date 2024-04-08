package com.gens.permissionmanagement.service;

import com.gens.common.utils.LoggingUtils;
import com.gens.permissionmanagement.model.Permissions;
import com.gens.permissionmanagement.repo.PermissionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private LoggingUtils loggingUtils;

    public Permissions createPermission(Permissions permissions) {
        log.debug("Invoking method: " + "PermissionService.createPermission");
        log.debug("incoming parameters: " + "permissions = " + loggingUtils.prettifyJson(permissions));
        return permissionRepository.save(permissions);
    }

    public List<Permissions> getAllPermissions() {
        log.debug("Invoking method: " + "PermissionService.getAllPermissions");
        return permissionRepository.findAll();
    }

    public Permissions findByRoleAndPermission(String role, String permission) {
        log.debug("Invoking method: " + "PermissionService.findByRoleAndPermission");
        log.debug("incoming parameters: " + "role = " + role + ", permission = " + permission);
        return permissionRepository.findByRoleAndPermission(role, permission).get();
    }

    public Permissions getPermissionByPermission(String permission){
        log.debug("Invoking method: " + "PermissionService.getPermissionByPermission");
        log.debug("incoming parameters: " + "permission = " + permission);
        return permissionRepository.findByPermission(permission)
                .orElseThrow(()-> new RuntimeException("Permission not found with permission name: " + permission));
    }

    public Permissions getPermissionById(int id) {
        log.debug("Invoking method: " + "PermissionService.getPermissionById");
        log.debug("incoming parameters: " + "id = " + id);
        return permissionRepository.findById(id).orElseThrow(() -> new RuntimeException("Permission not found with id: " + id));
    }

    public void deletePermissionByRoleAndPermission(String role, String permission) {
        log.debug("Invoking method: " + "PermissionService.deletePermissionByRoleAndPermission");
        log.debug("incoming parameters: " + "role = " + role + ", permission = " + permission);
        permissionRepository.delete(permissionRepository.findByRoleAndPermission(role, permission).get());
    }

    public void deletePermissionById(int id) {
        log.debug("Invoking method: " + "PermissionService.deletePermissionById");
        log.debug("incoming parameters: " + "id = " + id);
        permissionRepository.deleteById(id);
    }
}