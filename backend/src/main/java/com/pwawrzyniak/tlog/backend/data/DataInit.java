package com.pwawrzyniak.tlog.backend.data;

import com.pwawrzyniak.tlog.backend.model.BillItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;

@Service
public class DataInit implements ApplicationRunner {

  private static Logger log = LoggerFactory.getLogger(DataInit.class);

  @Autowired
  private BillItemRepository billItemRepository;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    long count = billItemRepository.count();
    if (count == 0) {
      log.info("Database empty. Creating test data...");
      BillItem billItem1 = BillItem.builder()
          .cost(1234L)
          .description("sample description")
          .expression("12+0.34")
          .tags(Collections.singletonList("food")).build();
      BillItem billItem2 = BillItem.builder()
          .cost(54L)
          .description("some stuff")
          .expression("1-0.46")
          .tags(Arrays.asList("education", "swimming"))
          .build();

      billItemRepository.save(billItem1);
      billItemRepository.save(billItem2);
    } else {
      log.info("Database contains data (count: {}). No test data created.", count);
    }
  }
}