package com.book.book.api;

import com.book.book.domain.Role;
import com.book.book.domain.User;
import com.book.book.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/role")
public class RoleController {

    private final RoleService roleService;

    @GetMapping("/all")
    public List<Role> getAllRoles(){
        return roleService.getAllRoles();
    }
    @PostMapping("/save")
    public Role saveRole(@RequestBody Role role) {
        return (roleService.saveRole(role));
    }

    @DeleteMapping("/{id}")
    public String deleteRole(@PathVariable Long id) {
        return roleService.deleteRole(id);
    }

    @PostMapping("/addToUser")
    public User addRoleToUser(@RequestParam("username") String username, @RequestParam("rolename") String rolename) {
        return roleService.addRoleToUser(username, rolename);
    }
    @PostMapping("/removeFromUser")
    public User takeRoleFromUser(@RequestParam("username") String username, @RequestParam("rolename") String rolename) {
        return roleService.removeFromUser(username, rolename);
    }
}
