package com.ecommerce.repository;

import com.ecommerce.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AppUserRepo extends JpaRepository<AppUser, Long> {
 Optional<AppUser> findByEmail(String email);
 boolean existsByEmail(String email);
 @Query("SELECT u FROM AppUser u JOIN FETCH u.role WHERE u.email = :email")
 Optional<AppUser> findByEmailWithRole(String email);
}
