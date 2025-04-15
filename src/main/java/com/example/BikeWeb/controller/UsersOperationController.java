package com.example.BikeWeb.controller;
import com.example.BikeWeb.model.FavSpots;
import com.example.BikeWeb.model.User;
import com.example.BikeWeb.repository.FavSpotsRepository;
import com.example.BikeWeb.repository.UserRepository;
import com.example.BikeWeb.services.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


import java.util.Optional;


@RestController
public class UsersOperationController {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private FavSpotsRepository favSpots;

    @Autowired
    private AuthService authService;

    @PostMapping("/add-fav-place")
    public ResponseEntity<?> addFavPlace(@RequestHeader("Authorization") String authorizationToken, @RequestBody FavSpots spot){
        Optional<User> userOPT = userRepository.findByToken(authorizationToken);

        if(!userOPT.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("you are not logged in");
        }
        if (spot.getName() == null || spot.getName().isEmpty() || spot.getName().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid spot name");
        }

        User usr = userOPT.get();
        spot.setName(spot.getName());
        spot.setUserId(usr.getId());

        favSpots.save(spot);

        return ResponseEntity.ok().body(spot);
    }

    @PostMapping("/add-fav-place-by-id/{id}")
    public ResponseEntity<?> addFavPlaceById(@RequestHeader("Authorization") String authorizationToken, @RequestBody FavSpots spot, @PathVariable long id){
        Optional<User> userOPT = userRepository.findByToken(authorizationToken);

        if(!userOPT.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("you are not logged in");
        }
        if(!authService.isAdmin(authorizationToken)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("you dont have admin permissions to do this action");
        }
        if (spot.getName() == null || spot.getName().isEmpty() || spot.getName().isBlank() || Long.valueOf(id)==null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid spot name");
        }
        spot.setName(spot.getName());
        spot.setUserId(id);
        favSpots.save(spot);

        return ResponseEntity.ok().body(spot);
    }
    @DeleteMapping("/del-fav-place")
    public ResponseEntity<?> delFavPlace(@RequestHeader("Authorization") String authorizationToken, @RequestBody FavSpots spot){
        Optional<User> userOPT = userRepository.findByToken(authorizationToken);
        if(!userOPT.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("you are not logged in");
        }

        if (spot.getName() == null || spot.getName().isEmpty() || spot.getName().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid spot name");
        }
        favSpots.deleteByNameAndUserId(spot.getName(), userOPT.get().getId());

        return ResponseEntity.ok().body("deleted fav spot from db");
    }
    @DeleteMapping("/del-fav-place-by-id/{id}")
    public ResponseEntity<?> delFavPlaceById(@RequestHeader("Authorization") String authorizationToken, @RequestBody FavSpots spot, @PathVariable long id){
        Optional<User> userOPT = userRepository.findByToken(authorizationToken);

        if(userOPT.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        if(!userOPT.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("you are not logged in");
        }
        if(!authService.isAdmin(authorizationToken)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("you dont have admin permissions to do this action");
        }
        if (spot.getName() == null || spot.getName().isEmpty() || spot.getName().isBlank() || Long.valueOf(id)==null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid spot name");
        }

        favSpots.deleteByNameAndUserId(spot.getName(), id);

        return ResponseEntity.ok().body("deleted fav spot from user id: "+id);
    }

}