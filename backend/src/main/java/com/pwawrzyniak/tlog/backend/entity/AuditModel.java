package com.pwawrzyniak.tlog.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class AuditModel {

  @Column(nullable = false, updatable = false)
  @CreatedDate
  protected LocalDateTime createdAt;

  @Column(nullable = false, updatable = false)
  @CreatedBy
  protected String createdBy;

  @Column
  @LastModifiedDate
  protected LocalDateTime lastModifiedAt;

  @Column
  @LastModifiedBy
  protected String lastModifiedBy;
}