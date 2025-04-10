package com.example.BikeWeb.services;

import com.example.BikeWeb.model.User;
import com.example.BikeWeb.model.enums.UserRoleEnum;
import com.example.BikeWeb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UserRepository usersRepository;

    public boolean isAdmin(String authorizationToken){
        Optional<User> user = usersRepository.findByToken(authorizationToken);
        return user.isPresent() && user.get().getRole() == UserRoleEnum.admin;
    }
}
