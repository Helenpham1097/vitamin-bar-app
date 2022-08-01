package com.vitaminBar.customerOrder.ordersManagement.exceptionHandler;

public class OrderNotFoundException extends RuntimeException{
    public OrderNotFoundException(String msg){
        super(msg);
    }
}
