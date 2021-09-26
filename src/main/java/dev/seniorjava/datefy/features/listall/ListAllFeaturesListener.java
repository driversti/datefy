package dev.seniorjava.datefy.features.listall;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class ListAllFeaturesListener {

  private final TelegramBot bot;

  @EventListener(ListAllFeaturesEvent.class)
  public void handle(final ListAllFeaturesEvent event) {
    displayAllAvailableFeatures(event);
  }

  private void displayAllAvailableFeatures(final ListAllFeaturesEvent event) {
    final SendMessage sendMessage = new SendMessage(event.chatId(), "Select what you want to do:")
        .replyMarkup(FeaturesKeyboard.create());
    bot.execute(sendMessage);
  }
}
