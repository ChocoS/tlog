package com.pwawrzyniak.tlog.backend.validation;

public interface ValidationConstants {

  String COST_PATTERN = "\\d+(.\\d+)?";
  String EXPRESSION_PATTERN = "[0-9.+/*-/()]+";
  int MAX_EXPRESSION_SIZE = 255;
  int MAX_DESCRIPTION_SIZE = 255;
  int MAX_TAG_SIZE = 30;
}