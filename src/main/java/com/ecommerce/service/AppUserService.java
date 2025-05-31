package com.ecommerce.service;

import com.ecommerce.dto.AppUserDtoRequest;
import com.ecommerce.dto.AppUserDtoResponse;
import com.ecommerce.entity.AppUser;
import com.ecommerce.entity.Role;
import com.ecommerce.exceptions.EmailAlreadyExists;
import com.ecommerce.mapper.AppUserMapper;
import com.ecommerce.repository.AppUserRepo;
import com.ecommerce.repository.RoleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserService {
    private final AppUserRepo appUserRepo;
    private final RoleRepo roleRepo;
    private final AppUserMapper appUserMapper;
    public AppUserDtoResponse crateUser(AppUserDtoRequest appUserDtoRequest)  {
        validateEmailDoesNotExist(appUserDtoRequest.getEmail());
        AppUser appUser = appUserMapper.dtoToEntity(appUserDtoRequest);
        Long roleId = appUserDtoRequest.getRoleId();
        Role role = roleRepo.findById(roleId).
                orElseThrow(() ->  new RuntimeException("role not found") );
        appUser.setRole(role);


        AppUser savedUser = appUserRepo.save(appUser);
        AppUserDtoResponse appUserDtoResponse = appUserMapper.entityToResponse(savedUser);
        return appUserDtoResponse;
    }


    private void validateEmailDoesNotExist(String email) {
        if(appUserRepo.existsByEmail(email)) {
            throw new EmailAlreadyExists(email);
        }
    }
    public AppUser getUserById(Long userId) {
        return appUserRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

}
