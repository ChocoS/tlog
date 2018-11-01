package com.pwawrzyniak.tlog.backend.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class BillItem {

  private Long cost;
  private String description;
  private List<String> tags;
  private String expression;
}