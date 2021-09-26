package dev.seniorjava.datefy.features.listall;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import dev.seniorjava.datefy.features.daysbetween.request.DaysBetweenSelectedEventFactory;
import dev.seniorjava.datefy.keyboards.InlineKeyboardBuilder;

class FeaturesKeyboard {

  static InlineKeyboardMarkup create() {
    return InlineKeyboardBuilder.builder()
        .row().button("Days between two dates", DaysBetweenSelectedEventFactory.SELECTOR).endRow()
        .build();
  }
}
