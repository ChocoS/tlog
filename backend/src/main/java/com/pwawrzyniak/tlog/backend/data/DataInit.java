package com.pwawrzyniak.tlog.backend.data;

import com.pwawrzyniak.tlog.backend.model.Bill;
import com.pwawrzyniak.tlog.backend.model.BillItem;
import com.pwawrzyniak.tlog.backend.model.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;

@Service
public class DataInit implements ApplicationRunner {

  private static Logger log = LoggerFactory.getLogger(DataInit.class);

  @Autowired
  private BillRepository billRepository;

  @Autowired
  private TagRepository tagRepository;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    long count = billRepository.count();
    if (count == 0) {
      log.info("Database empty. Creating test data...");

      Tag foodTag = Tag.builder().name("food").build();
      Tag maintenanceTag = Tag.builder().name("maintenance").build();
      Tag educationTag = Tag.builder().name("education").build();
      Tag piotrTag = Tag.builder().name("piotr").build();

      foodTag = tagRepository.save(foodTag);
      maintenanceTag = tagRepository.save(maintenanceTag);
      educationTag = tagRepository.save(educationTag);
      piotrTag = tagRepository.save(piotrTag);

      Bill bill1 = Bill.builder()
          .date(LocalDate.of(2018, 10, 15))
          .billItems(Arrays.asList(
              BillItem.builder()
                  .cost(1234L)
                  .description("mleko")
                  .expression("12+0.34")
                  .tags(Set.of(foodTag))
                  .build(),
              BillItem.builder()
                  .cost(2099L)
                  .description("chemia")
                  .expression("10+4.5+6.49")
                  .tags(Set.of(maintenanceTag))
                  .build()
          )).build();

      Bill bill2 = Bill.builder()
          .date(LocalDate.of(2018, 10, 23))
          .billItems(Arrays.asList(
              BillItem.builder()
                  .cost(8500L)
                  .description("basen")
                  .expression("85")
                  .tags(Set.of(educationTag, piotrTag))
                  .build()
          )).build();

      Bill bill3 = Bill.builder()
          .date(LocalDate.of(2018, 11, 2))
          .billItems(Arrays.asList(
              BillItem.builder()
                  .cost(649L)
                  .expression("6.49")
                  .tags(Set.of(foodTag))
                  .build()
          )).build();

      Arrays.asList(bill1, bill2, bill3).forEach(bill -> bill.getBillItems().forEach(billItem -> billItem.setBill(bill)));

      billRepository.save(bill1);
      billRepository.save(bill2);
      billRepository.save(bill3);
    } else {
      log.info("Database contains data (count: {}). No test data created.", count);
    }
  }
}