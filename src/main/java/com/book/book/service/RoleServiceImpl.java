package com.book.book.service;

import com.book.book.Repo.RoleRepo;
import com.book.book.Repo.UserRepo;
import com.book.book.domain.Role;
import com.book.book.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepo roleRepo;
    private final UserRepo userRepo;

    @Override
    public List<Role> getAllRoles() {
        return roleRepo.findAll();
    }
    @Override
    public String deleteRole(Long id) {
        if (!roleRepo.existsById(id)){throw new RuntimeException("Role with "+id+" already does not exist" );}
        String deletedRole = roleRepo.getById(id).getName();
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
        if (user.getRoles().contains(role)){throw new RuntimeException("User " + user.getName()+" already has this role");}
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
        String superRole = "SUPER_ADMIN";
        if (Objects.equals(roleName, superRole)){
            throw new RuntimeException("SUPER_ADMINs can not be unauthenticated ");
        }
        boolean ex = user.getRoles().contains(role);
        if (!ex){throw new RuntimeException("User " + user.getName()+" has not this role");}
        else user.getRoles().remove(role);
        return user;
    }
}
