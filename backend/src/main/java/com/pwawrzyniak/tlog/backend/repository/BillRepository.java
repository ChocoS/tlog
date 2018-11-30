package com.pwawrzyniak.tlog.backend.repository;

import com.pwawrzyniak.tlog.backend.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

  List<Bill> findAllByOrderByDateDesc();
}