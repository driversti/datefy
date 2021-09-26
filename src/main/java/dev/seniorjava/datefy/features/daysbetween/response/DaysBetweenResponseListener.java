package dev.seniorjava.datefy.features.daysbetween.response;

import static java.lang.String.format;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
import com.pengrad.telegrambot.request.SendMessage;
import dev.seniorjava.datefy.common.DatesCalculator;
import dev.seniorjava.datefy.features.errors.CaughtExceptionEvent;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DaysBetweenResponseListener {

  public static final String WRONG_DATES_PROVIDED = "You've provided wrong dates. Try again:";
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

  private final TelegramBot bot;
  private final DatesCalculator datesCalculator;
  private final ApplicationEventPublisher eventPublisher;

  @EventListener(DaysBetweenResponseEvent.class)
  public void handle(final DaysBetweenResponseEvent event) {
    SendMessage responseToUser;
    try {
      responseToUser = createMessageWithCalculatedDays(event);
    } catch (Exception ex) {
      log.error("Cannot parse dates.", ex);
      eventPublisher.publishEvent(new CaughtExceptionEvent(event.chatId(), ex));
      responseToUser = createTryAgainMessage(event.chatId());
    }

    bot.execute(responseToUser);
  }

  private SendMessage createMessageWithCalculatedDays(final DaysBetweenResponseEvent event) {
    return new SendMessage(event.chatId(), calculateDays(event.userResponse()))
        .replyMarkup(new ReplyKeyboardRemove());
  }

  private String calculateDays(final String userResponse) {
    final String[] dates = userResponse.split("\n");
    final LocalDate from = LocalDate.parse(dates[0], FORMATTER);
    final LocalDate to = LocalDate.parse(dates[1], FORMATTER);

    return new StringBuilder("There is:")
        .append("\n").append(days(from, to))
        .append("\nor ").append(seconds(from, to))
        .append("\nor ").append(yearsMonthsDays(from, to))
        .toString();
  }

  private String days(final LocalDate from, final LocalDate to) {
    final long days = Math.abs(datesCalculator.daysBetween(from, to));
    return format("%d days", days);
  }

  private String seconds(final LocalDate from, final LocalDate to) {
    final long seconds = Math.abs(datesCalculator.secondsBetween(from, to));
    return format("%d seconds", seconds);
  }

  private String yearsMonthsDays(final LocalDate from, final LocalDate to) {
    final Period period = datesCalculator.periodBetween(from, to);
    return format("%d years %d months %d days",
        Math.abs(period.getYears()), Math.abs(period.getMonths()), Math.abs(period.getDays()));
  }

  private SendMessage createTryAgainMessage(final Long chatId) {
    return new SendMessage(chatId, WRONG_DATES_PROVIDED)
        .replyMarkup(new ForceReply());
  }
}
