package dev.seniorjava.datefy.features.listall

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Chat
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.request.InlineKeyboardButton
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup
import com.pengrad.telegrambot.request.SendMessage
import dev.seniorjava.datefy.UnitTest
import dev.seniorjava.datefy.common.PresenceChecker
import dev.seniorjava.datefy.features.Result
import spock.lang.Subject

class ListAllFeatureUnitTest extends UnitTest {

    @Subject
    private ListAllFeature listAllFeature
    private TelegramBot bot
    private PresenceChecker presenceChecker

    void setup() {
        bot = Mock()
        presenceChecker = Mock()
        listAllFeature = new ListAllFeature(bot, presenceChecker)
    }

    def "should respond with a list of features in the form of buttons"() {
        given:
        def update = new Update(message: new Message(chat: new Chat(id: 123L), text: "/features"))

        and:
        presenceChecker.isPresent(update, _ as String) >> true

        when:
        def result = listAllFeature.handle(update)

        then:
        result == Result.HANDLED
        1 * bot.execute(_ as SendMessage) >> { SendMessage sm ->
            with(sm.parameters as Map<String, Object>) {
                chat_id == 123L
                text == "Choose what interests you:"
                (reply_markup as InlineKeyboardMarkup).inlineKeyboard() == expectedFeatureButtons()
            }
        }
    }

    def "should return SKIPPED if the given update cannot be handled"() {
        given:
        presenceChecker.isPresent(givenUpdate, _ as String) >> false

        expect:
        listAllFeature.handle(givenUpdate) == Result.SKIPPED

        where:
        givenUpdate                                    || _
        null                                           || _
        new Update()                                   || _
        new Update(message: new Message())             || _
        new Update(message: new Message(text: "/foo")) || _
    }

    private static InlineKeyboardButton[][] expectedFeatureButtons() {
        [[new InlineKeyboardButton("Days between dates").callbackData("/daysbetweendates")]]
    }
}
