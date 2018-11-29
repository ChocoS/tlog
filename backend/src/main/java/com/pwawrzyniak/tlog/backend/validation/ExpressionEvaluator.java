package com.pwawrzyniak.tlog.backend.validation;

import java.math.BigDecimal;

public class ExpressionEvaluator {

  public static boolean isValid(String expression) {
    try {
      parse(expression);
      return true;
    } catch (RuntimeException exception) {
      return false;
    }
  }

  public static BigDecimal evaluate(final String expression) {
    try {
      return parse(expression);
    } catch (RuntimeException exception) {
      return BigDecimal.ZERO;
    }
  }

  private static BigDecimal parse(String expression) {
    return new Object() {
      int pos = -1, ch;

      void nextChar() {
        ch = (++pos < expression.length()) ? expression.charAt(pos) : -1;
      }

      boolean eat(int charToEat) {
        while (ch == ' ') nextChar();
        if (ch == charToEat) {
          nextChar();
          return true;
        }
        return false;
      }

      BigDecimal parse() {
        nextChar();
        BigDecimal x = parseExpression();
        if (pos < expression.length()) throw new RuntimeException("Unexpected: " + (char) ch);
        return x;
      }

      BigDecimal parseExpression() {
        if (eat('-')) return parseExpression().negate(); // unary minus

        BigDecimal x = parseTerm();
        for (; ; ) {
          if (eat('+')) x = x.add(parseTerm()); // addition
          else if (eat('-')) x = x.subtract(parseTerm()); // subtraction
          else return x;
        }
      }

      BigDecimal parseTerm() {
        BigDecimal x = parseFactor();
        for (; ; ) {
          if (eat('*')) x = x.multiply(parseFactor()); // multiplication
          else if (eat('/')) x = x.divide(parseFactor()); // division
          else return x;
        }
      }

      BigDecimal parseFactor() {
        BigDecimal x;
        int startPos = this.pos;
        if (eat('(')) { // parentheses
          x = parseExpression();
          eat(')');
        } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
          boolean decimalPointAlreadyFound = false;
          while ((ch >= '0' && ch <= '9') || ch == '.') {
            if (ch == '.') {
              if (decimalPointAlreadyFound) {
                throw new RuntimeException("Unexpected: number contains more than one decimal point");
              }
              decimalPointAlreadyFound = true;
            }
            nextChar();
          }
          x = new BigDecimal(expression.substring(startPos, this.pos));
        } else {
          throw new RuntimeException("Unexpected: " + (char) ch);
        }

        return x;
      }
    }.parse();
  }
}