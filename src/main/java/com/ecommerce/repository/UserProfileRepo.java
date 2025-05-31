package com.ecommerce.repository;

import com.ecommerce.entity.AppUser;
import com.ecommerce.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepo extends JpaRepository<UserProfile, Long> {
    UserProfile findByUser(AppUser user);
}
