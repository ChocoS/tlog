package com.pwawrzyniak.tlog.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class BillItem {

    private BigDecimal cost;
    private String description;
    private List<Label> labelList;
    private String expression;
}