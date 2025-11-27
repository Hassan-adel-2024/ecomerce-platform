package com.ecommerce.service;

import com.ecommerce.dto.LoginRequestDto;
import com.ecommerce.dto.LoginResponseDto;
import com.ecommerce.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

//    public UserAuthenticationService(AuthenticationManager authenticationManager) {
//        this.authenticationManager = authenticationManager;
//    }

    public LoginResponseDto login(LoginRequestDto loginRequest) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String accessToken = jwtService.generateToken(userDetails);
        return new LoginResponseDto(accessToken);

    }
}
