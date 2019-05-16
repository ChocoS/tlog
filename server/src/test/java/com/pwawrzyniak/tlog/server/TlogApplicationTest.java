package com.pwawrzyniak.tlog.server;

import com.pwawrzyniak.tlog.backend.dto.BillDto;
import com.pwawrzyniak.tlog.backend.entity.User;
import com.pwawrzyniak.tlog.backend.service.BillService;
import com.pwawrzyniak.tlog.backend.service.security.UserAwareUserDetails;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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

  Logger log = LoggerFactory.getLogger(TlogApplicationTest.class);

  @Autowired
  private BillService billService;

  @BeforeClass
  public static void setupClass() {
    Authentication authentication = Mockito.mock(Authentication.class);
    User user = new User();
    user.setUsername("user");
    Mockito.when(authentication.getPrincipal()).thenReturn(new UserAwareUserDetails(user));
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
  }

  @Test
  public void billServiceIntegrationTest() {
    LocalDate now = LocalDate.now();

    log.info("find all bills and check size");
    List<BillDto> billDtoList = billService.findNotDeletedBillsFirstPage();
    assertEquals(8, billDtoList.size());
    assertTrue(billDtoList.get(0).getDate().isBefore(now));

    log.info("create new bill");
    BillDto billDto = bill(LocalDate.now(),
        billItem("150", "150", null, "education", "Kinga"),
        billItem("84.12", "79.99+4.13", "chemia", "maintenance"));

    log.info("save new bill");
    billService.saveBill(billDto);

    log.info("find all bills again and check new size");
    billDtoList = billService.findNotDeletedBillsFirstPage();
    assertEquals(9, billDtoList.size());
    assertTrue(billDtoList.get(0).getDate().equals(now));

    log.info("check who created bills");
    billDtoList.forEach(bill -> assertEquals("user", bill.getCreatedBy()));

    log.info("soft delete two bills");
    billService.softDeleteBill(billDtoList.get(0));
    billService.softDeleteBill(billDtoList.get(1));

    log.info("find all bills again and check new size");
    billDtoList = billService.findNotDeletedBillsFirstPage();
    assertEquals(7, billDtoList.size());

    log.info("edit bill");
    BillDto billToBeEdited = billDtoList.get(billDtoList.size() - 1);
    Long editedBillId = billToBeEdited.getId();
    int currentNumberOfBillItems = billToBeEdited.getBillItems().size();
    billToBeEdited.getBillItems().add(billItem("1", "1", "added", "added tag"));
    billService.saveBill(billToBeEdited);
    billDtoList = billService.findNotDeletedBillsFirstPage();
    BillDto editedBill = billDtoList.get(billDtoList.size() - 1);
    assertEquals(editedBillId, editedBill.getId());
    assertEquals(currentNumberOfBillItems + 1, editedBill.getBillItems().size());
  }
}