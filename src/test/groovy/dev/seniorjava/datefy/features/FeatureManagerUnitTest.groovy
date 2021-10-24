package dev.seniorjava.datefy.features

import com.pengrad.telegrambot.model.Update
import dev.seniorjava.datefy.UnitTest
import dev.seniorjava.datefy.features.errors.UpdateNotHandledEvent
import org.springframework.context.ApplicationEventPublisher
import spock.lang.Subject

class FeatureManagerUnitTest extends UnitTest {

    @Subject
    private FeatureManager featureManager

    private Feature fooFeature
    private List<Feature> features
    private ApplicationEventPublisher eventPublisher

    void setup() {
        fooFeature = Mock()
        features = [fooFeature]
        eventPublisher = Mock()
        featureManager = new FeatureManager(features, eventPublisher)
    }

    def "should successfully handle the given update"() {
        given:
        def update = new Update()

        and: "should response that the updated was handled"
        fooFeature.handle(update) >> Result.HANDLED

        when:
        featureManager.processUpdate(update)

        then: "there should not be notification about not handled updates"
        0 * eventPublisher.publishEvent(_ as UpdateNotHandledEvent)
    }

    def "should notify the developer if the given update was not handled"() {
        given:
        def update = new Update()

        and: "should response that the updated was NOT handled"
        fooFeature.handle(update) >> Result.SKIPPED

        when:
        featureManager.processUpdate(update)

        then: "an UpdateNotHandledEvent must be published"
        1 * eventPublisher.publishEvent(_ as UpdateNotHandledEvent)
    }
}
