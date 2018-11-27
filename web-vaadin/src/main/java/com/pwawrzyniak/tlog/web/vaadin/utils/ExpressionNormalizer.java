package com.pwawrzyniak.tlog.web.vaadin.utils;

import java.util.regex.Pattern;

public class ExpressionNormalizer {

  public static boolean isNormalized(String value) {
    return !Pattern.compile("\\s+").matcher(value).find() && !value.contains(",");
  }

  public static String normalize(String value) {
    return value.replaceAll("\\s+", "").replaceAll(",", ".");
  }
}