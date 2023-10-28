package com.demo.demo.Controllers;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.demo.demo.Dtos.PasswordUpdateDto;
import com.demo.demo.Dtos.LoginRequest;
import com.demo.demo.Models.Users;
import com.demo.demo.Service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    // Registrar un nuevo usuario con rol de cliente
    @PostMapping("/register")
    public ResponseEntity<Users> registerUser(@Valid @RequestBody LoginRequest registrationDto) {
        logger.info("Registering new user: {}", registrationDto.getUsername());
        Users newUser = userService.createUser(registrationDto.getUsername(), registrationDto.getPassword(), "USER");
        logger.info("User registered successfully: {}", newUser.getUsername());
        return ResponseEntity.ok(newUser);
    }

    // Actualizar contraseña
    @PutMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestParam String username, @RequestBody PasswordUpdateDto passwordUpdateDto) {
        logger.info("Updating password for user: {}", username);
        boolean isUpdated = userService.updatePassword(username, passwordUpdateDto);
        if (isUpdated) {
            logger.info("Password updated successfully for user: {}", username);
            return ResponseEntity.ok("Contraseña actualizada con éxito");
        } else {
            logger.warn("Error updating password for user: {}", username);
            return ResponseEntity.badRequest().body("Error al actualizar la contraseña");
        }
    }

    // Obtener todos los usuarios
    @GetMapping("/")
    public ResponseEntity<List<Users>> getAllUsers() {
        logger.info("Fetching all users");
        List<Users> users = userService.getAllUsers();
        logger.info("Fetched {} users", users.size());
        return ResponseEntity.ok(users);
    }

    // Buscar un usuario por ID
    @GetMapping("/{userId}")
    public ResponseEntity<Users> getUserById(@PathVariable Long userId) {
        logger.info("Fetching user with ID: {}", userId);
        Users user = userService.getUserById(userId);
        if (user != null) {
            logger.info("User fetched successfully: {}", user.getUsername());
            return ResponseEntity.ok(user);
        } else {
            logger.warn("User not found with ID: {}", userId);
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar un usuario
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        logger.info("Deleting user with ID: {}", userId);
        boolean isDeleted = userService.deleteUser(userId);
        if (isDeleted) {
            logger.info("User deleted successfully with ID: {}", userId);
            return ResponseEntity.ok("Usuario eliminado con éxito");
        } else {
            logger.warn("Error deleting user with ID: {}", userId);
            return ResponseEntity.badRequest().body("Error al eliminar el usuario");
        }
    }
}
