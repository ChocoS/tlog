package com.pwawrzyniak.tlog.server.controller;

import com.pwawrzyniak.tlog.model.Bill;
import com.pwawrzyniak.tlog.model.BillItem;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
                    .cost(1234L)
                    .description("sample description")
                    .expression("12+0.34")
                    .tags(Collections.singletonList("food")).build(),
                BillItem.builder()
                    .cost(54L)
                    .description("some stuff")
                    .expression("1-0.46")
                    .tags(Arrays.asList("education", "swimming"))
                    .build()
            )).build(),
        Bill.builder()
            .date(LocalDate.of(2017, 5, 27))
            .billItems(Arrays.asList(
                BillItem.builder()
                    .cost(11199L)
                    .description("jacket")
                    .expression("111.99")
                    .tags(Collections.singletonList("clothes")).build(),
                BillItem.builder()
                    .cost(599L)
                    .description("soap")
                    .expression("6-0.01")
                    .tags(Collections.singletonList("cosmetics"))
                    .build()
            )).build()
    );
  }
}