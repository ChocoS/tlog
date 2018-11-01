package com.pwawrzyniak.tlog.backend.data;

import com.pwawrzyniak.tlog.backend.model.BillItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillItemRepository extends JpaRepository<BillItem, Long> {
}