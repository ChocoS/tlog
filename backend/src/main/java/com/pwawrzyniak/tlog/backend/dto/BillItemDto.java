package com.pwawrzyniak.tlog.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.TreeSet;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillItemDto {

  private String cost;
  private String description;
  private Set<String> tags;
  private String expression;

  public String display() {
    String tagsDisplay = "(" + String.join(", ", new TreeSet<>(tags)) + ")";
    if (description != null) {
      return cost + " " + tagsDisplay + " " + description;
    } else {
      return cost + " " + tagsDisplay;
    }
  }
}