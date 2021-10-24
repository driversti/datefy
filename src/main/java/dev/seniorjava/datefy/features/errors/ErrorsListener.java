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
class ErrorsListener {

  private final TelegramBot bot;
  private final Long developerId;

  ErrorsListener(final TelegramBot bot,
      final @Value("${app.developer.id}") Long developerId) {
    this.bot = bot;
    this.developerId = developerId;
  }

  @EventListener(CaughtExceptionEvent.class)
  public void handle(final CaughtExceptionEvent event) {
    notifyDeveloper(event);
    // todo implement
//    if (event.userChatId() != null) {
//      notifyUser(event.userChatId());
//    }
  }

  @EventListener(UpdateNotHandledEvent.class)
  public void handle(final UpdateNotHandledEvent event) {
    log.warn("Update was not handled!");
    final String message = "An event was not handled. Check logs.";
    bot.execute(new SendMessage(developerId, message).replyMarkup(new ReplyKeyboardRemove()));
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
