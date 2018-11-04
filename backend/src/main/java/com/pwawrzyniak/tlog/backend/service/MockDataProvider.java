package com.pwawrzyniak.tlog.backend.service;

import com.pwawrzyniak.tlog.backend.model.Bill;
import com.pwawrzyniak.tlog.backend.model.BillItem;
import com.pwawrzyniak.tlog.backend.model.Tag;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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
                    .tags(Set.of(Tag.builder().name("food").build()))
                    .build(),
                BillItem.builder()
                    .cost(54L)
                    .description("some stuff")
                    .expression("1-0.46")
                    .tags(Set.of(Tag.builder().name("education").build()))
                    .build()
            )).build(),
        Bill.builder()
            .date(LocalDate.of(2017, 5, 27))
            .billItems(Arrays.asList(
                BillItem.builder()
                    .cost(11199L)
                    .description("jacket")
                    .expression("111.99")
                    .tags(Set.of(Tag.builder().name("clothes").build())).build(),
                BillItem.builder()
                    .cost(599L)
                    .description("soap")
                    .expression("6-0.01")
                    .tags(Set.of(Tag.builder().name("cosmetics").build()))
                    .build()
            )).build()
    );
  }
}