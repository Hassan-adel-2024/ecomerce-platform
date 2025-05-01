package com.ecommerce.repository;

import com.ecommerce.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepo extends JpaRepository<AppUser, Long> {
 AppUser findByEmail(String email);
 boolean existsByEmail(String email);
}
