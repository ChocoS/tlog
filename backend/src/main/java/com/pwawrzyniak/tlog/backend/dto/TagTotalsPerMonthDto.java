package com.pwawrzyniak.tlog.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagTotalsPerMonthDto {

  private LocalDate date;
  private Map<String, String> tagTotalMap;
}