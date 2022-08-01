package com.vitaminBar.customerOrder.ordersManagement.controller.request;

import lombok.Data;

@Data
public class FinalRequest {
    String customerPhone;
    String orderNumber;
    public FinalRequest(String orderNumber){
        this.orderNumber = orderNumber;
    }
}
