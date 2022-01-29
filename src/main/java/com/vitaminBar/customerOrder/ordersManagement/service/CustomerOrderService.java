package com.vitaminBar.customerOrder.ordersManagement.service;

import com.vitaminBar.customerOrder.ordersManagement.dto.CompletedOrderDto;
import com.vitaminBar.customerOrder.ordersManagement.exceptionHandler.OrderNotFoundException;
import com.vitaminBar.customerOrder.ordersManagement.dto.CartItemsDto;
import com.vitaminBar.customerOrder.ordersManagement.dto.CustomerDto;
import com.vitaminBar.customerOrder.ordersManagement.dto.OrderDto;

import java.util.List;
import java.util.Set;

public interface CustomerOrderService {
    List<CustomerDto> getAllCustomersInformation();
    List<OrderDto> findAllOrdersOfACustomer(String phoneNumber);
    Set<CartItemsDto> getItemsOfAOrder(String orderNumber);
    void getNewOrder(String customerPhone, CustomerDto customerInfo, OrderDto orderDto, Set<CartItemsDto> itemDtos);
    void updateQuantityOfItemsInAnOrder(String orderNumber, String itemCode, int quantity);
    void deleteAnItemOfOrder(String orderNumber,String itemName) throws OrderNotFoundException;
    void deleteOrder(String orderNumber);

    void addNewCompletedOrder(String orderNumber);
    CompletedOrderDto findCompletedOrderByOrderNumber(String orderNumber);
    void deleteCompletedOrder(String orderNumber);
    void deleteAnItemOfCompletedOrder(String orderNumber, String itemCode);

}
