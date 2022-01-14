package dev.seniorjava.datefy.features.daysbetweendates;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import dev.seniorjava.datefy.keyboard.InlineKeyboardBuilder;
import java.time.Month;

class MonthsKeyboard {

  private final int COLUMNS;
  private final InlineKeyboardBuilder builder;

  MonthsKeyboard(final int columns) {
    this.COLUMNS = columns;
    this.builder = InlineKeyboardBuilder.builder();
  }

  InlineKeyboardMarkup build() {
    addBody();
    addFooter();
    return builder.build();
  }

  private void addBody() {
    int buttonsInRow = 0;
    for (Month month : Month.values()) {
      if (builder.isRowEnded()) {
        builder.row();
      }
      builder.button(month.name(), "daysbetweendates_month_" + month.name());
      buttonsInRow += 1;
      if (buttonsInRow == COLUMNS) {
        builder.endRow();
        buttonsInRow = 0;
      }
    }
    if (!builder.isRowEnded()) {
      builder.endRow();
    }
  }

  private void addFooter() {
    builder.row().button("Cancel", "daysbetweendates_cancel").endRow();
  }
}
