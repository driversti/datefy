package dev.seniorjava.datefy.features.daysbetween.request;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DaysBetweenListener {

  public static final String HEADER = "Enter the first date, press Enter and enter the second date. "
      + "Both in the following format: dd.MM.yyyy";

  private final TelegramBot bot;

  @EventListener(DaysBetweenSelectedEvent.class)
  public void handle(final DaysBetweenSelectedEvent event) {
    final SendMessage sendMessage = new SendMessage(event.chatId(), HEADER)
        .replyMarkup(new ForceReply());
    bot.execute(sendMessage);
  }
}
