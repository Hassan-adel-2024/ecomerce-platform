package com.ecommerce.service;

import com.ecommerce.dto.AppUserDto;
import com.ecommerce.entity.AppUser;
import com.ecommerce.entity.Role;
import com.ecommerce.exceptions.EmailAlreadyExists;
import com.ecommerce.mapper.AppUserMapper;
import com.ecommerce.repository.AppUserRepo;
import com.ecommerce.repository.RoleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AppUserService {
    private final AppUserRepo appUserRepo;
    private final RoleRepo roleRepo;
    private final AppUserMapper appUserMapper;
    public AppUser crateUser(AppUserDto appUserDto)  {
        validateEmailDoesNotExist(appUserDto.getEmail());
        AppUser appUser = appUserMapper.dtoToEntity(appUserDto);
        Long roleId = appUserDto.getRoleId();
        Role role = roleRepo.findById(roleId).
                orElseThrow(() ->  new RuntimeException("role not found") );
        appUser.setRole(role);
        appUser.setCreatedAt(LocalDateTime.now());
        appUser.setUpdatedAt(LocalDateTime.now());
        appUserRepo.save(appUser);
        return appUserRepo.save(appUser);
    }


    private void validateEmailDoesNotExist(String email) {
        if(appUserRepo.existsByEmail(email)) {
            throw new EmailAlreadyExists(email);
        }
    }

}
