package com.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(
        name = "app_user",
        indexes = {
                @Index(name = "idx_email", columnList = "email")
        }
)
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false , unique = true)
    @Email(message = "Invalid email format")
    private String email;
    @Column(nullable = false)
    @Size(min = 4 ,message = "password must be at least 8 characters ")
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
    @Override
    public String toString() {
        return "AppUser [userId=" + userId + ", email=" +
                email + ", password=" + password + ", role=" + role;
    }

}
