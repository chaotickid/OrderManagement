package com.gens.permissionmanagement.utils;

import com.gens.common.constants.Constants;
import com.gens.common.model.UserAuthorizationVM;
import com.gens.permissionmanagement.model.Permissions;
import com.gens.permissionmanagement.repo.PermissionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Optional;

@Slf4j
@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        boolean isPermissionFound = false;
        UserAuthorizationVM userAuthorizationVM = (UserAuthorizationVM) authentication.getPrincipal();
        if (!(targetDomainObject instanceof String) && !(permission instanceof String)) {
            log.error("Invalid permission targetDomainObject: {}, permission: {}, Expected a String",
                    targetDomainObject, permission);
            return false;
        }
        String role = userAuthorizationVM.getRole();
        if (role.equalsIgnoreCase(Constants.ROOT)) {
            log.debug("Root user detected");
            return true;
        }
        Optional<Permissions> dbFetchedPermission = null;
        String finalPermission = (String) targetDomainObject + "_" + (String) permission;
        try {
            log.debug("Going to fetch permission by role: {} and permission: {}", role, permission);
            dbFetchedPermission = permissionRepository.findByRoleAndPermission(role, finalPermission);
        } catch (Exception e) {
            log.debug("Exception captured: {}", e.getMessage());
            e.printStackTrace();
            return false;
        }
        if (dbFetchedPermission.isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
}