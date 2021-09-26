package dev.seniorjava.datefy.exceptions;

public class TooManyEventFactoriesFoundException extends RuntimeException {

  public TooManyEventFactoriesFoundException(String selector) {
    super(selector);
  }
}
