package com.pwawrzyniak.tlog.backend.service;

import com.pwawrzyniak.tlog.backend.dto.BillDto;
import com.pwawrzyniak.tlog.backend.entity.Bill;
import com.pwawrzyniak.tlog.backend.repository.BillRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.validation.Validation;
import java.time.LocalDate;

import static com.pwawrzyniak.tlog.backend.service.DataInit.bill;
import static com.pwawrzyniak.tlog.backend.service.DataInit.billItem;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.internal.util.reflection.FieldSetter.setField;

@RunWith(MockitoJUnitRunner.class)
public class BillServiceTest {

  @Mock
  private BillRepository billRepository;
  @Mock
  private DtoToEntityConverter dtoToEntityConverter;
  private BillService billService = new BillService();

  @Before
  public void setup() throws NoSuchFieldException {
    setField(billService, billService.getClass().getDeclaredField("billRepository"), billRepository);
    setField(billService, billService.getClass().getDeclaredField("validator"), Validation.buildDefaultValidatorFactory().getValidator());
    setField(billService, billService.getClass().getDeclaredField("dtoToEntityConverter"), dtoToEntityConverter);
    Bill bill = Bill.builder().id(1L).build();
    when(billRepository.save(Mockito.any())).thenReturn(bill);
    when(dtoToEntityConverter.convertBillDto(Mockito.any(BillDto.class))).thenReturn(bill);
  }

  @Test
  public void shouldSaveValidBillDto() {
    // given
    BillDto billDto = bill(LocalDate.of(2018, 10, 15),
        billItem("12.34", "12+0.34", "mleko", "Food"),
        billItem("20.99", "10+4.5+6.49", "chemia", "maintenance"));

    // when
    billService.saveBill(billDto);

    // then
    Mockito.verify(billRepository, times(1)).save(Mockito.any(Bill.class));
  }

  @Test
  public void shouldNotSaveInvalidBillDto() {
    // given
    BillDto billDto = bill(LocalDate.of(2018, 10, 15),
        billItem("invalid", "12+0.34", "mleko", "Food"),
        billItem("20.99", "10+4.5+6.49", "chemia", "maintenance"));

    // when
    billService.saveBill(billDto);

    // then
    verifyZeroInteractions(billRepository);
  }
}