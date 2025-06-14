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


}
