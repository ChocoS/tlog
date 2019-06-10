package com.pwawrzyniak.tlog.server;

import com.pwawrzyniak.tlog.backend.dto.BillDto;
import com.pwawrzyniak.tlog.backend.dto.BillItemDto;
import com.pwawrzyniak.tlog.backend.dto.TagTotalsPerMonthDto;
import com.pwawrzyniak.tlog.backend.entity.User;
import com.pwawrzyniak.tlog.backend.repository.BillRepository;
import com.pwawrzyniak.tlog.backend.service.BillService;
import com.pwawrzyniak.tlog.backend.service.security.UserAwareUserDetails;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static com.pwawrzyniak.tlog.backend.service.DataInit.bill;
import static com.pwawrzyniak.tlog.backend.service.DataInit.billItem;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TlogApplicationTest {

  @Autowired
  private BillService billService;

  @Autowired
  private BillRepository billRepository;

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

  @Before
  public void setup() {
    billRepository.deleteAll();
  }

  @Test
  public void saveBillTest() {
    // given
    assertEquals(0, billService.countAllNotDeleted());
    BillDto billDto = bill(LocalDate.now(),
        billItem("150", "150", null, "education"),
        billItem("84.12", "79.99+4.13", "other", "maintenance"));

    // when
    billService.saveBill(billDto);

    // then
    assertEquals(1, billService.countAllNotDeleted());
  }

  @Test
  public void softDeleteBillTest() {
    // given
    billService.saveBill(bill(LocalDate.now(),
        billItem("150", "150", null, "education"),
        billItem("84.12", "79.99+4.13", "other", "maintenance")));
    Long id = billRepository.findAll().get(0).getId();
    BillDto billDto = BillDto.builder().id(id).build();

    // when
    billService.softDeleteBill(billDto);

    // then
    assertEquals(0, billService.countAllNotDeleted());
  }

  @Test
  public void editBillTest() {
    // given
    billService.saveBill(bill(LocalDate.now(),
        billItem("150", "150", null, "education"),
        billItem("84.12", "79.99+4.13", null, "other", "maintenance")));
    BillDto bill = billService.findAllNotDeletedBySearchString(0, 1, null).get(0);
    assertEquals(2, bill.getBillItems().size());
    BillItemDto billItemDto = bill.getBillItems().stream().filter(billItem -> "84.12".equals(billItem.getCost())).findFirst().get();
    assertEquals("79.99+4.13", billItemDto.getExpression());
    assertEquals(Set.of("other", "maintenance"), billItemDto.getTags());

    // when
    billItemDto.setCost("90.99");
    billItemDto.setExpression("90+0.99");
    billItemDto.setTags(Set.of("maintenance", "food"));
    bill.getBillItems().add(billItem("32", "32", "desc", "gift"));
    billService.saveBill(bill);

    // then
    bill = billService.findAllNotDeletedBySearchString(0, 1, null).get(0);
    assertEquals(3, bill.getBillItems().size());
    billItemDto = bill.getBillItems().stream().filter(billItem -> "90.99".equals(billItem.getCost())).findFirst().get();
    assertEquals("90+0.99", billItemDto.getExpression());
    assertEquals(Set.of("food", "maintenance"), billItemDto.getTags());
    billItemDto = bill.getBillItems().stream().filter(billItem -> "32.00".equals(billItem.getCost())).findFirst().get();
    assertEquals("32", billItemDto.getExpression());
    assertEquals("desc", billItemDto.getDescription());
    assertEquals(Set.of("gift"), billItemDto.getTags());
  }

  @Test
  public void getTagTotalsPerMonthTest() {
    // given
    LocalDate now = LocalDate.now().withDayOfMonth(1);
    billService.saveBill(bill(now.minusDays(70),
        billItem("12.34", "12+0.34", "mleko", "food"),
        billItem("20.99", "10+4.5+6.49", "chemia", "maintenance")));
    billService.saveBill(bill(now.minusDays(47),
        billItem("85", "85", "basen", "education", "piotr")));
    billService.saveBill(bill(now.minusDays(45),
        billItem("6.49", "6.49", null, "food")));
    billService.saveBill(bill(now.minusDays(42),
        billItem("122.99", "122.99", null, "other"),
        billItem("26.98", "26.98", null, "clothes"),
        billItem("7.08", "7.08", null, "maintenance"),
        billItem("132.17", "289.22-157.05", null, "food")));
    billService.saveBill(bill(now.minusDays(39),
        billItem("15.74", "15.74", null, "other"),
        billItem("99.99", "99.99", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi sit amet blandit" +
            " massa, egestas porta enim. Quisque eget odio vitae ante molestie mollis at quis nunc. Donec posuere," +
            " nibh at varius fringilla, est nunc fringilla turpis, sit", "clothes"),
        billItem("7.08", "7.08", null, "maintenance")));
    billService.saveBill(bill(now.minusDays(36),
        billItem("11.21", "11.21", null, "food")));
    billService.saveBill(bill(now.minusDays(23),
        billItem("10.99", "10.99", null, "food")));
    billService.saveBill(bill(now.minusDays(20),
        billItem("119.99", "119.99", null, "food")));

    // when
    List<TagTotalsPerMonthDto> tagTotalsPerMonthDtoList = billService.getTagTotalsPerMonthList(0, 50);

    // then
    assertEquals(4, tagTotalsPerMonthDtoList.size());
    assertEquals(now, tagTotalsPerMonthDtoList.get(0).getDate());
    assertEquals("0", tagTotalsPerMonthDtoList.get(0).getTagTotalMap().get("food"));
    assertEquals(now.minusMonths(1), tagTotalsPerMonthDtoList.get(1).getDate());
    assertEquals("130.98", tagTotalsPerMonthDtoList.get(1).getTagTotalMap().get("food"));
  }
}