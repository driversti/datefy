package dev.seniorjava.datefy.features.daysbetweendates;

import java.time.LocalDate;
import java.time.Month;
import org.apache.commons.lang3.tuple.Pair;

public sealed interface Storage permits InMemoryStorage {

  void init(final Long chatId);

  void addYear(final Long chatId, final int year);
  int getFirstYear(final Long chatId);
  boolean hasFirstYear(Long chatId);
  int getSecondYear(final Long chatId);

  void addMonth(final Long chatId, final Month month);
  Month getFirstMonth(final Long chatId);
  boolean hasFirstMonth(final Long chatId);
  Month getSecondMonth(final Long chatId);

  void addDay(final Long chatId, final int day);
  int getFirstDay(final Long chatId);
  boolean hasFirstDay(Long chatId);
  int getSecondDay(final Long chatId);

  void destroy(Long chatId);

  Pair<LocalDate, LocalDate> getPair(Long chatId);
}
