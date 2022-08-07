package com.kitap.kitap.service;

import com.kitap.kitap.domain.Role;
import com.kitap.kitap.domain.User;

public interface RoleService {
    Role saveRole(Role role);
    User addRoleToUser(String username, String roleName);
    User removeFromUser(String username,String roleName);
    String deleteRole(Long id);
}
