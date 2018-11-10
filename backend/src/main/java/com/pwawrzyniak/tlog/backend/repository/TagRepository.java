package com.pwawrzyniak.tlog.backend.repository;

import com.pwawrzyniak.tlog.backend.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, String> {
}