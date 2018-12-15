package com.pwawrzyniak.tlog.backend.service;

import com.pwawrzyniak.tlog.backend.dto.BillDto;
import com.pwawrzyniak.tlog.backend.dto.BillItemDto;
import com.pwawrzyniak.tlog.backend.entity.Bill;
import com.pwawrzyniak.tlog.backend.entity.BillItem;
import com.pwawrzyniak.tlog.backend.entity.Tag;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
public class EntityToDtoConverter {

  public BillDto convertBill(Bill bill) {
    BillDto billDto = BillDto.builder()
        .id(bill.getId())
        .date(bill.getDate())
        .billItems(bill.getBillItems().stream().map(this::convertBillItem).collect(Collectors.toList()))
        .totalCost(bill.getBillItems().stream().map(BillItem::getCost).reduce(BigDecimal.ZERO, BigDecimal::add).toString())
        .createdAt(bill.getCreatedAt())
        .createdBy(bill.getCreatedBy())
        .lastModifiedAt(bill.getLastModifiedAt())
        .lastModifiedBy(bill.getLastModifiedBy())
        .build();
    recalculateLastModified(billDto, bill);
    return billDto;
  }

  private void recalculateLastModified(BillDto billDto, Bill bill) {
    bill.getBillItems().forEach(billItem -> {
      LocalDateTime lastModifiedAt = billDto.getLastModifiedAt();
      if (lastModifiedAt == null) {
        if (billItem.getLastModifiedAt() != null) {
          // bill not modified, bill item modified
          billDto.setLastModifiedAt(billItem.getLastModifiedAt());
          billDto.setLastModifiedBy(billItem.getLastModifiedBy());
        } else {
          // both not modified
          if (ChronoUnit.SECONDS.between(bill.getCreatedAt(), billItem.getCreatedAt()) > 0) {
            // bill item created later than at creation time of the bill
            billDto.setLastModifiedAt(billItem.getCreatedAt());
            billDto.setLastModifiedBy(billItem.getCreatedBy());
          }
        }
      } else {
        if (billItem.getLastModifiedAt() != null) {
          // both modified
          if (billItem.getLastModifiedAt().isAfter(lastModifiedAt)) {
            billDto.setLastModifiedAt(billItem.getLastModifiedAt());
            billDto.setLastModifiedBy(billItem.getLastModifiedBy());
          }
        } else {
          // bill modified, bill item not modified
          if (billItem.getCreatedAt().isAfter(lastModifiedAt)) {
            billDto.setLastModifiedAt(billItem.getCreatedAt());
            billDto.setLastModifiedBy(billItem.getCreatedBy());
          }
        }
      }
    });
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