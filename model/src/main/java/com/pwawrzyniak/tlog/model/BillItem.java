package com.pwawrzyniak.tlog.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class BillItem {

  private BigDecimal cost;
  private String description;
  private List<Label> labelList;
  private String expression;
}