package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Kafka message should have correct structure"

    label "kafka_message"

    input {
        triggeredBy("sendMessageTriggered()")
    }

    outputMessage {
        sentTo("test-topic")
        body([
                content: $(consumer(anyNonEmptyString()), producer("Hello Kafka!"))
        ])
        headers {
            contentType(applicationJson())
        }
    }
}
