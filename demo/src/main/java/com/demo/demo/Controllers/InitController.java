package com.demo.demo.Controllers;

import com.demo.demo.Dtos.StandardResponseDTO;
import com.demo.demo.Models.Roles;
import com.demo.demo.Models.Users;
import com.demo.demo.Repositories.IRolesRepository;
import com.demo.demo.Repositories.IUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Collections;

@RestController
@RequestMapping("/api")
public class InitController {
    private static final Logger logger = LoggerFactory.getLogger(InitController.class);
    @Autowired
    private IRolesRepository rolesRepository;

    @Autowired
    private IUsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${default-user.username}")
    private String defaultUsername;

    @Value("${default-user.password}")
    private String defaultPassword;

    @Value("${default-user.rol}")
    private String defaultRol;

    @GetMapping("/init")
    public StandardResponseDTO initializeDatabase() {
        logger.info("Initializing database...");
        StandardResponseDTO response = new StandardResponseDTO();
        if (!usersRepository.existsByUsername(defaultUsername)) {
            logger.info("Default user not found. Creating default user.");
            Roles role = rolesRepository.findByName(defaultRol)
                    .orElseGet(() -> {
                        logger.info("Default role not found. Creating default role.");
                        Roles newRole = new Roles();
                        newRole.setName(defaultRol);
                        return rolesRepository.save(newRole);
                    });
            Users user = new Users();
            user.setUsername(defaultUsername);
            user.setPassword(passwordEncoder.encode(defaultPassword));
            user.setRoles(Collections.singletonList(role));
            usersRepository.save(user);
            logger.info("User initialized successfully.");
            return response.FullSuccess("User initialized successfully.");
        }
        logger.warn("User already exists.");
        return response.FailSuccess("User already exists.");
    }
}
