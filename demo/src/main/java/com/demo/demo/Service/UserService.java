package com.demo.demo.Service;

import com.demo.demo.Dtos.PasswordUpdateDto;
import com.demo.demo.Models.Roles;
import com.demo.demo.Models.Users;
import com.demo.demo.Repositories.IRolesRepository;
import com.demo.demo.Repositories.IUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private IUsersRepository usersRepository;

    @Autowired
    private IRolesRepository rolesRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Users createUser(String username, String password, String role) {
        Users user = new Users();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        Roles userRole = rolesRepository.findByName(role)
                .orElseGet(() -> {
                    Roles newRole = new Roles();
                    newRole.setName(role);
                    return rolesRepository.save(newRole);
                });

        user.setRoles(Collections.singletonList(userRole));
        return usersRepository.save(user);
    }

    public boolean updatePassword(String username, PasswordUpdateDto passwordUpdateDto) {
        Optional<Users> userOptional = usersRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            user.setPassword(passwordEncoder.encode(passwordUpdateDto.getNewPassword()));
            usersRepository.save(user);
            return true;
        }
        return false;
    }

    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    public Users getUserById(Long userId) {
        return usersRepository.findById(userId).orElse(null);
    }

    public boolean deleteUser(Long userId) {
        if (usersRepository.existsById(userId)) {
            usersRepository.deleteById(userId);
            return true;
        }
        return false;
    }
}
