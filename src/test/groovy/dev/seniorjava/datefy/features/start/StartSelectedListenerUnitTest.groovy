package dev.seniorjava.datefy.features.start

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.User
import com.pengrad.telegrambot.request.SendMessage
import spock.lang.Specification
import spock.lang.Subject

class StartSelectedListenerUnitTest extends Specification {

    private TelegramBot bot

    @Subject
    private StartSelectedListener listener

    void setup() {
        bot = Mock()
        listener = new StartSelectedListener(bot)
    }

    def "should say hello to a user"() {
        given: "an event"
        def chatId = 12L
        def event = new StartSelectedEvent(chatId, new Update(message: new Message(from: new User(first_name: "fooName"))))

        and: "a SendMessage with expected parameters"
        def expectedSendMessage = new SendMessage(chatId, "Hi, fooName.\nPress /features to continue")

        when:
        listener.handle(event)

        then:
        1 * bot.execute(_) >> { SendMessage sendMessage ->
            assert sendMessage.parameters == expectedSendMessage.parameters
        }
    }
}
