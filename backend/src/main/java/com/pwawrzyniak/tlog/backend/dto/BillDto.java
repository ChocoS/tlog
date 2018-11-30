package com.pwawrzyniak.tlog.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.pwawrzyniak.tlog.backend.validation.ValidationConstants.COST_PATTERN;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillDto {

  private Long id;
  @NotNull
  @PastOrPresent
  private LocalDate date;
  @Valid
  @NotEmpty
  private List<BillItemDto> billItems;
  @Pattern(regexp = COST_PATTERN)
  private String totalCost;
  private LocalDateTime createdAt;
  private String createdBy;
  private LocalDateTime lastModifiedAt;
  private String lastModifiedBy;
}