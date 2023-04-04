package mw.micro.customer;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import mw.micro.amqp.RabbitMQMessageProducer;
import mw.micro.clients.fraud.FraudCheckResponse;
import mw.micro.clients.fraud.FraudClient;
import mw.micro.clients.notification.NotificationClient;
import mw.micro.clients.notification.NotificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    private final RestTemplate restTemplate;
    private final FraudClient fraudClient;

    private final RabbitMQMessageProducer  rabbitMQMessageProducer;

    private final NotificationClient notificationClient;

    public void registerCustomer(CustomerRegistrationRequest request) {
        //builder pattern
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();

        customerRepository.saveAndFlush(customer);

        //previous way of service communication, before EnableFeignClients
        /*FraudCheckResponse fraudCheckResponseOld = restTemplate.getForObject(
                "http://FRAUD:8081/api/v1/fraud-check/{customerId}",
                FraudCheckResponse.class,
                customer.getId());*/

        FraudCheckResponse fraudCheckResponse = fraudClient.isFraudster(customer.getId());

        if(fraudCheckResponse.isFraudster()){
            throw new IllegalStateException("It is fraudster");
        }

        //message:
        NotificationRequest notificationRequest = new NotificationRequest(customer.getId(),
                customer.getEmail(),
                String.format("Hi %s, welcome", customer.getFirstName()));

        //previous synchronous communication:
        //notificationClient.sendNotification(notificationRequest);

        //asynchronous communication
        //with the same static params defined in consumer (notification):
        rabbitMQMessageProducer.publish(
                notificationRequest,
                "internal.exchange",
                "internal.notification.routing-key"
        );
        
    }
}
