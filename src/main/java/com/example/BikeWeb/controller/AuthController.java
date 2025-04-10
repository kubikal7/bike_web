package com.example.BikeWeb.controller;

import com.example.BikeWeb.model.User;
import com.example.BikeWeb.model.enums.UserRoleEnum;
import com.example.BikeWeb.repository.UserRepository;
import com.example.BikeWeb.services.AuthService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository usersRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private AuthService authService;

    @Getter
    @Setter
    public static class LoginRequest {
        private String email;
        private String password;
    }

    @GetMapping("/isadmin")
    public ResponseEntity<?> isAdmin(@RequestHeader("Authorization") String authorizationToken){
        if(!authService.isAdmin(authorizationToken)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        else
            return ResponseEntity.ok().build();
    }

    @PutMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Optional<User> userOPT = usersRepository.findByEmail(loginRequest.getEmail());

        //poprawność hasła (porównanie zahashowanego hasła)
        if (userOPT.isPresent() && passwordEncoder.matches(loginRequest.getPassword(), userOPT.get().getPassword())) {
            User user = userOPT.get();
            String token = generateUniqueToken();

            //token do bazy danych
            user.setToken(token);
            usersRepository.save(user);

            return ResponseEntity.ok(token);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    private String generateUniqueToken() {
        String token;
        do {
            token = UUID.randomUUID().toString();  //generowanie nowego tokena
        } while (usersRepository.existsByToken(token));  //czy token już istnieje w bazie
        return token;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User newUser, @RequestHeader("Authorization") String authorizationToken) {
        if(newUser.getRole() != UserRoleEnum.admin && newUser.getRole() != UserRoleEnum.user) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request");
        }

        if(newUser.getRole() == UserRoleEnum.admin){
            if(!authService.isAdmin(authorizationToken)){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        }

        //czy użytkownik o takim e-mailu już istnieje
        if (usersRepository.findByEmail(newUser.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already in use");
        }

        //haszowanie hasła
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        usersRepository.save(newUser);

        return ResponseEntity.ok("User registered successfully");
    }
}
