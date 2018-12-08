package com.pwawrzyniak.tlog.backend.repository;

import com.pwawrzyniak.tlog.backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

  Role findByName(String roleName);
}