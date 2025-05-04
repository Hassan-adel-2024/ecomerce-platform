package com.ecommerce.dto;

import com.ecommerce.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AppUserDtoResponse {
    private Long userId;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Role role; // Consider using RoleDto instead
    private UserProfileDto profile; // Optional, if needed
    private List<AddressDto> addressList; // Optional, if needed
}
