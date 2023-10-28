package com.demo.demo.Config;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import com.demo.demo.Models.Roles;
import com.demo.demo.Repositories.IRolesRepository;

import jakarta.annotation.PostConstruct;
@Component
public class DatabaseInitializer {
    @Autowired
    private IRolesRepository rolesRepository;

    @PostConstruct
    public void init() {
        // Asegurar que los roles ADMIN y USER estén presentes en la base de datos
        if (!rolesRepository.existsByName("ADMIN")) {
            Roles adminRole = new Roles();
            adminRole.setName("ADMIN");
            rolesRepository.save(adminRole);
        }
        if (!rolesRepository.existsByName("USER")) {
            Roles userRole = new Roles();
            userRole.setName("USER");
            rolesRepository.save(userRole);
        }
    }
}
