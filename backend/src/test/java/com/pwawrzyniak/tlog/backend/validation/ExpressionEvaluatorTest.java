package com.pwawrzyniak.tlog.backend.validation;

import org.junit.Test;

import java.math.BigDecimal;

import static com.pwawrzyniak.tlog.backend.validation.ExpressionEvaluator.evaluate;
import static com.pwawrzyniak.tlog.backend.validation.ExpressionEvaluator.isValid;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ExpressionEvaluatorTest {

  @Test
  public void shouldProperlyEvaluateExpressions() {
    assertProperExpressionWithTheSameOutcome("-1");
    assertProperExpressionWithTheSameOutcome("0");
    assertProperExpressionWithTheSameOutcome("1");
    assertProperExpressionWithTheSameOutcome("-1.23");
    assertProperExpressionWithTheSameOutcome("-123.4567");
    assertProperExpressionWithTheSameOutcome("1321");
    assertProperExpressionWithTheSameOutcome("1321.12345633");

    assertProperExpressionAndOutcome("--1", "1");
    assertProperExpressionAndOutcome("-(-1)", "1");
    assertProperExpressionAndOutcome("((-(((-1)))))", "1");
    assertProperExpressionAndOutcome("1/2", "0.5");
    assertProperExpressionAndOutcome("4.5/3", "1.5");
    assertProperExpressionAndOutcome("23.", "23");
    assertProperExpressionAndOutcome(".456", "0.456");

    assertWrongExpression(null);
    assertWrongExpression("");
    assertWrongExpression("+1");
    assertWrongExpression("1++1");
    assertWrongExpression("123412..");
    assertWrongExpression("..12342");
    assertWrongExpression("123.1412.12");
    assertWrongExpression("1/3");
  }

  private void assertProperExpressionWithTheSameOutcome(String expression) {
    assertExpressionAndOutcome(expression, true, expression);
  }

  private void assertProperExpressionAndOutcome(String expression, String expectedOutcome) {
    assertExpressionAndOutcome(expression, true, expectedOutcome);
  }

  private void assertWrongExpression(String expression) {
    assertExpressionAndOutcome(expression, false, "0");
  }

  private void assertExpressionAndOutcome(String expression, boolean expectedValid, String expectedOutcome) {
    if (expectedValid) {
      assertTrue(isValid(expression));
    } else {
      assertFalse(isValid(expression));
    }
    assertEquals(new BigDecimal(expectedOutcome), evaluate(expression));
  }
}