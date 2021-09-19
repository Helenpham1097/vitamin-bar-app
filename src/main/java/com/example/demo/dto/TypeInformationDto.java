package com.example.demo.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TypeInformationDto {
    String itemCode;
    String name;
    double price;
}

