package com.pwawrzyniak.tlog.backend.validation;

import org.junit.Test;

import static com.pwawrzyniak.tlog.backend.validation.ExpressionNormalizer.isNormalized;
import static com.pwawrzyniak.tlog.backend.validation.ExpressionNormalizer.normalize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ExpressionNormalizerTest {

  @Test
  public void shouldProperlyNormalizeExpressions() {
    assertNormalizedExpression("1");
    assertNormalizedExpression("1+5");
    assertNormalizedExpression("2*(3+6)");
    assertNormalizedExpression("-(-(-1))");
    assertNormalizedExpression("1.23");
    assertNormalizedExpression(".1234.5.124.24.2135.");
    assertNormalizedExpression("");
    assertNormalizedExpression(null);

    assertExpressionNormalization("1 ", "1");
    assertExpressionNormalization("12 + 5 * (5 - 2) / 4", "12+5*(5-2)/4");
    assertExpressionNormalization("   \t  \r\n 1,5 ", "1.5");
  }

  private void assertNormalizedExpression(String expression) {
    assertExpressionAndOutcome(expression, true, expression);
  }

  private void assertExpressionNormalization(String expression, String expectedOutcome) {
    assertExpressionAndOutcome(expression, false, expectedOutcome);
  }

  private void assertExpressionAndOutcome(String expression, boolean expectedValid, String expectedOutcome) {
    if (expectedValid) {
      assertTrue(isNormalized(expression));
    } else {
      assertFalse(isNormalized(expression));
    }
    assertEquals(expectedOutcome, normalize(expression));
  }
}