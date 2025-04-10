package com.example.BikeWeb.model;

import com.example.BikeWeb.model.enums.UserRoleEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String surname;
    private String email;
    private String password;
    @JsonIgnore
    private String token;
    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;
}
