package com.ecommerce.controller;

import com.ecommerce.dto.AppUserDto;
import com.ecommerce.entity.AppUser;
import com.ecommerce.exceptions.EmailAlreadyExists;
import com.ecommerce.service.AppUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AppUserController {
    private final AppUserService appUserService;

    @GetMapping("/register")
    public ResponseEntity<AppUser> createUser(@Valid @RequestBody AppUserDto appUserDto) throws EmailAlreadyExists {
        AppUser appUser = appUserService.crateUser(appUserDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(appUser);
    }

}
