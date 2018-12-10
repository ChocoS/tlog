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
import java.util.Optional;

import static com.pwawrzyniak.tlog.backend.service.DataInit.bill;
import static com.pwawrzyniak.tlog.backend.service.DataInit.billItem;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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

  @Test
  public void shouldSoftDeleteExistingBill() {
    // given
    Long billId = 1L;
    Bill bill = Bill.builder().deleted(false).build();
    when(billRepository.findById(billId)).thenReturn(Optional.of(bill));

    // when
    boolean result = billService.softDeleteBill(BillDto.builder().id(billId).build());

    // then
    Mockito.verify(billRepository, times(1)).findById(billId);
    assertTrue(result);
    assertTrue(bill.isDeleted());
  }

  @Test
  public void shouldNotSoftDeleteNonExistingBill() {
    // given
    Long billId = 1L;
    when(billRepository.findById(billId)).thenReturn(Optional.empty());

    // when
    boolean result = billService.softDeleteBill(BillDto.builder().id(billId).build());

    // then
    Mockito.verify(billRepository, times(1)).findById(billId);
    assertFalse(result);
  }

  @Test
  public void shouldNotSoftDeleteIfIdIsNull() {
    // when
    boolean result = billService.softDeleteBill(new BillDto());

    // then
    verifyZeroInteractions(billRepository);
    assertFalse(result);
  }
}