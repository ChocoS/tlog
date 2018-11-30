package com.pwawrzyniak.tlog.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Bill extends AuditModel {

  @Builder
  public Bill(LocalDateTime createdAt, String createdBy, LocalDateTime lastModifiedAt, String lastModifiedBy, Long id, LocalDate date, List<BillItem> billItems) {
    super(createdAt, createdBy, lastModifiedAt, lastModifiedBy);
    this.id = id;
    this.date = date;
    this.billItems = billItems;
  }

  @Id
  @GeneratedValue
  @Column(nullable = false)
  private Long id;

  @Column(nullable = false)
  private LocalDate date;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "bill")
  private List<BillItem> billItems;
}