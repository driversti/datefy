package dev.seniorjava.datefy.features.daysbetween.request

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.request.ForceReply
import com.pengrad.telegrambot.request.SendMessage
import spock.lang.Specification
import spock.lang.Subject

class DaysBetweenListenerUnitTest extends Specification {

    private TelegramBot bot

    @Subject
    private DaysBetweenListener listener

    void setup() {
        bot = Mock()
        listener = new DaysBetweenListener(bot)
    }

    def "should send message to the user asking enter dates"() {
        given:
        def event = new DaysBetweenSelectedEvent(123L)

        when:
        listener.handle(event)

        then:
        1 * bot.execute(_) >> { SendMessage sendMessage ->
            with(sendMessage.parameters) {
                chat_id == 123L
                text == DaysBetweenListener.HEADER
                reply_markup.class == ForceReply
            }
        }
    }
}
