package com.vitaminBar.customerOrder.menuManagement.exceptionHandler;

public class TypeInformationNotFoundException extends RuntimeException{
    public TypeInformationNotFoundException(String msg){
        super(msg);
    }
}
