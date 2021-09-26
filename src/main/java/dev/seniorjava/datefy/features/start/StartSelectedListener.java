package dev.seniorjava.datefy.features.start;

import static java.lang.String.format;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class StartSelectedListener {

  private final TelegramBot bot;

  @EventListener(StartSelectedEvent.class)
  public void handle(StartSelectedEvent event) {
    sayHello(event);
  }

  private void sayHello(StartSelectedEvent event) {
    final String message = format("Hi, %s.\nPress /features to continue",
        event.update().message().from().firstName());
    bot.execute(new SendMessage(event.chatId(), message));
  }
}
