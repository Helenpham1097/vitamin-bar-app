package com.vitaminBar.customerOrder.ordersManagement.exceptionHandler;

public class CompletedOrderNotFoundException extends RuntimeException{
    public CompletedOrderNotFoundException(String msg){
        super(msg);
    }

}
