package dev.seniorjava.datefy.features.errors;

import static java.lang.String.format;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
class CaughtExceptionListener {

  private final TelegramBot bot;
  private final Long developerId;

  CaughtExceptionListener(final TelegramBot bot,
      final @Value("${app.developer.id}") Long developerId) {
    this.bot = bot;
    this.developerId = developerId;
  }

  @EventListener(CaughtExceptionEvent.class)
  public void handle(CaughtExceptionEvent event) {
    notifyDeveloper(event);
    if (event.userChatId() != null) {
      notifyUser(event.userChatId());
    }
  }

  private void notifyDeveloper(final CaughtExceptionEvent event) {
    final String exceptionSimpleName = event.exception().getClass().getSimpleName();
    final String exceptionMessage = event.exception().getMessage();
    final String message = format("Got %s: %s", exceptionSimpleName, exceptionMessage);

    bot.execute(new SendMessage(developerId, message));
  }

  private void notifyUser(final Long userChatId) {
    final String message = """
        Ooops, you're a lucky man and caught a bug!
        But don't worry, I've notified the developer already.
        Press /features to continue""";
    bot.execute(new SendMessage(userChatId, message).replyMarkup(new ReplyKeyboardRemove()));
  }
}
