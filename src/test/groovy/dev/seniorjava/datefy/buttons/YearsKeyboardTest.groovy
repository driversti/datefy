package dev.seniorjava.datefy.buttons

import dev.seniorjava.datefy.UnitTest
import dev.seniorjava.datefy.features.test.YearsKeyboard

class YearsKeyboardTest extends UnitTest {

    def "should return 3 rows with 3 year buttons in each with the given year in the middle"() {
        given:
        int givenYear = 2022

        when:
        def keyboard = YearsKeyboard.create(2022).inlineKeyboard()

        then:
        keyboard.length == 3
        keyboard[0].length == 3
        keyboard[1].length == 3
        keyboard[1][1].text() == givenYear.toString()
        keyboard[2].length == 3
    }
}
