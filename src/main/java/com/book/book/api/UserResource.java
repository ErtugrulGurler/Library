package com.book.book.api;

import com.book.book.domain.User;
import com.book.book.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserResource {
    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<User>>getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<User>getUser(@PathVariable("id") Long id) {
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
    @DeleteMapping(path = "{user_id}")
    public String deleteMappingUser(@PathVariable("user_id") Long user_no) {
        return userService.deleteUser(user_no);
    }
    @DeleteMapping
    public void deleteMappingUser() {
        throw new RuntimeException("Please specify the ID of user to be deleted. ");
    }

    @GetMapping("/signup")
    public String userSignIn(@RequestBody User user) {
        return userService.signUpUser(user);
    }

    @PostMapping("/enable/{id}")
    public String enable(@PathVariable("id") Long id){
        return userService.enable_user(id);
    }
    @PostMapping("/disable/{id}")
    public String disable(@PathVariable("id") Long id){
        return userService.disable_user(id);
    }
    @GetMapping("/confirm")
    public String userConfirm(@RequestParam("token") Long token_id) {
        return userService.checkIfEnabled(token_id);
    }
    @PutMapping
    public User putMappingUser(@RequestBody User user) {
        return userService.putUser(user);
    }
    @PutMapping(path = "{id}")
    public User Put(@PathVariable("id") Long id, @RequestBody User user) {
        return userService.putUser(id,user);
    }

}
