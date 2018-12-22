package com.pwawrzyniak.tlog.backend.service;

import com.pwawrzyniak.tlog.backend.dto.BillDto;
import com.pwawrzyniak.tlog.backend.dto.BillItemDto;
import com.pwawrzyniak.tlog.backend.entity.Privilege;
import com.pwawrzyniak.tlog.backend.service.security.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;

import static java.time.LocalDate.now;

// TODO mark with test profile only, after it is no longer needed
@Service
public class DataInit implements ApplicationRunner {

  private static Logger log = LoggerFactory.getLogger(DataInit.class);

  @Autowired
  private BillService billService;

  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    initializeUsers();
    initializeBills();
  }

  private void initializeUsers() {
    Privilege readPrivilege = userDetailsService.createPrivilegeIfNotFound("READ_PRIVILEGE");
    Privilege writePrivilege = userDetailsService.createPrivilegeIfNotFound("WRITE_PRIVILEGE");

    userDetailsService.createRoleIfNotFound("ROLE_ADMIN", Arrays.asList(readPrivilege, writePrivilege));
    userDetailsService.createRoleIfNotFound("ROLE_USER", Arrays.asList(readPrivilege));

    userDetailsService.registerNewUserAccount("admin", "admin", "adminName", "adminLastName", "ROLE_ADMIN");
    userDetailsService.registerNewUserAccount("chocos", "123qwe", "chocos", "", "ROLE_USER");
  }

  private void initializeBills() {
    long count = billService.findAllNotDeletedBills().size();
    if (count == 0) {
      log.info("Database empty. Creating test repository...");

      billService.saveBill(bill(now().minusDays(50),
          billItem("12.34", "12+0.34", "mleko", "food"),
          billItem("20.99", "10+4.5+6.49", "chemia", "maintenance")));
      billService.saveBill(bill(now().minusDays(47),
          billItem("85", "85", "basen", "education", "piotr")));
      billService.saveBill(bill(now().minusDays(45),
          billItem("6.49", "6.49", null, "food")));
      billService.saveBill(bill(now().minusDays(42),
          billItem("122.99", "122.99", null, "other"),
          billItem("26.98", "26.98", null, "clothes"),
          billItem("7.08", "7.08", null, "maintenance"),
          billItem("132.17", "289.22-157.05", null, "food")));
      billService.saveBill(bill(now().minusDays(39),
          billItem("15.74", "15.74", null, "other"),
          billItem("99.99", "99.99", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi sit amet blandit" +
              " massa, egestas porta enim. Quisque eget odio vitae ante molestie mollis at quis nunc. Donec posuere," +
              " nibh at varius fringilla, est nunc fringilla turpis, sit", "clothes"),
          billItem("7.08", "7.08", null, "maintenance")));
      billService.saveBill(bill(now().minusDays(36),
          billItem("11.21", "11.21", null, "food")));
      billService.saveBill(bill(now().minusDays(33),
          billItem("10.99", "10.99", null, "food")));
      billService.saveBill(bill(now().minusDays(29),
          billItem("119.99", "119.99", null, "clothes")));

    } else {
      log.info("Database contains repository (count: {}). No test repository created.", count);
    }
  }

  public static BillItemDto billItem(String cost, String expression, String description, String... tags) {
    return BillItemDto.builder()
        .cost(cost)
        .expression(expression)
        .description(description)
        .tags(Set.of(tags))
        .build();
  }

  public static BillDto bill(LocalDate localDate, BillItemDto... billItems) {
    return BillDto.builder()
        .date(localDate)
        .billItems(Arrays.asList(billItems))
        .build();
  }
}