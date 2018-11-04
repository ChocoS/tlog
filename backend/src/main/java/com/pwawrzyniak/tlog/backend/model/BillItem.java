package com.pwawrzyniak.tlog.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Set;

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

  @Column(nullable = false)
  private Long cost;

  @Column
  private String description;

  @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
  @JoinTable(name = "bill_item_tags",
      joinColumns = {@JoinColumn(name = "bill_item_id")},
      inverseJoinColumns = {@JoinColumn(name = "tag_name")})
  private Set<Tag> tags;

  @Column(nullable = false)
  private String expression;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "bill_id", nullable = false)
  private Bill bill;
}