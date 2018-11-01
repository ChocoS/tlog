package com.pwawrzyniak.tlog.backend.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class Bill {

  private LocalDate date;
  private List<BillItem> billItems;
}