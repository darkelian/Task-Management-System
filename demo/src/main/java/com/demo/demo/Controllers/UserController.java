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

import com.demo.demo.Dtos.PasswordUpdateDto;
import com.demo.demo.Dtos.LoginRequest;
import com.demo.demo.Models.Users;
import com.demo.demo.Service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    // Registrar un nuevo usuario con rol de cliente
    @PostMapping("/register")
    public ResponseEntity<Users> registerUser(@Valid @RequestBody LoginRequest registrationDto) {
        Users newUser = userService.createUser(registrationDto.getUsername(), registrationDto.getPassword(), "USER");
        return ResponseEntity.ok(newUser);
    }

    // Actualizar contraseña
    @PutMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestParam String username, @RequestBody PasswordUpdateDto passwordUpdateDto) {
        boolean isUpdated = userService.updatePassword(username, passwordUpdateDto);
        if (isUpdated) {
            return ResponseEntity.ok("Contraseña actualizada con éxito");
        } else {
            return ResponseEntity.badRequest().body("Error al actualizar la contraseña");
        }
    }

    // Obtener todos los usuarios
    @GetMapping("/")
    public ResponseEntity<List<Users>> getAllUsers() {
        List<Users> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Buscar un usuario por ID
    @GetMapping("/{userId}")
    public ResponseEntity<Users> getUserById(@PathVariable Long userId) {
        Users user = userService.getUserById(userId);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar un usuario
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        boolean isDeleted = userService.deleteUser(userId);
        if (isDeleted) {
            return ResponseEntity.ok("Usuario eliminado con éxito");
        } else {
            return ResponseEntity.badRequest().body("Error al eliminar el usuario");
        }
    }
}
