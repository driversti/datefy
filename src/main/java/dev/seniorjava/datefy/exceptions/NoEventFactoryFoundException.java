package dev.seniorjava.datefy.exceptions;

public class NoEventFactoryFoundException extends RuntimeException {

  public NoEventFactoryFoundException(String selector) {
    super(selector);
  }
}
