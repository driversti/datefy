package dev.seniorjava.datefy.features.start;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove;
import com.pengrad.telegrambot.request.SendMessage;
import dev.seniorjava.datefy.common.PresenceChecker;
import dev.seniorjava.datefy.features.Feature;
import dev.seniorjava.datefy.features.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class StartFeature implements Feature {

  private static final String COMMAND = "/start";

  private final TelegramBot bot;
  private final PresenceChecker commandPresenceChecker;

  @Override
  public Result handle(final Update update) {
    if (!commandPresenceChecker.isPresent(update, COMMAND)) {
      return Result.SKIPPED;
    }
    final Long chatId = update.message().chat().id();
    bot.execute(new SendMessage(chatId, "Click on /features to find out what I can do")
        .replyMarkup(new ReplyKeyboardRemove()));
    return Result.HANDLED;
  }
}
