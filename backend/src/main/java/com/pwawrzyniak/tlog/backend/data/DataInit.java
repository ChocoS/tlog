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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static java.time.LocalDate.now;

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

      Tag foodTag = tagRepository.save(Tag.builder().name("food").build());
      Tag maintenanceTag = tagRepository.save(Tag.builder().name("maintenance").build());
      Tag educationTag = tagRepository.save(Tag.builder().name("education").build());
      Tag piotrTag = tagRepository.save(Tag.builder().name("piotr").build());
      Tag otherTag = tagRepository.save(Tag.builder().name("other").build());
      Tag clothesTag = tagRepository.save(Tag.builder().name("clothes").build());

      List<Bill> bills = new ArrayList<>();
      bills.add(bill(now().minusDays(50),
          billItem("12.34", "12+0.34", "mleko", foodTag),
          billItem("20.99", "10+4.5+6.49", "chemia", maintenanceTag)));
      bills.add(bill(now().minusDays(47),
          billItem("85", "85", "basen", educationTag, piotrTag)));
      bills.add(bill(now().minusDays(45),
          billItem("6.49", "6.49", foodTag)));
      bills.add(bill(now().minusDays(42),
          billItem("122.99", "122.99", otherTag),
          billItem("26.98", "26.98", clothesTag),
          billItem("7.08", "7.08", maintenanceTag),
          billItem("132.17", "289.22-157.05", foodTag)));
      bills.add(bill(now().minusDays(39),
          billItem("15.74", "15.74", otherTag),
          billItem("99.99", "99.99", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi sit amet blandit" +
              " massa, egestas porta enim. Quisque eget odio vitae ante molestie mollis at quis nunc. Donec posuere," +
              " nibh at varius fringilla, est nunc fringilla turpis, sit", clothesTag),
          billItem("7.08", "7.08", maintenanceTag)));
      bills.add(bill(now().minusDays(36),
          billItem("11.21", "11.21", foodTag)));
      bills.add(bill(now().minusDays(33),
          billItem("10.99", "10.99", foodTag)));
      bills.add(bill(now().minusDays(29),
          billItem("119.99", "119.99", clothesTag)));

      bills.forEach(bill -> {
        bill.getBillItems().forEach(billItem -> billItem.setBill(bill));
        billRepository.save(bill);
      });
    } else {
      log.info("Database contains data (count: {}). No test data created.", count);
    }
  }

  private BillItem billItem(String cost, String expression, String description, Tag... tags) {
    return BillItem.builder()
        .cost(new BigDecimal(cost))
        .expression(expression)
        .description(description)
        .tags(Set.of(tags))
        .build();
  }

  private BillItem billItem(String cost, String expression, Tag... tags) {
    return billItem(cost, expression, null, tags);
  }

  private Bill bill(LocalDate localDate, BillItem... billItems) {
    return Bill.builder()
        .date(localDate)
        .billItems(Arrays.asList(billItems))
        .build();
  }
}