package com.kitap.kitap.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kitap.kitap.Repo.TokenRepo;
import com.kitap.kitap.Repo.UserRepo;
import com.kitap.kitap.domain.Role;
import com.kitap.kitap.domain.Token;
import com.kitap.kitap.domain.User;
import com.kitap.kitap.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;


@RestController
@RequestMapping("/user")
public class UserResource {
    private final UserService userService;
    @Autowired
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    //TODO:MAYBE PUT MAPPING FOR USERS USERS SHOULD BE ABLE TO CHANGE THEIR ACCOUNTS; DELETE USERS
    //TODO: CHANGE THE NAME KITAP TO BOOK; SEPARATE CONTROLLERS

    @GetMapping("/all")
    public ResponseEntity<List<User>>getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User>getUsers(@PathVariable Long id) {
        return ResponseEntity.ok().body(userService.getUser(id));
    }
    @GetMapping
    public String get(){
        return "Please specify ID of user to drawn. ";
    }
    @PostMapping("/save")
    public ResponseEntity<User>saveUser(@RequestBody User user) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(user));
    }

    @PostMapping("/signup")
    public String userSignIn(@RequestBody User user) {
        return userService.signUpUser(user);
    }

    @GetMapping("/confirm")
    public String userConfirm(@RequestParam("token") Long token_id) {
        return userService.checkIfEnabled(token_id);
    }
    @PostMapping("/enable/{id}")
    public String enable(@PathVariable("id") Long id){
        return userService.enable_user(id);
    }
    @PostMapping("/disable/{id}")
    public String disable(@PathVariable("id") Long id){
        return userService.disable_user(id);
    }
}
