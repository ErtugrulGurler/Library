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
import lombok.Data;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api")
@RequiredArgsConstructor

public class UserResource {
    private final UserService userService;
    private final UserRepo userRepo;
    private final TokenRepo tokenRepo;

    @GetMapping("/user")
    public ResponseEntity<List<User>>getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @PostMapping("/user/save")
    public ResponseEntity<User>saveUser(@RequestBody User user) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(user));
    }

    @PostMapping("/signup")
    public String userSignIn(@RequestBody User user) {
        return userService.signUpUser(user);
    }

    @GetMapping("/confirm")
    public String userConfirm(@RequestParam("token") Long token_id) {
        boolean enabled = tokenRepo.getById(token_id).getUser().isEnabled();
        if (enabled) {
            return "Your account is already verified " ;
        }
        userService.confirm(token_id);
        boolean exists = tokenRepo.existsById(token_id);
        if( !exists){
            return "Your account has been deleted because it has not been verified";
        }
        Token token = tokenRepo.getById(token_id);
        return " Welcome "+ token.getUser().getUsername()+ " Your account is verified successfully\n" + "\n enabled = " + token.getUser().isEnabled();
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role>saveRole(@RequestBody Role role) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }
    @PostMapping("/role/addtouser")
    public ResponseEntity<?>addRoleToUser(@RequestParam("username") String username, @RequestParam("rolename") String rolename) {
        userService.addRoleToUser(username, rolename);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/role/takerolefromuser")
    public ResponseEntity<?>takeRoleFromUser(@RequestParam("username") String username, @RequestParam("rolename") String rolename) {
        userService.takeRoleFromUser(username, rolename);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/enable_disable")
    public String enabled_change(@RequestParam("id") Long user_id, @RequestParam("enabled") boolean enabled) {
        User user = userRepo.getById(user_id);
        user.setEnabled(enabled);
        return "User " + user.getUsername() + "s activity changed to : "+ enabled ;
    }
    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                User user = userService.getUser(username);
                String access_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            }catch (Exception exception) {
                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }
}
