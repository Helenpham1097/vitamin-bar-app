package com.vitaminBar.customerOrder.ordersManagement.dao;


import com.vitaminBar.customerOrder.ordersManagement.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query(value = "Select * from customer where customer.phone =:phone", nativeQuery = true)
    Customer getCustomerByPhone(@Param("phone")String phone);
}
