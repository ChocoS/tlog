package com.pwawrzyniak.tlog.backend.repository;

import com.pwawrzyniak.tlog.backend.entity.BillItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillItemRepository extends JpaRepository<BillItem, Long> {
}