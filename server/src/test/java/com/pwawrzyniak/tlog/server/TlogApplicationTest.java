package com.pwawrzyniak.tlog.server;

import com.pwawrzyniak.tlog.backend.dto.BillDto;
import com.pwawrzyniak.tlog.backend.service.BillService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

import static com.pwawrzyniak.tlog.backend.service.DataInit.bill;
import static com.pwawrzyniak.tlog.backend.service.DataInit.billItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TlogApplicationTest {

  @Autowired
  private BillService billService;

  @Test
  public void billServiceIntegrationTest() {
    LocalDate now = LocalDate.now();

    // find all bills and check size
    List<BillDto> billDtoList = billService.findAllBills();
    assertEquals(8, billDtoList.size());
    assertTrue(billDtoList.get(0).getDate().isBefore(now));

    // create new bill
    BillDto billDto = bill(LocalDate.now(),
        billItem("150", "150", null, "education", "Kinga"),
        billItem("84.12", "79.99+4.13", "chemia", "maintenance"));

    // save new bill
    billService.saveBill(billDto);

    // find all bills again and check new size
    billDtoList = billService.findAllBills();
    assertEquals(9, billDtoList.size());
    assertTrue(billDtoList.get(0).getDate().equals(now));
  }
}