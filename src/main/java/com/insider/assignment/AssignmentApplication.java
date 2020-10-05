package com.insider.assignment;

import org.springframework.amqp.core.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class AssignmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssignmentApplication.class, args);
    }

    @Bean
    public Queue topStoryQueue() {
        return new Queue("newTopStoryQueue", true);
    }

    @Bean
    CustomExchange delayExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange("delayed-update-exchange", "x-delayed-message", true, false, args);
    }

    @Bean
    Binding binding(Queue topStoryQueue, Exchange delayExchange) {
        return BindingBuilder.bind(topStoryQueue).to(delayExchange).with("all").noargs();
    }
}
