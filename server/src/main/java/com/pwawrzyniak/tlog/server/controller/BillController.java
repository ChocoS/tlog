package com.pwawrzyniak.tlog.server.controller;

import com.pwawrzyniak.tlog.model.Bill;
import com.pwawrzyniak.tlog.model.BillItem;
import com.pwawrzyniak.tlog.model.Label;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.pwawrzyniak.tlog.server.controller.ControllerPaths.BILL;

@RestController(BILL)
public class BillController {

  @GetMapping
  List<Bill> getBill() {

    return getMockData();
  }

  private List<Bill> getMockData() {

    return Arrays.asList(
        Bill.builder()
            .date(LocalDate.of(2017, 5, 26))
            .billItems(Arrays.asList(
                BillItem.builder()
                    .cost(new BigDecimal("12.34"))
                    .description("sample description")
                    .expression("12+0.34")
                    .labelList(Collections.singletonList(
                        Label.builder().value("food").build()
                    )).build(),
                BillItem.builder()
                    .cost(new BigDecimal("0.54"))
                    .description("some stuff")
                    .expression("1-0.46")
                    .labelList(Arrays.asList(
                        Label.builder().value("education").build(),
                        Label.builder().value("swimming").build()
                    ))
                    .build()
            )).build(),
        Bill.builder()
            .date(LocalDate.of(2017, 5, 27))
            .billItems(Arrays.asList(
                BillItem.builder()
                    .cost(new BigDecimal("111.99"))
                    .description("jacket")
                    .expression("111.99")
                    .labelList(Collections.singletonList(
                        Label.builder().value("clothes").build()
                    )).build(),
                BillItem.builder()
                    .cost(new BigDecimal("5.99"))
                    .description("soap")
                    .expression("6-0.01")
                    .labelList(Collections.singletonList(
                        Label.builder().value("cosmetics").build()
                    ))
                    .build()
            )).build()
    );
  }
}