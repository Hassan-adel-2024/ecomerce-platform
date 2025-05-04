package com.ecommerce.mapper;

import com.ecommerce.dto.AppUserDtoRequest;
import com.ecommerce.dto.AppUserDtoResponse;
import com.ecommerce.entity.Address;
import com.ecommerce.entity.AppUser;
import com.ecommerce.entity.Role;
import com.ecommerce.entity.UserProfile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

class AppUserMapperTest {

    private AppUserMapper mapper;
    @BeforeEach
    void setUp() {
        mapper = new AppUserMapperImpl();
    }
    @Test
    public void shouldMapUserDtoRequestToAppUser() {
        AppUserDtoRequest appUserDtoRequest =
                new AppUserDtoRequest("user1@example.com","1234",1L);
        AppUser appUser = mapper.dtoToEntity(appUserDtoRequest);
        Assertions.assertEquals(appUserDtoRequest.getEmail(),appUser.getEmail());
        Assertions.assertEquals(appUserDtoRequest.getPassword(),appUser.getPassword());
        Assertions.assertNotNull(appUser.getEmail());
        Assertions.assertNotNull(appUser.getPassword());

    }

    @Test
    public void shouldMapAppUserToAppUserDtoResponse() {
        Role role = new Role(1L, "ROLE_USER", "Standard user role");

        // 2. Create a UserProfile
        UserProfile profile = new UserProfile();
        profile.setFirstName("John");
        profile.setLastName("Doe");
        profile.setPhoneNumber("+1234567890");
        profile.setDateOfBirth(LocalDate.of(1990, 1, 1));

        // 3. Create Addresses
        Address address1 = new Address();
        address1.setFullAddress("123 Main St");
        address1.setCity("New York");
        address1.setCountry("USA");
        address1.setDefault(true);

        Address address2 = new Address();
        address2.setFullAddress("456 Oak Ave");
        address2.setCity("Boston");
        address2.setCountry("USA");

        List<Address> addresses = List.of(address1, address2);

        // 4. Create and return AppUser
        AppUser user = new AppUser();
        user.setUserId(1L);
        user.setEmail("john.doe@example.com");
        user.setPassword("securePassword123");
        user.setRole(role);
        user.setProfile(profile);
        user.setAddressList(addresses);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        // Bidirectional relationship setup
        profile.setUser(user);  // Link profile to user
        addresses.forEach(addr -> addr.setUser(user)); // Link addresses to user
        AppUserDtoResponse dtoResponse = mapper.entityToResponse(user);

        // Assertions
        Assertions.assertEquals(user.getEmail(), dtoResponse.getEmail());
        Assertions.assertEquals(user.getUserId(), dtoResponse.getUserId());
        Assertions.assertEquals(user.getCreatedAt(), dtoResponse.getCreatedAt());
        Assertions.assertEquals(user.getUpdatedAt(), dtoResponse.getUpdatedAt());

        // Compare Role fields
        Assertions.assertEquals(user.getRole().getRoleId(), dtoResponse.getRole().getRoleId());
        Assertions.assertEquals(user.getRole().getRoleName(), dtoResponse.getRole().getRoleName());

        // Compare UserProfile fields
        Assertions.assertEquals(user.getProfile().getFirstName(), dtoResponse.getProfile().getFirstName());
        Assertions.assertEquals(user.getProfile().getLastName(), dtoResponse.getProfile().getLastName());
        Assertions.assertEquals(user.getProfile().getPhoneNumber(), dtoResponse.getProfile().getPhoneNumber());

        // Compare Address fields (if needed)
        Assertions.assertEquals(user.getAddressList().size(), dtoResponse.getAddressList().size());
        Assertions.assertEquals(user.getAddressList().get(0).getCity(), dtoResponse.getAddressList().get(0).getCity());

    }

}