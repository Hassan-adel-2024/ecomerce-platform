package com.ecommerce.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class UserProfile {
    @Id
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDateTime dateOfBirth;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // relations
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_profile_id")
    private AppUser user;
}
