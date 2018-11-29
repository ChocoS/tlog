package com.pwawrzyniak.tlog.backend.service;

import com.pwawrzyniak.tlog.backend.dto.BillDto;
import com.pwawrzyniak.tlog.backend.entity.Bill;
import com.pwawrzyniak.tlog.backend.entity.BillItem;
import com.pwawrzyniak.tlog.backend.entity.Tag;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class EntityToDtoConverterTest {

  private EntityToDtoConverter entityToDtoConverter = new EntityToDtoConverter();

  @Test
  public void shouldProperlyConvertBillEntityToDto() {
    // given
    Bill bill = givenSampleBill();

    // when
    BillDto billDto = entityToDtoConverter.convertBill(bill);

    // then
    assertTrue(12 == billDto.getId());
    assertEquals(LocalDate.of(2018, 10, 22), billDto.getDate());
    assertEquals("135.33", billDto.getTotalCost());
    assertNotNull(billDto.getBillItems());
    assertEquals(2, billDto.getBillItems().size());
    // first bill item
    assertEquals("9.99+2.35", billDto.getBillItems().get(0).getExpression());
    assertEquals("12.34 (maintenance) chemia", billDto.getBillItems().get(0).display());
    // second bill item
    assertEquals("122.99", billDto.getBillItems().get(1).getExpression());
    assertEquals("122.99 (clothes, piotr)", billDto.getBillItems().get(1).display());
  }

  private Bill givenSampleBill() {
    Bill bill = Bill.builder()
        .id(12L)
        .date(LocalDate.of(2018, 10, 22))
        .billItems(Arrays.asList(
            BillItem.builder()
                .id(20L)
                .cost(new BigDecimal("12.34"))
                .description("chemia")
                .tags(Set.of(Tag.builder().name("maintenance").build()))
                .expression("9.99+2.35")
                .build(),
            BillItem.builder()
                .id(22L)
                .cost(new BigDecimal("122.99"))
                .tags(Set.of(Tag.builder().name("piotr").build(), Tag.builder().name("clothes").build()))
                .expression("122.99")
                .build()
        ))
        .build();
    bill.getBillItems().forEach(billItem -> billItem.setBill(bill));
    return bill;
  }
}