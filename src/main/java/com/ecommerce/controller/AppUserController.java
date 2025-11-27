package com.ecommerce.controller;

import com.ecommerce.dto.AppUserDtoRequest;
import com.ecommerce.dto.AppUserDtoResponse;
import com.ecommerce.dto.UserProfileDto;
import com.ecommerce.exceptions.EmailAlreadyExists;
import com.ecommerce.service.AppUserService;
import com.ecommerce.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AppUserController {
    private final AppUserService appUserService;
    private final UserProfileService userProfileService;

    @PostMapping("/register")
    public ResponseEntity<AppUserDtoResponse> createUser(@Valid @RequestBody AppUserDtoRequest appUserDtoRequest) throws EmailAlreadyExists {
        AppUserDtoResponse appUser = appUserService.createUser(appUserDtoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(appUser);
    }
   @PostMapping("/profile/add")
    public ResponseEntity<UserProfileDto> setProfile(@RequestBody UserProfileDto userProfileDto) {
        userProfileService.addProfile(userProfileDto);
        return ResponseEntity.status(HttpStatus.OK).body(userProfileDto);
   }
    @PutMapping("/profile/update")
    public ResponseEntity<UserProfileDto> updateProfile(@RequestBody UserProfileDto userProfileDto) {
        UserProfileDto updatedUser = userProfileService.updateProfile(userProfileDto);

        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }
}
