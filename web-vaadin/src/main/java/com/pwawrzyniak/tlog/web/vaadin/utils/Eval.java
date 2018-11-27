package com.pwawrzyniak.tlog.web.vaadin.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class Eval {

  private static Logger log = LoggerFactory.getLogger(Eval.class);

  public static BigDecimal eval(final String str) {
    try {
      return new Object() {
        int pos = -1, ch;

        void nextChar() {
          ch = (++pos < str.length()) ? str.charAt(pos) : -1;
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
          log.info("Parsing '{}'", str);
          nextChar();
          BigDecimal x = parseExpression();
          if (pos < str.length()) throw new EvalException("Unexpected: " + (char) ch);
          return x;
        }

        BigDecimal parseExpression() {
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
          if (eat('+')) return parseFactor(); // unary plus
          if (eat('-')) return parseFactor().negate(); // unary minus

          BigDecimal x;
          int startPos = this.pos;
          if (eat('(')) { // parentheses
            x = parseExpression();
            eat(')');
          } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
            while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
            x = new BigDecimal(str.substring(startPos, this.pos));
          } else {
            throw new EvalException("Unexpected: " + (char) ch);
          }

          return x;
        }
      }.parse();
    } catch (EvalException exception) {
      return BigDecimal.ZERO;
    }
  }

  static class EvalException extends RuntimeException {
    public EvalException(String message) {
      super(message);
    }
  }
}