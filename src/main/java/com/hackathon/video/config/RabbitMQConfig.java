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
    public static final String NOTIFICATION_QUEUE = "video.notification.queue";
    public static final String NOTIFICATION_EXCHANGE = "video.notification.exchange";
    public static final String NOTIFICATION_ROUTING_KEY = "video.notification.send";

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
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_KEY);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(NOTIFICATION_QUEUE);
    }

    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(NOTIFICATION_EXCHANGE);
    }

    @Bean
    public Binding notificationBinding(Queue notificationQueue, TopicExchange notificationExchange) {
        return BindingBuilder.bind(notificationQueue).to(notificationExchange).with(NOTIFICATION_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
