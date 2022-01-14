package dev.seniorjava.datefy.features.daysbetweendates;

import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import dev.seniorjava.datefy.keyboard.InlineKeyboardBuilder;

class YearsKeyboard {

  private final int COLUMNS;
  private final int OFFSET;
  private final int YEAR;
  private final InlineKeyboardBuilder builder;

  YearsKeyboard(final int columns, final int year) {
    this.COLUMNS = columns;
    this.OFFSET = calculateOffset(columns);
    this.YEAR = year;
    this.builder = InlineKeyboardBuilder.builder();
  }

  private int calculateOffset(final int columns) {
    return (columns * columns - 1) / 2;
  }

  InlineKeyboardMarkup build() {
    addHeader();
    addBody();
    addFooter();
    return builder.build();
  }

  private void addHeader() {
    final int YEAR_LEFT = YEAR - 2 * OFFSET - 1;
    final int YEAR_RIGHT = YEAR + 2 * OFFSET + 1;
    builder.row()
        .button("◀", "daysbetweendates_shift_year_left_" + YEAR_LEFT)
        .button("▶︎", "daysbetweendates_shift_year_right_" + YEAR_RIGHT)
        .endRow();
  }

  private void addBody() {
    int buttonsInRow = 0;
    for (int offset = -OFFSET; offset <= OFFSET; offset++) {
      if (builder.isRowEnded()) {
        builder.row();
      }
      final int currentYear = YEAR + offset;
      builder.button(currentYear, "daysbetweendates_year_" + currentYear);
      buttonsInRow = buttonsInRow + 1;
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
