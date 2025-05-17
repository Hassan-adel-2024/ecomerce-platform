package com.ecommerce.controller;

import com.ecommerce.dto.LoginRequestDto;
import com.ecommerce.dto.LoginResponseDto;
import com.ecommerce.service.UserAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserAuthenticationService userAuthenticationService;
     @PostMapping("/login")
     public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequest) {
         LoginResponseDto response = userAuthenticationService.login(loginRequest);
         return ResponseEntity.ok(response);
    }

}
