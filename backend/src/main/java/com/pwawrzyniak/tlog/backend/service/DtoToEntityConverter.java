package com.pwawrzyniak.tlog.backend.service;

import com.pwawrzyniak.tlog.backend.dto.BillDto;
import com.pwawrzyniak.tlog.backend.dto.BillItemDto;
import com.pwawrzyniak.tlog.backend.entity.Bill;
import com.pwawrzyniak.tlog.backend.entity.BillItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
public class DtoToEntityConverter {

  @Autowired
  private TagService tagService;

  public Bill convertBillDto(BillDto billDto) {
    Bill bill = Bill.builder()
        .date(billDto.getDate())
        .billItems(billDto.getBillItems().stream().map(this::convertBillItemDto).collect(Collectors.toList()))
        .build();
    bill.getBillItems().forEach(billItem -> billItem.setBill(bill));
    return bill;
  }

  private BillItem convertBillItemDto(BillItemDto billItemDto) {
    return BillItem.builder()
        .cost(new BigDecimal(billItemDto.getCost()))
        .description(billItemDto.getDescription())
        .tags(billItemDto.getTags().stream().map(String::toLowerCase).distinct().map(tagService::getOrCreateTagByName).collect(Collectors.toSet()))
        .expression(billItemDto.getExpression())
        .build();
  }
}