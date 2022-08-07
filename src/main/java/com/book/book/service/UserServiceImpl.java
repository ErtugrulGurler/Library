package com.book.book.service;
import com.book.book.Repo.TokenRepo;
import com.book.book.domain.Token;
import com.book.book.domain.User;
import com.book.book.Repo.UserRepo;
import com.book.book.email.EmailService;
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
import java.util.Optional;

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
    public void deleteUser(Long user_no) {
        checkById(user_no);
        userRepo.deleteById(user_no);
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
        log.info("Fetching user {}", userRepo.findById(id).get().getUsername().toString());
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
    //#############################################################################################################

    @Override
    public User putUser(User user) {
        if (user.getId()==null){
            throw new RuntimeException("ID is not entered");
        }
        return changeUser(user);
    }
    @Override
    public User putUser(Long id,User user) {
        if (user.getId()==null){
            user.setId(id);
            return putUser(user);
        } else if(!user.getId().equals(id)){
            throw new RuntimeException("Path variable and the book that is entered are not the same");
        }
        return changeUser(user);
    }
    public User changeUser(User user){
        checkById(user.getId());
        Optional<User> userFind = userRepo.findById(user.getId());
        User dbUser = userRepo.getById(user.getId());

        if (userFind.isPresent()) {
            if (user.getName() == null) {
                user.setName(dbUser.getName());
            } else {
                dbUser.setName(user.getName());
            }
            if (user.getUsername() == null) {
                user.setUsername(dbUser.getUsername());
            } else {
                dbUser.setUsername(user.getUsername());
            }
            if (user.getPassword() == null) {
                user.setPassword(dbUser.getPassword());
            } else {
                dbUser.setPassword(user.getPassword());
            }
            if (user.getEmail() == null) {
                user.setEmail(dbUser.getEmail());
            } else {
                dbUser.setEmail(user.getEmail());
            }
        }

        dbUser = user;
        userRepo.save(dbUser);
        return userRepo.getById(dbUser.getId());
    }

    @Override
    public void checkById(Long id) {
        boolean exists = userRepo.existsById(id);
        if (!exists) {
            throw new IllegalStateException("The User with ID that is entered does not exist. ");
        }
    }

}