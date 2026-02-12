package com.hackathon.video.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_KEY = "video.processing.queue";
    public static final String EXCHANGE_KEY = "video.processing.exchange";
    public static final String ROUTING_KEY = "video.processing.request";
    public static final String DLQ = "video.processing.dlq";
    public static final String RESULT_QUEUE_KEY = "video.processing.result.queue";

    @Bean
    public Queue queue() {
        return QueueBuilder.durable(QUEUE_KEY)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", DLQ)
                .build();
    }

    @Bean
    public Queue deadLetterQueue() {
        return new Queue(DLQ);
    }

    @Bean
    public Queue resultQueue() {
        return new Queue(RESULT_QUEUE_KEY);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_KEY);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
