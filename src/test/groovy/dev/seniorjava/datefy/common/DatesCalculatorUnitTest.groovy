package dev.seniorjava.datefy.common

import static java.time.LocalDate.of
import static java.time.Month.APRIL
import static java.time.Month.AUGUST
import static java.time.Month.FEBRUARY
import static java.time.Month.NOVEMBER
import static java.time.Month.OCTOBER
import static java.time.Month.SEPTEMBER

import java.time.Period
import spock.lang.Specification
import spock.lang.Subject

class DatesCalculatorUnitTest extends Specification {

    @Subject
    private final datesCalculator = new DatesCalculator()

    def "expect #expectedDays between #from and #to"() {
        expect:
        datesCalculator.daysBetween(from, to) == expectedDays

        where:
        from                    | to                      || expectedDays
        of(2021, SEPTEMBER, 25) | of(2021, SEPTEMBER, 25) || 0
        of(2021, SEPTEMBER, 20) | of(2021, SEPTEMBER, 25) || 5
        of(2021, SEPTEMBER, 25) | of(2021, SEPTEMBER, 20) || -5
        of(2021, APRIL, 12)     | of(2021, OCTOBER, 23)   || 194
        of(2021, NOVEMBER, 7)   | of(2021, AUGUST, 23)    || -76
    }

    def "expect #expectedSeconds between #from and #to"() {
        expect:
        datesCalculator.secondsBetween(from, to) == expectedSeconds

        where:
        from                    | to                      || expectedSeconds
        of(2021, SEPTEMBER, 25) | of(2021, SEPTEMBER, 25) || 0
        of(2021, SEPTEMBER, 25) | of(2021, SEPTEMBER, 26) || 86_400
        of(2021, SEPTEMBER, 25) | of(2021, SEPTEMBER, 24) || -86_400
        of(2021, SEPTEMBER, 25) | of(2022, SEPTEMBER, 25) || 31_536_000
    }

    def "expect #expectedPeriod between #from and #to"() {
        expect:
        datesCalculator.periodBetween(from, to) == expectedPeriod

        where:
        from                    | to                      || expectedPeriod
        of(2021, SEPTEMBER, 25) | of(2021, SEPTEMBER, 25) || Period.of(0, 0, 0)
        of(2021, SEPTEMBER, 5)  | of(2021, SEPTEMBER, 25) || Period.of(0, 0, 20)
        of(2021, SEPTEMBER, 5)  | of(2023, FEBRUARY, 17)  || Period.of(1, 5, 12)
        of(2003, SEPTEMBER, 18) | of(2021, AUGUST, 25)    || Period.of(17, 11, 7)
        of(2021, SEPTEMBER, 18) | of(2003, AUGUST, 25)    || Period.of(-18, 0, -24)
    }
}
