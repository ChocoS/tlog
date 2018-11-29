package com.pwawrzyniak.tlog.backend.dto;

import com.pwawrzyniak.tlog.backend.validation.ExpressionValid;
import com.pwawrzyniak.tlog.backend.validation.ValidationConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;
import java.util.TreeSet;

import static com.pwawrzyniak.tlog.backend.validation.ValidationConstants.COST_PATTERN;
import static com.pwawrzyniak.tlog.backend.validation.ValidationConstants.MAX_DESCRIPTION_SIZE;
import static com.pwawrzyniak.tlog.backend.validation.ValidationConstants.MAX_TAG_SIZE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillItemDto {

  @Pattern(regexp = COST_PATTERN)
  private String cost;
  @Size(max = MAX_DESCRIPTION_SIZE)
  private String description;
  @NotEmpty
  private Set<@Size(max = MAX_TAG_SIZE) String> tags;
  @Size(max = MAX_DESCRIPTION_SIZE)
  @ExpressionValid
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