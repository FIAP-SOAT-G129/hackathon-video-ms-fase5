package com.hackathon.video.config;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.MessageConverter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class RabbitMQConfigTest {

    private final RabbitMQConfig config = new RabbitMQConfig();

    @Test
    void shouldCreateQueues() {
        assertNotNull(config.queue());
        assertEquals(RabbitMQConfig.QUEUE_KEY, config.queue().getName());

        assertNotNull(config.deadLetterQueue());
        assertEquals(RabbitMQConfig.DLQ, config.deadLetterQueue().getName());

        assertNotNull(config.resultQueue());
        assertEquals(RabbitMQConfig.RESULT_QUEUE_KEY, config.resultQueue().getName());
    }

    @Test
    void shouldCreateExchange() {
        TopicExchange exchange = config.exchange();
        assertNotNull(exchange);
        assertEquals(RabbitMQConfig.EXCHANGE_KEY, exchange.getName());
    }

    @Test
    void shouldCreateBinding() {
        Queue queue = new Queue("testQueue");
        TopicExchange exchange = new TopicExchange("testExchange");

        Binding binding = config.binding(queue, exchange);

        assertNotNull(binding);
        assertEquals(RabbitMQConfig.ROUTING_KEY, binding.getRoutingKey());
    }

    @Test
    void shouldCreateMessageConverter() {
        MessageConverter converter = config.jsonMessageConverter();
        assertNotNull(converter);
    }
}
