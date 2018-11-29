package com.pwawrzyniak.tlog.backend.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ExpressionValidator implements ConstraintValidator<ExpressionValid, String> {

  @Override
  public boolean isValid(String expression, ConstraintValidatorContext context) {
    return ExpressionNormalizer.isNormalized(expression) && ExpressionEvaluator.isValid(expression);
  }
}