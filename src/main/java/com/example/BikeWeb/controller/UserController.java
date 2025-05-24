package com.example.BikeWeb.controller;

import com.example.BikeWeb.model.FavSpots;
import com.example.BikeWeb.model.User;
import com.example.BikeWeb.repository.UserRepository;
import com.example.BikeWeb.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.example.BikeWeb.repository.FavSpotsRepository;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository usersRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private FavSpotsRepository favSpots;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/all")
    public ResponseEntity<?> getAll(@RequestHeader("Authorization") String token){
        if (!authService.isAdmin(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(usersRepository.findAll());
    }

    @GetMapping("/get-all-fav-places")
    public ResponseEntity<?> getAllFavPlaces(@RequestHeader("Authorization") String authorizationToken){
        Optional<User> userOPT = usersRepository.findByToken(authorizationToken);
        Optional<FavSpots[]> spots = favSpots.findAllByUserId(userOPT.get().getId());
        //if (!authService.isAdmin(authorizationToken)) {
            //return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        //}
        return ResponseEntity.ok().body(spots);
    }


    @GetMapping("/")
    public ResponseEntity<?> getByToken(@RequestHeader("Authorization") String token){
        Optional<User> userOPT = usersRepository.findByToken(token);
        if(userOPT.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.ok(userOPT.get());
    }

    @PutMapping("/modify/{id}")
    public ResponseEntity<?> modifyUser(@RequestHeader("Authorization") String token, @PathVariable long id, @RequestBody User userBody){
        Optional<User> userOPTtoken = usersRepository.findByToken(token);
        Optional<User> userOPT = usersRepository.findById(id);

        if(userOPT.isEmpty() || userOPTtoken.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        if(userOPT.get().getId()!=userOPTtoken.get().getId() && !authService.isAdmin(token))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        User user = userOPT.get();
        user.setName(userBody.getName());
        user.setEmail(userBody.getEmail());
        user.setSurname(userBody.getSurname());
        return ResponseEntity.ok(usersRepository.save(user));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String token, @PathVariable long id){
        if (!authService.isAdmin(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<User> userOPT = usersRepository.findById(id);

        if(userOPT.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        usersRepository.deleteById(id);
        return ResponseEntity.ok().body("Deleted "+id);
    }
}
