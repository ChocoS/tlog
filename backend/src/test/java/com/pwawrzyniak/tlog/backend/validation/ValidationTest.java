package com.pwawrzyniak.tlog.backend.validation;

import com.pwawrzyniak.tlog.backend.dto.BillItemDto;
import org.junit.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static com.pwawrzyniak.tlog.backend.service.DataInit.bill;
import static com.pwawrzyniak.tlog.backend.service.DataInit.billItem;
import static java.time.LocalDate.now;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ValidationTest {

  private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
  private Validator validator = factory.getValidator();

  @Test
  public void shouldProperlyValidateBills() {
    assertValid(bill(now().minusDays(50),
        billItem("12.34", "12+0.34", "mleko", "food"),
        billItem("20.99", "10+4.5+6.49", "chemia", "maintenance")));
    assertValid(bill(now(),
        billItem("12.34", "12+0.34", "mleko", "food")));

    // no date
    assertNotValid(bill(null,
        billItem("12.34", "12+0.34", "mleko", "food")));
    // no bill items
    assertNotValid(bill(now()));
    // date in future
    assertNotValid(bill(now().plusDays(1),
        billItem("12.34", "12+0.34", "mleko", "food")));
    // bill item not valid
    assertNotValid(bill(now().minusDays(50),
        billItem("12.34", "12+0.34xxx", "mleko", "food"),
        billItem("20.99", "10+4.5+6.49", "chemia", "maintenance")));
  }

  @Test
  public void shouldProperlyValidateBillItems() {
    assertValid(billItem("85", "85", "basen", "education", "piotr"));
    assertValid(billItem("85", "80.01+4.99", null, "education"));

    // invalid expression
    assertNotValid(billItem("85", "invalid expression", "basen", "education"));
    // too long expression
    assertNotValid(billItem("85", "1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+" +
        "1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+" +
        "1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+" +
        "1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+" +
        "1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1+1", "basen", "education"));
    // invalid cost
    assertNotValid(billItem("cost", "85", "basen", "education"));
    // description too long
    assertNotValid(billItem("85", "85", "basenbasenbasenbasenbasenbasenbasenbasenbasenbasen" +
        "basenbasenbasenbasenbasenbasenbasenbasenbasenbasen" +
        "basenbasenbasenbasenbasenbasenbasenbasenbasenbasen" +
        "basenbasenbasenbasenbasenbasenbasenbasenbasenbasen" +
        "basenbasenbasenbasenbasenbasenbasenbasenbasenbasenbasen1", "education"));
    // tags empty
    assertNotValid(billItem("85", "85", "basen"));
    // too long tag
    assertNotValid(billItem("85", "85", "basen", "thisTagIsToooooooooooooooooLong"));
  }

  private void assertNotValid(Object object) {
    assertFalse(validator.validate(object).isEmpty());
  }

  private void assertValid(Object object) {
    assertTrue(validator.validate(object).isEmpty());
  }
}