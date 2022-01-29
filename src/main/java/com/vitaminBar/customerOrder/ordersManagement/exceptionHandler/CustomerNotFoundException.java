package com.vitaminBar.customerOrder.ordersManagement.exceptionHandler;

public class CustomerNotFoundException extends RuntimeException{
    public CustomerNotFoundException(String msg){
        super(msg);
    }
}
