package com.kitap.kitap.api;

import com.kitap.kitap.domain.Role;
import com.kitap.kitap.domain.User;
import com.kitap.kitap.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role")
public class RoleController {
    //TODO:MAYBE GET ROLES ETC; USER SHOULD NOT BE ABLE TO ADD SAME ROLE TO SOMEONE
    private final RoleService roleService;
    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/save")
    public Role saveRole(@RequestBody Role role) {
        return (roleService.saveRole(role));
    }

    @DeleteMapping("/delete/{id}")
    public String deleteRole(@PathVariable Long id) {
        return roleService.deleteRole(id);
    }

    @PostMapping("/addToUser")
    public User addRoleToUser(@RequestParam("username") String username, @RequestParam("rolename") String rolename) {
        return roleService.addRoleToUser(username, rolename);
    }
    @PostMapping("/removeFromUser")
    public User takeRoleFromUser(@RequestParam("username") String username, @RequestParam("rolename") String rolename) {
        roleService.removeFromUser(username, rolename);
        return roleService.removeFromUser(username, rolename);
    }
}
