package com.vitaminBar.customerOrder.menuManagement.controller.request;

import com.vitaminBar.customerOrder.menuManagement.dto.TypeInformationDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class MenuRequest {
    String typeName;
    List<TypeInformationDto> list;
    TypeInformationDto item;
    String itemCode;
    String name;
    double price;
    String newTypeName;
    public MenuRequest(String name, TypeInformationDto item){
        this.name = name;
        this.item = item;
    }
}
