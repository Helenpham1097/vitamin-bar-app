package com.vitaminBar.customerOrder.ordersManagement.controller.request;

import com.vitaminBar.customerOrder.ordersManagement.dto.CartItemsDto;
import com.vitaminBar.customerOrder.ordersManagement.dto.CustomerDto;
import com.vitaminBar.customerOrder.ordersManagement.dto.OrderDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;
import java.util.Set;

//@NoArgsConstructor
@Data
//@AllArgsConstructor
public class OrderRequest {
    CustomerDto customer;
    String name;
    Date dateOfBirth;
    String phone;
    String email;
    String deliveryAddress;
    int point;

    List<OrderDto> orders;
    OrderDto order;
    String orderNumber;
    double totalBill;

    Set<CartItemsDto> items;
    String itemCode;
    String itemName;
    int quantity;
    double price;

    public OrderRequest(String phone, CustomerDto customerDto, OrderDto orderDto, Set<CartItemsDto> itemsDto){
        this.phone = phone;
        this.customer = customerDto;
        this.order = orderDto;
        this.items = itemsDto;
    }
}
