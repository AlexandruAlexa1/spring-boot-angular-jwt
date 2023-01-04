package com.aa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aa.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

}
