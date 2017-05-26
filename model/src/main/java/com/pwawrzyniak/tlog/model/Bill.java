package com.pwawrzyniak.tlog.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class Bill {

    private LocalDate date;
    private List<BillItem> billItems;
}