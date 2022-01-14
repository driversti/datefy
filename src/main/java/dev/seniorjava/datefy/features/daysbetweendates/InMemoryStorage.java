package dev.seniorjava.datefy.features.daysbetweendates;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

@Slf4j
@Component
final class InMemoryStorage implements Storage {

  private static final Map<Long, Pair<State, State>> states = new HashMap<>();

  @Override
  public void init(final Long chatId) {
    states.put(chatId, Pair.of(new State(), new State()));
  }

  @Override
  public void addYear(final Long chatId, final int year) {
    if (states.get(chatId).getLeft().year == null) {
      states.get(chatId).getLeft().year = year;
      log.debug("Added first year {} for {}", year, chatId);
    } else {
      states.get(chatId).getRight().year = year;
      log.debug("Added second year {} for {}", year, chatId);
    }
  }

  @Override
  public int getFirstYear(final Long chatId) {
    return states.get(chatId).getLeft().year;
  }

  @Override
  public boolean hasFirstYear(final Long chatId) {
    return states.get(chatId).getLeft().hasYear();
  }

  @Override
  public int getSecondYear(final Long chatId) {
    return states.get(chatId).getRight().year;
  }

  @Override
  public void addMonth(final Long chatId, final Month month) {
    if (states.get(chatId).getLeft().month == null) {
      states.get(chatId).getLeft().month = month;
      log.debug("Added first month {} for {}", month, chatId);
    } else {
      states.get(chatId).getRight().month = month;
      log.debug("Added second month {} for {}", month, chatId);
    }
  }

  @Override
  public Month getFirstMonth(final Long chatId) {
    return states.get(chatId).getLeft().month;
  }

  @Override
  public boolean hasFirstMonth(final Long chatId) {
    return states.get(chatId).getLeft().hasMonth();
  }

  @Override
  public Month getSecondMonth(final Long chatId) {
    return states.get(chatId).getRight().month;
  }

  @Override
  public void addDay(final Long chatId, final int day) {
    if (states.get(chatId).getLeft().day == null) {
      states.get(chatId).getLeft().day = day;
      log.debug("Added first day {} for {}", day, chatId);
    } else {
      states.get(chatId).getRight().day = day;
      log.debug("Added second day {} for {}", day, chatId);
    }
  }

  @Override
  public int getFirstDay(final Long chatId) {
    return states.get(chatId).getLeft().day;
  }

  @Override
  public boolean hasFirstDay(final Long chatId) {
    return states.get(chatId).getLeft().hasDay();
  }

  @Override
  public int getSecondDay(final Long chatId) {
    return states.get(chatId).getRight().day;
  }

  @Override
  public void destroy(final Long chatId) {
    states.remove(chatId);
    log.debug("State removed for {}", chatId);
  }

  @Override
  public Pair<LocalDate, LocalDate> getPair(final Long chatId) {
    final State left = states.get(chatId).getLeft();
    final State right = states.get(chatId).getRight();
    return Pair.of(
        LocalDate.of(left.year, left.month, left.day),
        LocalDate.of(right.year, right.month, right.day)
    );
  }

  private static class State {

    Integer year;
    Month month;
    Integer day;

    boolean hasYear() {
      return year != null;
    }

    boolean hasMonth() {
      return month != null;
    }

    boolean hasDay() {
      return day != null;
    }
  }
}
