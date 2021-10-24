package dev.seniorjava.datefy.features.listall;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import dev.seniorjava.datefy.keyboard.InlineKeyboardBuilder;
import lombok.experimental.UtilityClass;

@UtilityClass
class AllFeaturesKeyboard {

  InlineKeyboardMarkup create() {
    return InlineKeyboardBuilder.builder()
        .row()
        .button("Days between dates", "/daysbetweendates")
        .endRow()
        .build();
  }
}
