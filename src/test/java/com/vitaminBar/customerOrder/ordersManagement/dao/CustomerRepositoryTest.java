package com.vitaminBar.customerOrder.ordersManagement.dao;

import com.vitaminBar.customerOrder.ordersManagement.model.Customer;
import com.vitaminBar.customerOrder.ordersManagement.model.Order;
import com.vitaminBar.customerOrder.ordersManagement.model.CompletedOrder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    CustomerRepository repositoryTest;
    private Customer customer;

    @BeforeEach
    void setUp(){
        this.customer = new Customer(1L,
                "Helen Pham",
                Date.valueOf("1997-10-10"),
                "0212971569",
                "helenpham1097@gmail.com",
                "29 Corricvale Way, Auckland",
                0,
                List.of(new Order()),
                List.of(new CompletedOrder()));
    }

    @AfterEach
    void tearDown(){
        repositoryTest.deleteAll();
    }

    @Test
    void testingGetCustomerByPhone_returnExpectedValue_whenGivenAPhoneNumber() {
        repositoryTest.save(customer);
        Customer expectedCustomer = repositoryTest.getCustomerByPhone(customer.getPhone());
        assertThat(expectedCustomer.getPhone()).isEqualTo(customer.getPhone());
    }
}