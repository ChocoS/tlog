package com.pwawrzyniak.tlog.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
public class BillItem extends AuditModel {

  @Builder
  public BillItem(LocalDateTime createdAt, String createdBy, LocalDateTime lastModifiedAt, String lastModifiedBy,
                  Long id, BigDecimal cost, String description, Set<Tag> tags, String expression, Bill bill) {
    super(createdAt, createdBy, lastModifiedAt, lastModifiedBy);
    this.id = id;
    this.cost = cost;
    this.description = description;
    this.tags = tags;
    this.expression = expression;
    this.bill = bill;
  }

  @Id
  @GeneratedValue
  @Column(nullable = false)
  private Long id;

  @Column(nullable = false)
  private BigDecimal cost;

  private String description;

  @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
  @JoinTable(name = "bill_item_tags",
      joinColumns = {@JoinColumn(name = "bill_item_id")},
      inverseJoinColumns = {@JoinColumn(name = "tag_name")})
  private Set<Tag> tags;

  @Column(nullable = false)
  private String expression;

  @ToString.Exclude
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "bill_id", nullable = false)
  private Bill bill;
}