package dev.seniorjava.datefy.features.start

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Chat
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.request.ReplyKeyboardRemove
import com.pengrad.telegrambot.request.SendMessage
import dev.seniorjava.datefy.UnitTest
import dev.seniorjava.datefy.common.PresenceChecker
import dev.seniorjava.datefy.features.Result
import spock.lang.Subject

class StartFeatureUnitTest extends UnitTest {

    @Subject
    private StartFeature startFeature

    private TelegramBot bot
    private PresenceChecker presenceChecker

    void setup() {
        bot = Mock()
        presenceChecker = Mock()
        startFeature = new StartFeature(bot, presenceChecker)
    }

    def "should offer to a user all available features"() {
        given:
        def update = new Update(message: new Message(chat: new Chat(id: 123L), text: "/start"))

        and:
        presenceChecker.isPresent(update, _ as String) >> true

        when:
        Result result = startFeature.handle(update)

        then:
        result == Result.HANDLED
        1 * bot.execute(_ as SendMessage) >> { SendMessage sm ->
            with(sm.parameters as Map<String, Object>) {
                chat_id == 123L
                text == "Click on /features to find out what I can do"
                reply_markup.class == ReplyKeyboardRemove
            }
        }
    }

    def "should return SKIPPED if the given update cannot be handled"() {
        given:
        presenceChecker.isPresent(givenUpdate, _ as String) >> false

        expect:
        startFeature.handle(givenUpdate) == Result.SKIPPED

        where:
        givenUpdate                                    || _
        null                                           || _
        new Update()                                   || _
        new Update(message: new Message())             || _
        new Update(message: new Message(text: "/foo")) || _
    }
}
