package dev.seniorjava.datefy.features.daysbetween.response

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.request.ForceReply
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove
import com.pengrad.telegrambot.request.SendMessage
import dev.seniorjava.datefy.common.DatesCalculator
import dev.seniorjava.datefy.features.errors.CaughtExceptionEvent
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeParseException
import org.springframework.context.ApplicationEventPublisher
import spock.lang.Specification
import spock.lang.Subject

class DaysBetweenResponseListenerUnitTest extends Specification {

    private TelegramBot bot
    private DatesCalculator datesCalculator
    private ApplicationEventPublisher eventPublisher

    @Subject
    private listener

    void setup() {
        bot = Mock()
        datesCalculator = Mock()
        eventPublisher = Mock()
        listener = new DaysBetweenResponseListener(bot, datesCalculator, eventPublisher)
    }

    def "should ask the user to try again if their response #givenCase"() {
        given:
        def event = new DaysBetweenResponseEvent(123L, userResponse)

        when:
        listener.handle(event)

        then:
        1 * eventPublisher.publishEvent(_) >> { CaughtExceptionEvent exceptionEvent ->
            with(exceptionEvent) {
                userChatId() == 123L
                exception().class == expectedException
            }
        }
        1 * bot.execute(_) >> { SendMessage sendMessage ->
            with(sendMessage.parameters) {
                chat_id == 123L
                text == DaysBetweenResponseListener.WRONG_DATES_PROVIDED
                reply_markup.class == ForceReply
            }
        }

        where:
        userResponse || expectedException      | givenCase
        null         || NullPointerException   | "is null"
        "11.22.33"   || DateTimeParseException | "cannot be parsed"
    }

    def "should return the response with calculated dates"() {
        given:
        def event = new DaysBetweenResponseEvent(123L, "12.02.2021\n22.02.2021")

        and: "return mocked calculations"
        datesCalculator.daysBetween(_ as LocalDate, _ as LocalDate) >> 10
        datesCalculator.secondsBetween(_ as LocalDate, _ as LocalDate) >> 864_000
        datesCalculator.periodBetween(_ as LocalDate, _ as LocalDate) >> Period.of(0, 0, 10)

        when:
        listener.handle(event)

        then:
        1 * bot.execute(_) >> { SendMessage sendMessage ->
            with(sendMessage.parameters) {
                chat_id == 123L
                text == "There is:\n" +
                        "10 days\n" +
                        "or 864000 seconds\n" +
                        "or 0 years 0 months 10 days"
                reply_markup.class == ReplyKeyboardRemove
            }
        }
    }
}
