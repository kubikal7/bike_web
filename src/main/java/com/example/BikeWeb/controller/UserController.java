package com.example.BikeWeb.controller;

import com.example.BikeWeb.model.FavSpots;
import com.example.BikeWeb.model.User;
import com.example.BikeWeb.repository.UserRepository;
import com.example.BikeWeb.services.AuthService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Optionals;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.example.BikeWeb.repository.FavSpotsRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    @GetMapping("/find-by-email/{email}")
    public ResponseEntity<?> getAll(@RequestHeader("Authorization") String token, @PathVariable String email){
        if (!authService.isAdmin(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(usersRepository.findByEmail(email));
    }

    @GetMapping("/get-all-fav-places")
    public ResponseEntity<List<FavSpots>> getAllFavPlaces(@RequestHeader("Authorization") String authorizationToken) {
        Optional<User> userOPT = usersRepository.findByToken(authorizationToken);

        List<FavSpots> spotsList = new ArrayList<>();

        if (userOPT.isPresent()) {
            Optional<FavSpots[]> spotsOpt = favSpots.findAllByUserId(userOPT.get().getId());
            if (spotsOpt.isPresent()) {
                spotsList = Arrays.asList(spotsOpt.get());
            }
        }

        return ResponseEntity.ok(spotsList);
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

    @Transactional
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String token, @PathVariable long id){
        if (!authService.isAdmin(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<User> userOPT = usersRepository.findById(id);

        if(userOPT.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        favSpots.deleteAllByUserId(id);
        usersRepository.deleteById(id);
        return ResponseEntity.ok().body("Deleted "+id);
    }
}
