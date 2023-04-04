package mw.micro.amqp;

import lombok.AllArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
@AllArgsConstructor
public class RabbitMQConfig {

    private final ConnectionFactory connectionFactory; //imported from Spring Framework

    //template to sending messages to the queue (publish)
    @Bean
    public AmqpTemplate amqpTemplate(){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        //make producer send messages in json format
        rabbitTemplate.setMessageConverter(jacksonConverter());
        return rabbitTemplate;
    }

    //template to receive messages from the queue (listen, consume)
    @Bean
    public SimpleRabbitListenerContainerFactory listenerFactory(){
        SimpleRabbitListenerContainerFactory factory
                = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jacksonConverter());
        return factory;
    }

    //moved to separated bean to use any specific mapper module
    @Bean
    public MessageConverter jacksonConverter(){
        //Jackson2ObjectMapperBuilder.json().modules();
        MessageConverter jacksonConverter = new Jackson2JsonMessageConverter();
        return jacksonConverter;
    }

}


