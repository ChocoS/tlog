package com.pwawrzyniak.tlog.backend.service;

import com.pwawrzyniak.tlog.backend.dto.BillDto;
import com.pwawrzyniak.tlog.backend.dto.BillItemDto;
import com.pwawrzyniak.tlog.backend.entity.Bill;
import com.pwawrzyniak.tlog.backend.entity.BillItem;
import com.pwawrzyniak.tlog.backend.entity.Tag;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
public class EntityToDtoConverter {

  public BillDto convertBill(Bill bill) {
    return BillDto.builder()
        .id(bill.getId())
        .date(bill.getDate())
        .billItems(bill.getBillItems().stream().map(this::convertBillItem).collect(Collectors.toList()))
        .cost(bill.getBillItems().stream().map(BillItem::getCost).reduce(BigDecimal.ZERO, BigDecimal::add).toString())
        .build();
  }

  private BillItemDto convertBillItem(BillItem billItem) {
    return BillItemDto.builder()
        .cost(billItem.getCost().toString())
        .description(billItem.getDescription())
        .expression(billItem.getExpression())
        .tags(billItem.getTags().stream().map(Tag::getName).collect(Collectors.toSet()))
        .build();
  }
}