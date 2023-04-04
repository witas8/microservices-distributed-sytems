package mw.micro.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

//to inject RabbitMQMessageProducer from mw.micro.amqp
@SpringBootApplication(
        scanBasePackages = {
                "mw.micro.customer",
                "mw.micro.amqp"
        }
)
@EnableEurekaClient
@EnableFeignClients(basePackages = "mw.micro.clients")
public class CustomerApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class, args);
    }
}
