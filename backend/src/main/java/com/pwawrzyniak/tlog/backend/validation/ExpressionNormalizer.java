package com.pwawrzyniak.tlog.backend.validation;

import java.util.regex.Pattern;

public class ExpressionNormalizer {

  public static boolean isNormalized(String value) {
    if (value == null) {
      return true;
    }
    return !Pattern.compile("\\s+").matcher(value).find() && !value.contains(",");
  }

  public static String normalize(String value) {
    if (value == null) {
      return null;
    }
    return value.replaceAll("\\s+", "").replaceAll(",", ".");
  }
}