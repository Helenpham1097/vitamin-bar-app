package com.vitaminBar.customerOrder.menuManagement.exceptionHandler;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//@ResponseStatus(HttpStatus.NOT_FOUND)
public class MenuNotFoundException extends RuntimeException{
    public MenuNotFoundException(String msg){
        super(msg);
    }
}
