package com.demo.demo.Repositories;

import com.demo.demo.Models.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface IRolesRepository extends JpaRepository<Roles, Long>{
    public Optional<Roles> findByName(String name);
    public boolean existsByName(String name);
}
