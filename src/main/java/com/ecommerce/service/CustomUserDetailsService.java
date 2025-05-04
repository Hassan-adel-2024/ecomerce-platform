package com.ecommerce.service;

import com.ecommerce.entity.AppUser;
import com.ecommerce.repository.AppUserRepo;
import com.ecommerce.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final AppUserRepo appUserRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email "+username));

        return new CustomUserDetails(appUser);
    }
}
