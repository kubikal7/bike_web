package com.example.BikeWeb.controller;
import com.example.BikeWeb.model.TestModel;
import com.example.BikeWeb.model.User;
import com.example.BikeWeb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Test {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/test")
    public String test(){
        TestModel testModel = new TestModel(23);
        return testModel.toString();
    }

    @GetMapping("/users")
    public List<User> allUsers(){
        return userRepository.findAll();
    }
}
