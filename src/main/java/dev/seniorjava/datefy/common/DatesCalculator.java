package dev.seniorjava.datefy.common;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.SECONDS;

import java.time.LocalDate;
import java.time.Period;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DatesCalculator {

  public long daysBetween(final LocalDate from, final LocalDate to) {
    return DAYS.between(from, to);
  }

  public long secondsBetween(final LocalDate from, final LocalDate to) {
    return SECONDS.between(from.atStartOfDay(), to.atStartOfDay());
  }

  public Period periodBetween(final LocalDate from, final LocalDate to) {
    return from.until(to);
  }
}
