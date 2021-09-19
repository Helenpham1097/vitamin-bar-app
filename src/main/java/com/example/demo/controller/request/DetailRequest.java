package com.example.demo.controller.request;


import com.example.demo.model.Menu;
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
