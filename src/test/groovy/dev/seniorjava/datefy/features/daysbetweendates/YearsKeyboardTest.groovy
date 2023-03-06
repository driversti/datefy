package dev.seniorjava.datefy.features.daysbetweendates

import dev.seniorjava.datefy.UnitTest
import spock.lang.Ignore

class YearsKeyboardTest extends UnitTest {

    @Ignore
    def "should return 3 rows with 3 year buttons in each with the given year in the middle"() {
        given:
        int givenYear = 2022

        when:
        def keyboard = new YearsKeyboard(5, 2022).build().inlineKeyboard()

        then:
        keyboard.length == 7
        keyboard[0].length == 2
        keyboard[1].length == 5
        keyboard[2][2].text() == givenYear.toString()
        keyboard[2].length == 3
    }
}
