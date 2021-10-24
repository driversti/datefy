package dev.seniorjava.datefy.features.daysbetweendates;

import static java.lang.String.format;
import static java.util.Objects.isNull;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import dev.seniorjava.datefy.common.ChatIdExtractor;
import dev.seniorjava.datefy.common.DatesCalculator;
import dev.seniorjava.datefy.common.PresenceChecker;
import dev.seniorjava.datefy.features.Feature;
import dev.seniorjava.datefy.features.Result;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class DaysBetweenDatesFeature implements Feature {

  private static final String COMMAND = "/daysbetweendates";
  private static final String ENTER_DATES_MESSAGE = "Enter dates in the following format: 05.02.1996-24.12.2011";
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

  private final TelegramBot bot;
  private final PresenceChecker presenceChecker;
  private final ChatIdExtractor chatIdExtractor;
  private final DatesCalculator datesCalculator;

  @Override
  public Result handle(final Update update) {
    if (containsCommand(update)) {
      log.debug("Got the command: {}", COMMAND);
      handleCommand(update);
      return Result.HANDLED;
    }
    if (waitsForAnswer(update)) {
      log.debug("User provided dates");
      handleAnswer(update);
      return Result.HANDLED;
    }
    return Result.SKIPPED;

  }

  private boolean containsCommand(final Update update) {
    return presenceChecker.isPresent(update, COMMAND);
  }

  private void handleCommand(final Update update) {
    final Long chatId = chatIdExtractor.chatId(update);
    final SendMessage sendMessage = new SendMessage(chatId, ENTER_DATES_MESSAGE)
        .replyMarkup(new ForceReply());
    final SendResponse response = bot.execute(sendMessage);
    log.info("{}", response.message()); // fixme can be null. Handle it
  }

  private boolean waitsForAnswer(final Update update) {
    if (isNull(update) || isNull(update.message()) || isNull(update.message().replyToMessage())) {
      return false;
    }
    final String question = update.message().replyToMessage().text();
    return ENTER_DATES_MESSAGE.equals(question);
  }

  private void handleAnswer(final Update update) {
    final String responseContent = parseUsersDatesToBotsResponse(update);
    final Long chatId = chatIdExtractor.chatId(update);
    final SendMessage sendMessage = new SendMessage(chatId, responseContent)
        .replyMarkup(new ReplyKeyboardRemove());
    final SendResponse response = bot.execute(sendMessage);
    log.info("{}", response.message());
  }

  private String parseUsersDatesToBotsResponse(final Update update) {
    final String answer = update.message().text();
    final String[] dates = answer.split("-");
    final LocalDate from = LocalDate.parse(dates[0], FORMATTER);
    final LocalDate to = LocalDate.parse(dates[1], FORMATTER);

    return "There are:"
        + " " + days(from, to)
        + ", or " + seconds(from, to)
        + ", or " + yearsMonthsDays(from, to);
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
