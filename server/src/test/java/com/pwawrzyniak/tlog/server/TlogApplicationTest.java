package com.pwawrzyniak.tlog.server;

import com.pwawrzyniak.tlog.backend.dto.BillDto;
import com.pwawrzyniak.tlog.backend.service.BillService;
import org.junit.Assert;
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

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TlogApplicationTest {

  @Autowired
  private BillService billService;

  @Test
  public void billServiceIntegrationTest() {
    // find all bills and check size
    List<BillDto> billDtoList = billService.findAllBills();
    Assert.assertEquals(8, billDtoList.size());

    // create new bill
    BillDto billDto = bill(LocalDate.of(2018, 10, 31),
        billItem("150", "150", null, "education", "Kinga"),
        billItem("84.12", "79.99+4.13", "chemia", "maintenance"));

    // save new bill
    billService.saveBill(billDto);

    // find all bills again and check new size
    billDtoList = billService.findAllBills();
    Assert.assertEquals(9, billDtoList.size());
  }
}