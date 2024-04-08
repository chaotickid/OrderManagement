package com.gens.permissionmanagement.resource;

import com.gens.permissionmanagement.model.Permissions;
import com.gens.permissionmanagement.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/***
 * ONLY ROOT USER CAN ACCESS THESE PERMISSIONS
 */

@RestController
@RequestMapping("/api/v1")
public class PermissionResource {

    @Autowired
    private PermissionService permissionService;

    @PreAuthorize("hasPermission('PERMISSION-MANAGEMENT_CREATE-PERMISSION','CREATE')")
    @PostMapping("/permission")
    public ResponseEntity<?> createPermission(@RequestBody Permissions permissions) {
        return new ResponseEntity<>(permissionService.createPermission(permissions), HttpStatus.CREATED);
    }

    @PreAuthorize("hasPermission('PERMISSION-MANAGEMENT_GET-ALL-PERMISSION','GET')")
    @GetMapping("/permission")
    public ResponseEntity<?> getAllPermissions() {
        return new ResponseEntity<>(permissionService.getAllPermissions(), HttpStatus.OK);
    }

    @PreAuthorize("hasPermission('PERMISSION-MANAGEMENT_GET-PERMISSION','GET')")
    @GetMapping("/permission/{id}")
    public ResponseEntity<?> getPermissionsById(@PathVariable(value = "id") int id) {
        return new ResponseEntity<>(permissionService.getPermissionById(id), HttpStatus.OK);
    }

    @PreAuthorize("hasPermission('PERMISSION-MANAGEMENT_GET-PERMISSION','GET')")
    @GetMapping("/permission/{permission}")
    public ResponseEntity<?> getPermissionsByPermission(@PathVariable(value = "permission") String permission) {
        return new ResponseEntity<>(permissionService.getPermissionByPermission(permission), HttpStatus.OK);
    }


    @PreAuthorize("hasPermission('PERMISSION-MANAGEMENT_DELETE-PERMISSION','DELETE')")
    @DeleteMapping("/permission/{id}")
    public ResponseEntity<?> deletePermissionsById(@PathVariable(value = "id") int id) {
        permissionService.deletePermissionById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}