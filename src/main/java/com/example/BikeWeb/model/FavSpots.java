package com.example.BikeWeb.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fav_spots")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class FavSpots {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @Column(name = "user_id")
    private long userId;       
}
