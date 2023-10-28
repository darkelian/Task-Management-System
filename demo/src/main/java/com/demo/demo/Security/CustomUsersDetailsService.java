package com.demo.demo.Security;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.demo.demo.Models.Roles;
import com.demo.demo.Models.Users;
import com.demo.demo.Repositories.IUsersRepository;

@Service
public class CustomUsersDetailsService implements UserDetailsService{
    private IUsersRepository usersRepo;

    public CustomUsersDetailsService(IUsersRepository usersRepo) {
        this.usersRepo = usersRepo;
    }
    public Collection<GrantedAuthority> mapToAuthorities(List<Roles> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = usersRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return new User(users.getUsername(), users.getPassword(), mapToAuthorities(users.getRoles()));
    }
}
