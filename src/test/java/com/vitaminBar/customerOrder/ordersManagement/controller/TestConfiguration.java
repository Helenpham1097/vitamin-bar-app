package com.vitaminBar.customerOrder.ordersManagement.controller;

import com.vitaminBar.customerOrder.ordersManagement.service.CustomerOrderService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;

//To be used by @ContextConfiguration
//@Configuration
public class TestConfiguration {

//    private CustomerOrder fakeService = Mockito.mock(CustomerOrder.class);

    @Bean
    public CustomerOrderService customerOrder() {
        CustomerOrderService fakeService = Mockito.mock(CustomerOrderService.class);
        //when(fakeService.findAllOrdersOfACustomer(a))
        // return fake/stub
        return fakeService;
    }
}
