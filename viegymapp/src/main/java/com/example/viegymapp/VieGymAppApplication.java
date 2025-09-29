package com.example.viegymapp;

import com.example.viegymapp.entity.Enum.ERole;
import com.example.viegymapp.entity.Role;
import com.example.viegymapp.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class VieGymAppApplication {
	public static void main(String[] args) {
		SpringApplication.run(VieGymAppApplication.class, args);
	}

	@Bean
	CommandLineRunner run(RoleRepository roleRepository) {
		return args -> {
			if (roleRepository.findByName(ERole.ROLE_USER).isEmpty()) {
				roleRepository.save(new Role(ERole.ROLE_USER));
			}
			if (roleRepository.findByName(ERole.ROLE_ADMIN).isEmpty()) {
				roleRepository.save(new Role(ERole.ROLE_ADMIN));
			}
			if (roleRepository.findByName(ERole.ROLE_COACH).isEmpty()) {
				roleRepository.save(new Role(ERole.ROLE_COACH));
			}
		};
	}
}

