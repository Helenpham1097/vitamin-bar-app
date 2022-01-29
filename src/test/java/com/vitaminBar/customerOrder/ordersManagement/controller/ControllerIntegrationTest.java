package com.vitaminBar.customerOrder.ordersManagement.controller;


import com.vitaminBar.customerOrder.ordersManagement.service.CustomerOrderService;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;


@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CustomerOrderService.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerIntegrationTest {
    @LocalServerPort
    private int port;
    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();

    @Test
    public void getAllCustomer() throws JSONException {
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/store-order/customers"),
                HttpMethod.GET, entity, String.class);
        String expected = "{name:Helen,dateOfBirth:1997-10-10,phone:0212971569,email:helenpham,deliveryAddress:29 Corricvale, point:0}";
        JSONAssert.assertEquals(expected, response.getBody(), false);
    }
        private String createURLWithPort(String uri) {
            return "http://localhost:" + port + uri;
        }



}
