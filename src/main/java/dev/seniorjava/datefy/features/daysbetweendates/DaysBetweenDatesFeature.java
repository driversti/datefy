package dev.seniorjava.datefy.features.daysbetweendates;

import static java.lang.String.format;
import static java.util.Calendar.YEAR;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import dev.seniorjava.datefy.common.ChatIdExtractor;
import dev.seniorjava.datefy.common.DatesCalculator;
import dev.seniorjava.datefy.common.PresenceChecker;
import dev.seniorjava.datefy.features.Feature;
import dev.seniorjava.datefy.features.Result;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DaysBetweenDatesFeature implements Feature {

  private static final String COMMAND = "/daysbetweendates";
  private static final String PREFIX = "daysbetweendates_";
  private static final String PREFIX_YEAR = "daysbetweendates_year_";
  private static final String PREFIX_MONTH = "daysbetweendates_month_";
  private static final String PREFIX_DAY = "daysbetweendates_day_";
  private static final String PREFIX_SHIFT_YEAR_LEFT = "daysbetweendates_shift_year_left_";
  private static final String PREFIX_SHIFT_YEAR_RIGHT = "daysbetweendates_shift_year_right_";
  private static final String CANCEL = "daysbetweendates_cancel";
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

  private final TelegramBot telegramBot;
  private final PresenceChecker presenceChecker;
  private final ChatIdExtractor chatIdExtractor;
  private final Storage storage;
  private final DatesCalculator datesCalculator;

  @Override
  public Result handle(final Update update) {
    final Long chatId = chatIdExtractor.chatId(update);
    if (containsCommand(update)) {
      log.debug("Received {} command.", COMMAND);
      storage.init(chatId);
      displayYears(chatId, Calendar.getInstance().get(YEAR));
      return Result.HANDLED;
    }
    if (!presenceChecker.startsWith(update, PREFIX)) {
      return Result.SKIPPED;
    }
    final Integer messageId = update.callbackQuery().message().messageId();
    if (shouldShiftYearsLeft(update)) {
      log.info("Shift year left.");
      shiftYears(chatId, messageId, update, PREFIX_SHIFT_YEAR_LEFT);
      return Result.HANDLED;
    }
    if (shouldShiftYearsRight(update)) {
      log.debug("Shift year right.");
      shiftYears(chatId, messageId, update, PREFIX_SHIFT_YEAR_RIGHT);
      return Result.HANDLED;
    }
    if (containsYear(update)) {
      displayMonths(chatId, messageId, update);
      return Result.HANDLED;
    }
    if (containsMonth(update)) {
      displayDays(chatId, messageId, update);
      return Result.HANDLED;
    }
    if (containsDay(update)) {
      final int day = Integer.parseInt(
          update.callbackQuery().data().substring(PREFIX_DAY.length()));
      if (!storage.hasFirstDay(chatId)) {
        log.debug("First date has no day");
        storage.addDay(chatId, day);
        displayYears(chatId, messageId, Calendar.getInstance().get(YEAR));
      } else {
        log.debug("Both dates are collected");
        storage.addDay(chatId, day);
        calculateDatesDifference(chatId, messageId);
      }
      return Result.HANDLED;
    }
    if (shouldBeCanceled(update)) {
      cancelProcess(chatId, messageId, update);
      return Result.HANDLED;
    }
    return Result.SKIPPED;
  }

  private int getYear(final Update update, final String prefix) {
    final String asString = update.callbackQuery().data().substring(prefix.length());
    return Integer.parseInt(asString);
  }

  private boolean containsCommand(final Update update) {
    return presenceChecker.isPresent(update, COMMAND);
  }

  private void displayYears(final Long chatId, int year) {
    final String yearOrder = storage.hasFirstYear(chatId) ? "second" : "first";
    final String message = format("Choose %s date. Year:", yearOrder);
    final InlineKeyboardMarkup keyboardMarkup = new YearsKeyboard(5, year).build();
    telegramBot.execute(new SendMessage(chatId, message)
        .replyMarkup(keyboardMarkup));
  }

  private void displayYears(final Long chatId, final Integer messageId, int year) {
    final String yearOrder = storage.hasFirstYear(chatId) ? "second" : "first";
    final String message = format("Choose %s date. Year:", yearOrder);
    final InlineKeyboardMarkup yearsKeyboard = new YearsKeyboard(5, year).build();
    telegramBot.execute(new EditMessageText(chatId, messageId, message)
        .replyMarkup(yearsKeyboard));
  }

  private void shiftYears(final Long chatId, int messageId, final Update update,
      final String prefix) {
    final int year = getYear(update, prefix);
    displayYears(chatId, messageId, year);
  }

  private boolean containsYear(final Update update) {
    return presenceChecker.startsWith(update, PREFIX_YEAR);
  }

  private boolean shouldShiftYearsLeft(final Update update) {
    return presenceChecker.startsWith(update, PREFIX_SHIFT_YEAR_LEFT);
  }

  private boolean shouldShiftYearsRight(final Update update) {
    return presenceChecker.startsWith(update, PREFIX_SHIFT_YEAR_RIGHT);
  }

  private void displayMonths(final Long chatId, final Integer messageId, final Update update) {
    final String year = update.callbackQuery().data().substring(PREFIX_YEAR.length());
    log.info("Chosen year: {}", year);
    storage.addYear(chatId, Integer.parseInt(year));
    final String yearOrder = storage.hasFirstMonth(chatId) ? "second" : "first";
    final String message = format("Choose %s date. Month:", yearOrder);
    telegramBot.execute(new EditMessageText(chatId, messageId, message)
        .replyMarkup(new MonthsKeyboard(3).build()));
  }

  private boolean containsMonth(final Update update) {
    return presenceChecker.startsWith(update, PREFIX_MONTH);
  }

  private void displayDays(final Long chatId, final Integer messageId, final Update update) {
    final Month month = Month.valueOf(
        update.callbackQuery().data().substring(PREFIX_MONTH.length()));
    log.info("Chosen month: {}", month);
    storage.addMonth(chatId, month);
    final InlineKeyboardMarkup daysKeyboard = new DaysKeyboard(
        storage.getFirstYear(chatId), storage.getFirstMonth(chatId), 7)
        .build();
    final String yearOrder = storage.hasFirstDay(chatId) ? "second" : "first";
    final String message = format("Choose %s date. Day:", yearOrder);
    telegramBot.execute(new EditMessageText(chatId, messageId, message)
        .replyMarkup(daysKeyboard));
  }

  private boolean containsDay(final Update update) {
    return presenceChecker.startsWith(update, PREFIX_DAY);
  }

  private void calculateDatesDifference(final Long chatId, final Integer messageId) {
    Pair<LocalDate, LocalDate> dates = storage.getPair(chatId);
    final String resultMessage = createResultMessage(dates.getLeft(), dates.getRight());
    telegramBot.execute(new EditMessageText(chatId, messageId, resultMessage));
  }

  private boolean shouldBeCanceled(final Update update) {
    return presenceChecker.isPresent(update, CANCEL);
  }

  private void cancelProcess(final Long chatId, final Integer messageId, final Update update) {
    storage.destroy(chatId);
    telegramBot.execute(new EditMessageText(chatId, messageId, "Ok."));
    log.debug("The process canceled");
  }

  private String createResultMessage(final LocalDate left, final LocalDate right) {
    return left.toString() + " - " + right.toString()
        + "\nThere are:"
        + " " + days(left, right)
        + ", or " + seconds(left, right)
        + ", or " + yearsMonthsDays(left, right)
        + ".\nToday is not included!";
  }

  private String days(final LocalDate from, final LocalDate to) {
    final long days = Math.abs(datesCalculator.daysBetween(from, to));
    return format("%d %s", days, addPluralOrSingularEnd("day", days));
  }

  private String seconds(final LocalDate from, final LocalDate to) {
    final long seconds = Math.abs(datesCalculator.secondsBetween(from, to));
    return format("%d %s", seconds, addPluralOrSingularEnd("second", seconds));
  }

  private String yearsMonthsDays(final LocalDate from, final LocalDate to) {
    final Period period = datesCalculator.periodBetween(from, to);
    final int years = Math.abs(period.getYears());
    final int months = Math.abs(period.getMonths());
    final int days = Math.abs(period.getDays());
    return format("%d %s %d %s %d %s",
        years, addPluralOrSingularEnd("year", years),
        months, addPluralOrSingularEnd("month", months),
        days, addPluralOrSingularEnd("day", days));
  }

  private static String addPluralOrSingularEnd(final String period, long value) {
    if (value == 1) {
      return period;
    }
    return period + "s";
  }
}
