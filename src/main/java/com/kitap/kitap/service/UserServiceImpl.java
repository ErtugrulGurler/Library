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
    private final TokenRepo tokenRepo;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
//UserDetails loadUserByUsername!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
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
    //UserDetails loadUserByUsername!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    @Override
    public User saveUser(User user) {
        if (user.getName()==null||user.getUsername()==null||user.getPassword()==null){throw new RuntimeException("Name, username or password can not be empty. ");}
        log.info("Saving new user {} to the database", user.getName());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }
    @Override
    public User getUserByname(String username) {
        log.info("Fetching user {}", username);
        return userRepo.findByUsername(username);
    }
    @Override
    public List<User> getUsers() {
        if (userRepo.findAll().isEmpty()){throw new RuntimeException("There is no user to show. ");}
        log.info("Fetching all users");
        return userRepo.findAll();
    }
    @Override
    public User getUser(Long id) {
        if (id==null){throw new RuntimeException("Please specify the user id to be shown. ");}
        checkById(id);
        log.info("Fetching user {}", userRepo.findById(id).toString());
        return userRepo.getById(id);
    }
    @Override
    public String signUpUser(User user) {
        if (user.getEmail()==null){
            throw new RuntimeException("Email can not be empty. ");}
        saveUser(user);
        Token token = new Token(user, LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15));
        token = tokenRepo.save(token);
        String link = "http://localhost:8080/user/confirm?token=" + token.getId();
        return emailService.send(user.getEmail(), emailService.buildEmail(user.getName(), link));
    }

    @Override
    public void confirm(Long token_id) {
        Token token = tokenRepo.getById(token_id);
        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            userRepo.deleteById(token.getUser().getId());
            tokenRepo.deleteById(token_id);
        } else{
            token.getUser().setEnabled(true);
        }
    }

    @Override
    public String enable_user(Long id) {
        checkById(id);
        User user = userRepo.getById(id);
        if(user.isEnabled()){return "User "+user.getUsername()+" is already enabled. ";}
        user.setEnabled(true);
        return " User " + user.getUsername() + " is enabled ";
    }

    @Override
    public String disable_user(Long id) {
        checkById(id);
        User user = userRepo.getById(id);
        if(!user.isEnabled()){return "User "+user.getUsername()+" is already disabled. ";}
        user.setEnabled(false);
        return " User " + user.getUsername() + " is disabled ";
    }

    @Override
    public String checkIfEnabled(Long id) {
        boolean enabled = tokenRepo.getById(id).getUser().isEnabled();
        if (enabled) {
            return "Your account is already verified ";
        }else{
            confirm(id);
        }
        boolean exists = tokenRepo.existsById(id);
        if (!exists) {
            return "Your account has been deleted because it has not been verified";
        }
        Token token = tokenRepo.getById(id);
        return " Welcome "+ token.getUser().getUsername()+ ". Your account is verified successfully";
    }

    @Override
    public void checkById(Long id) {
        boolean exists = userRepo.existsById(id);
        if (!exists) {
            throw new IllegalStateException("The User with ID that is entered does not exist. ");
        }
    }

}