package com.pwawrzyniak.tlog.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class BillItem {

  @Id
  @GeneratedValue
  @Column(nullable = false)
  private Long id;

  @Column
  private Long cost;

  @Column
  private String description;

  @Column
  @ElementCollection(fetch = FetchType.EAGER)
  private List<String> tags;

  @Column
  private String expression;
}