package com.kitap.kitap.service;

import com.kitap.kitap.Repo.TokenRepo;
import com.kitap.kitap.domain.Token;
import com.kitap.kitap.domain.Role;
import com.kitap.kitap.domain.User;
import com.kitap.kitap.Repo.RoleRepo;
import com.kitap.kitap.Repo.UserRepo;
import com.kitap.kitap.email.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Service @RequiredArgsConstructor @Transactional @Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final TokenRepo tokenRepo;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            log.info("User found in the database: {}", username);
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            user.getRoles().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            });
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
        }
    }

    @Override
    public User saveUser(User user) {
        log.info("Saving new user {} to the database", user.getName());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role {} to the database", role.getName());
        return roleRepo.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("Adding role {} to user {}", roleName, username);
        User user = userRepo.findByUsername(username);
        Role role = roleRepo.findByName(roleName);
        user.getRoles().add(role);
    }
    @Override
    public void takeRoleFromUser(String username, String  roleName){
        log.info("Deleting role {} from user {}",  roleRepo.findByName(roleName), username);
        User user = userRepo.findByUsername(username);
        Role role = roleRepo.findByName(roleName);
        user.getRoles().remove(role);
    }

    @Override
    public User getUser(String username) {
        log.info("Fetching user {}", username);
        return userRepo.findByUsername(username);
    }

    @Override
    public List<User> getUsers() {
        log.info("Fetching all users");
        return userRepo.findAll();
    }

    @Override
    public String signUpUser(User user) {
        saveUser(user);
        Token token = new Token(user, LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15));
        token = tokenRepo.save(token);
        String link = "http://localhost:8080/api/confirm?token=" + token.getId();
        return emailService.send(user.getEmail(), emailService.buildEmail(user.getName(), link));
    }

    @Override
    public void confirm(Long token_id) {
        Token token = tokenRepo.getById(token_id);
        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            userRepo.deleteById(token.getUser().getId());
            tokenRepo.deleteById(token_id);
        } else {
            token.getUser().enable();
        }
    }
}