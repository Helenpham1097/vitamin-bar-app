package com.vitaminBar.customerOrder.ordersManagement.exceptionHandler;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(String msg){
        super(msg);
    }
}
