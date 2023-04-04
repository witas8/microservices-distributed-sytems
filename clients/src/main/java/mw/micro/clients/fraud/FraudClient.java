package mw.micro.clients.fraud;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// a way without using api in an interface body:
// value is an application name from application.yaml
// path from microservice's controller
// @FeignClient(value = "fraud", path="api/v1/fraud-check")


//the parameter is an application name from application.yaml
@FeignClient("fraud")
public interface FraudClient {

    //transformed api from fraud controller
    @GetMapping(path = "api/v1/fraud-check/{customerId}")
    FraudCheckResponse isFraudster(
            @PathVariable("customerId") Integer customerId);

}

