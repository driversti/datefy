package dev.seniorjava.datefy.features.daysbetweendates

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.CallbackQuery
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.request.ForceReply
import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove
import com.pengrad.telegrambot.request.SendMessage
import com.pengrad.telegrambot.response.SendResponse
import dev.seniorjava.datefy.UnitTest
import dev.seniorjava.datefy.common.ChatIdExtractor
import dev.seniorjava.datefy.common.DatesCalculator
import dev.seniorjava.datefy.common.PresenceChecker
import dev.seniorjava.datefy.features.Result
import java.time.LocalDate
import java.time.Period
import spock.lang.Subject

class DaysBetweenDatesFeatureUnitTest extends UnitTest {

    private static final String ENTER_DATES_MESSAGE = "Enter dates in the following format: 05.02.1996-24.12.2011"

    @Subject
    private DaysBetweenDatesFeature feature
    private TelegramBot bot
    private PresenceChecker presenceChecker
    private ChatIdExtractor chatIdExtractor
    private DatesCalculator datesCalculator

    void setup() {
        bot = Mock()
        presenceChecker = Mock()
        chatIdExtractor = Mock()
        datesCalculator = Mock()
        feature = new DaysBetweenDatesFeature(bot, presenceChecker, chatIdExtractor, datesCalculator)
    }

    def "should return SKIPPED if the given update cannot be handled"() {
        given:
        presenceChecker.isPresent(givenUpdate, _ as String) >> false

        expect:
        feature.handle(givenUpdate) == Result.SKIPPED

        where:
        givenUpdate                                                 || _
        null                                                        || _
        new Update()                                                || _
        new Update(message: new Message())                          || _
        new Update(message: new Message(text: "/foo"))              || _
        new Update(callback_query: new CallbackQuery())             || _
        new Update(callback_query: new CallbackQuery(data: "/foo")) || _
    }

    def "should ask a user to provide two dates when got the command"() {
        given:
        final Update givenUpdate = updateWithCommand()

        and:
        presenceChecker.isPresent(givenUpdate, _ as String) >> true
        chatIdExtractor.chatId(givenUpdate) >> 123L

        when:
        Result result = feature.handle(givenUpdate)

        then:
        result == Result.HANDLED
        bot.execute(_ as SendMessage) >> { SendMessage sm ->
            with(sm.parameters as Map<String, Object>) {
                chat_id == 123L
                text == DaysBetweenDatesFeatureUnitTest.ENTER_DATES_MESSAGE
                reply_markup.class == ForceReply
            }
            new SendResponse(result: new Message())
        }
    }

    def "should return calculated dates to the user"() {
        given:
        final Long chatId = 123L
        final Update update = updateWithDates()
        long days = 12
        long seconds = 39
        final Period period = Period.of(1, 2, 3)

        and:
        datesCalculator.daysBetween(_ as LocalDate, _ as LocalDate) >> days
        datesCalculator.secondsBetween(_ as LocalDate, _ as LocalDate) >> seconds
        datesCalculator.periodBetween(_ as LocalDate, _ as LocalDate) >> period
        chatIdExtractor.chatId(update) >> chatId

        when:
        feature.handle(update)

        then:
        1 * bot.execute(_ as SendMessage) >> { SendMessage sm ->
            with(sm.parameters as Map<String, Object>) {
                chat_id == chatId
                text == "There are: $days days, or $seconds seconds" +
                        ", or $period.years year $period.months months $period.days days"
                reply_markup.class == ReplyKeyboardRemove
            }
            new SendResponse(result: new Message())
        }
    }

    private static Update updateWithCommand() {
        new Update(message: new Message(text: "/daysbetweendates"))
    }

    private static Update updateWithDates() {
        new Update(message: new Message(reply_to_message: new Message(text: ENTER_DATES_MESSAGE),
        text: "01.01.1996-31.12.2003"))
    }
}
