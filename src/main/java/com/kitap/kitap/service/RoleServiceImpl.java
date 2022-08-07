package com.kitap.kitap.service;

import com.kitap.kitap.Repo.RoleRepo;
import com.kitap.kitap.Repo.UserRepo;
import com.kitap.kitap.domain.Role;
import com.kitap.kitap.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class RoleServiceImpl implements RoleService {
    private RoleRepo roleRepo;
    private UserRepo userRepo;
    @Autowired
    public RoleServiceImpl(RoleRepo roleRepo, UserRepo userRepo) {
        this.roleRepo = roleRepo;
        this.userRepo = userRepo;
    }

    @Override
    public String deleteRole(Long id) {
        if (!roleRepo.existsById(id)){throw new RuntimeException("Role with "+id+" already does not exist" );}
        String deletedRole = roleRepo.getById(id).getName().toString();
        roleRepo.deleteById(id);
        return "Role " + deletedRole + " is deleted";
    }
    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role {} to the database", role.getName());
        return roleRepo.save(role);
    }

    @Override
    public User addRoleToUser(String username, String roleName) {
        boolean userExists = userRepo.existsByUsername(username);
        boolean roleExists = roleRepo.existsByName(roleName);
        if(!userExists){throw new RuntimeException("User "+username+ " does not exist");}
        if(!roleExists){throw new RuntimeException("Role "+roleName+ " does not exist");}
        log.info("Adding role {} to user {}", roleName, username);
        User user = userRepo.findByUsername(username);
        Role role = roleRepo.findByName(roleName);
        user.getRoles().add(role);
        return user;
    }

    @Override
    public User removeFromUser(String username, String roleName) {
        boolean userExists = userRepo.existsByUsername(username);
        boolean roleExists = roleRepo.existsByName(roleName);
        if(!userExists){throw new RuntimeException("User "+username+ " does not exist");}
        if(!roleExists){throw new RuntimeException("Role "+roleName+ " does not exist");}
        log.info("Deleting role {} from user {}", roleRepo.findByName(roleName), username);
        User user = userRepo.findByUsername(username);
        Role role = roleRepo.findByName(roleName);
        user.getRoles().remove(role);
        return user;
    }
}
