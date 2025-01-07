package com.team4.nottumblr.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team4.nottumblr.model.Roles;

public interface RolesRepository extends JpaRepository<Roles, Integer> {
    Roles findByRoleName(String roleName);
}
