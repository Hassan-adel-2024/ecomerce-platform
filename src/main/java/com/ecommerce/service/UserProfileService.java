package com.ecommerce.service;

import com.ecommerce.dto.UserProfileDto;
import com.ecommerce.entity.AppUser;
import com.ecommerce.entity.UserProfile;
import com.ecommerce.exceptions.UserNotFoundException;
import com.ecommerce.mapper.UserProfileMapper;
import com.ecommerce.repository.AppUserRepo;
import com.ecommerce.repository.UserProfileRepo;
import com.ecommerce.utility.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserProfileService {
    private final UserProfileRepo userProfileRepo;
    private final UserProfileMapper userProfileMapper;
    private final AppUserRepo appUserRepo;

    public UserProfile addProfile(UserProfileDto userProfileDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser appUser;
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

        return userProfileRepo.save(userProfile);
    }

    public UserProfileDto updateProfile(UserProfileDto userProfileDto) {
        Long userId = SecurityUtils.getAuthenticatedUserId();
        if (userId == null) {
            throw new UserNotFoundException("User not authenticated");
        }

        AppUser appUser = appUserRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
//        UserProfile userProfile = userProfileMapper.dtoToEntity(userProfileDto);

        UserProfile userProfile = userProfileRepo.findById(userId).orElse(new UserProfile());
        System.out.println("---------------> "+userProfile);
        userProfile.setUser(appUser);
        userProfile.setEmail(appUser.getEmail());
        userProfileMapper.updateEntityFromDto(userProfileDto, userProfile);
        userProfileRepo.save(userProfile);

        return userProfileMapper.entityToDto(userProfile);

    }
}
