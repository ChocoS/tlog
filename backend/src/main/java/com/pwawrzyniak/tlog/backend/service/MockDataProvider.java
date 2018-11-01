package com.pwawrzyniak.tlog.backend.service;

import com.pwawrzyniak.tlog.backend.model.Bill;
import com.pwawrzyniak.tlog.backend.model.BillItem;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class MockDataProvider {

  public List<Bill> getMockData() {
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