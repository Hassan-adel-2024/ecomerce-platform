package com.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String email;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // relations
    @ManyToOne()
    @JoinColumn(name = "role_id",referencedColumnName = "roleId")
    private Role role;
    @OneToOne(cascade = CascadeType.ALL , mappedBy = "user")
    private UserProfile profile;
    @OneToMany(mappedBy = "user"  )
    private List<Address> addressList;
    @OneToOne(cascade = CascadeType.ALL , orphanRemoval = true)
    private Cart cart;
    @Override
    public String toString() {
        return "AppUser [userId=" + userId + ", email=" +
                email + ", password=" + password + ", role=" + role;
    }

}
