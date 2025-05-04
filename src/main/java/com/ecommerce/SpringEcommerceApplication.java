package com.ecommerce;

import com.ecommerce.dto.AppUserDtoRequest;
import com.ecommerce.entity.Role;
import com.ecommerce.repository.AppUserRepo;
import com.ecommerce.repository.RoleRepo;
import com.ecommerce.service.AppUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SpringEcommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringEcommerceApplication.class, args);
	}
	@Bean
	CommandLineRunner initializeBasicData(
			RoleRepo roleRepository,
			AppUserRepo appUserRepository,
			AppUserService appUserService, PasswordEncoder passwordEncoder) {

		return args -> {
			// Skip if data already exists
			if (roleRepository.count() == 0) {
				// Create roles
				Role userRole = roleRepository.save(new Role("ROLE_USER", "Regular user"));
				Role adminRole = roleRepository.save(new Role("ROLE_ADMIN", "Administrator"));
				appUserService.crateUser(new AppUserDtoRequest("user1@example.com",passwordEncoder.encode("1234"),2L));
				appUserService.crateUser(new AppUserDtoRequest("user2@example.com",passwordEncoder.encode("1234"),1L));
			}
		};
}
}
