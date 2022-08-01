package com.vitaminBar.customerOrder.menuManagement.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypeInformationDto {
    String itemCode;
    String name;
    double price;
}

