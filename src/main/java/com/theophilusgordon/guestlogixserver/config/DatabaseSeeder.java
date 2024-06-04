package com.theophilusgordon.guestlogixserver.config;

import com.theophilusgordon.guestlogixserver.user.Role;
import com.theophilusgordon.guestlogixserver.user.Status;
import com.theophilusgordon.guestlogixserver.user.User;
import com.theophilusgordon.guestlogixserver.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DatabaseSeeder {
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initAdminUser(UserRepository userRepository){
        return args -> {
            String adminEmail = "gordonfiifi@gmail.com";
            if (!userRepository.existsByEmail(adminEmail)) {
                User admin = new User();
                admin.setEmail(adminEmail);
                admin.setFirstName("Theophilus");
                admin.setLastName("Gordon");
                admin.setDepartment("Some Department");
                admin.setPassword(passwordEncoder.encode("adminPassword@2024"));
                admin.setRole(Role.ADMIN);
                admin.setStatus(Status.ACTIVE);
                userRepository.save(admin);
            }
        };
    }
}
