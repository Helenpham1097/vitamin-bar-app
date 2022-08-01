package com.vitaminBar.customerOrder.menuManagement.controller.request;


import com.vitaminBar.customerOrder.menuManagement.model.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DetailRequest {
    String itemName;
    String newCode;
    String newName;
    double newPrice;
    Menu beverage;
    String typeName;



}
