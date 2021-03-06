package com.pwawrzyniak.tlog.backend.repository;

import com.pwawrzyniak.tlog.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  User findOneByUsername(String username);
}