package com.vitaminBar.customerOrder.ordersManagement.exceptionHandler;

public class CompletedItemNotFoundException extends RuntimeException {
    public CompletedItemNotFoundException(String msg){
        super(msg);
    }
}
