package com.pwawrzyniak.tlog.backend.service;

import com.pwawrzyniak.tlog.backend.dto.BillDto;
import com.pwawrzyniak.tlog.backend.entity.Bill;
import com.pwawrzyniak.tlog.backend.entity.Tag;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;

import static com.pwawrzyniak.tlog.backend.service.DataInit.bill;
import static com.pwawrzyniak.tlog.backend.service.DataInit.billItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;
import static org.mockito.internal.util.reflection.FieldSetter.setField;

@RunWith(MockitoJUnitRunner.class)
public class DtoToEntityConverterTest {

  @Mock
  private TagService tagService;
  private DtoToEntityConverter dtoToEntityConverter = new DtoToEntityConverter();

  @Before
  public void setup() throws NoSuchFieldException {
    setField(dtoToEntityConverter, dtoToEntityConverter.getClass().getDeclaredField("tagService"), tagService);
    when(tagService.getOrCreateTagByName("food")).thenReturn(Tag.builder().name("food").build());
    when(tagService.getOrCreateTagByName("maintenance")).thenReturn(Tag.builder().name("maintenance").build());
  }

  @Test
  public void shouldProperlyConvertBillDtoToEntity() {
    // given
    BillDto billDto = bill(LocalDate.of(2018, 10, 15),
        billItem("12.34", "12+0.34", "mleko", "Food", "food"),
        billItem("20.99", "10+4.5+6.49", "chemia", "maintenance"));

    // when
    Bill bill = dtoToEntityConverter.convertBillDto(billDto);

    // then
    assertNull(bill.getId());
    assertEquals(LocalDate.of(2018, 10, 15), bill.getDate());
    assertNotNull(bill.getBillItems());
    assertEquals(2, bill.getBillItems().size());
    // first bill item
    assertNull(bill.getBillItems().get(0).getId());
    assertEquals("12.34", bill.getBillItems().get(0).getCost().toString());
    assertEquals("12+0.34", bill.getBillItems().get(0).getExpression());
    assertEquals("mleko", bill.getBillItems().get(0).getDescription());
    assertNotNull(bill.getBillItems().get(0).getTags());
    assertEquals(1, bill.getBillItems().get(0).getTags().size());
    assertEquals("food", bill.getBillItems().get(0).getTags().iterator().next().getName());
    // second bill item
    assertNull(bill.getBillItems().get(1).getId());
    assertEquals("20.99", bill.getBillItems().get(1).getCost().toString());
    assertEquals("10+4.5+6.49", bill.getBillItems().get(1).getExpression());
    assertEquals("chemia", bill.getBillItems().get(1).getDescription());
    assertNotNull(bill.getBillItems().get(1).getTags());
    assertEquals(1, bill.getBillItems().get(1).getTags().size());
    assertEquals("maintenance", bill.getBillItems().get(1).getTags().iterator().next().getName());
  }
}