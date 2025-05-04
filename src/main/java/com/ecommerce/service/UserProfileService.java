package com.ecommerce.service;

import com.ecommerce.dto.UserProfileDto;
import com.ecommerce.entity.AppUser;
import com.ecommerce.entity.UserProfile;
import com.ecommerce.exceptions.UserNotFoundException;
import com.ecommerce.mapper.UserProfileMapper;
import com.ecommerce.repository.AppUserRepo;
import com.ecommerce.repository.UserProfileRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class UserProfileService {
    private final UserProfileRepo userProfileRepo;
    private final UserProfileMapper userProfileMapper;
    private final AppUserRepo appUserRepo;

    public UserProfile addProfile(UserProfileDto userProfileDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser appUser = null;
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String username = authentication.getName();
            appUser=appUserRepo.findByEmail(username).orElseThrow(()-> new UserNotFoundException("User not found"));

        }
        else {
            throw new RuntimeException("User not authenticated");
        }
        UserProfile userProfile = userProfileMapper.dtoToEntity(userProfileDto);
        System.out.println("---------------> "+userProfile);
        userProfile.setUser(appUser);
//        userProfile.setUserId(appUser.getUserId());
        userProfile.setEmail(appUser.getEmail());
        userProfile.setCreatedAt(LocalDateTime.now());
        userProfile.setUpdatedAt(LocalDateTime.now());
        return userProfileRepo.save(userProfile);
    }
}
