package com.vitaminBar.customerOrder.ordersManagement.service;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ItemChanges {

    String orderNumber;
    String itemCode;
    int newQuantity;
    public ItemChanges(String orderNumber, String itemCode){
        this.orderNumber = orderNumber;
        this.itemCode = itemCode;
    }
}
