package dev.seniorjava.datefy.features.daysbetweendates;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import dev.seniorjava.datefy.keyboard.InlineKeyboardBuilder;
import java.time.Month;
import java.time.YearMonth;

class DaysKeyboard {

  private final int year;
  private final Month month;
  private final int columns;
  private final InlineKeyboardBuilder builder;

  DaysKeyboard(final int year, final Month month, final int columns) {
    this.year = year;
    this.month = month;
    this.columns = columns;
    this.builder = InlineKeyboardBuilder.builder();
  }

  InlineKeyboardMarkup build() {
    addBody();
    addFooter();
    return builder.build();
  }

  private void addBody() {
    int buttonsInRow = 0;
    final int daysInMonth = YearMonth.of(year, month).lengthOfMonth();
    for (int day = 1; day <= daysInMonth; day++) {
      if (builder.isRowEnded()) {
        builder.row();
      }
      builder.button(day, "daysbetweendates_day_" + day);
      buttonsInRow += 1;
      if (buttonsInRow == columns) {
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
