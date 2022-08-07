package com.book.book.service;

import com.book.book.domain.Role;
import com.book.book.domain.User;

import java.util.List;

public interface RoleService {
    Role saveRole(Role role);
    User addRoleToUser(String username, String roleName);
    User removeFromUser(String username,String roleName);
    String deleteRole(Long id);
    List<Role> getAllRoles();
}
