package com.example.demo.controller.request;

import com.example.demo.dto.TypeInformationDto;
import com.example.demo.model.TypeInformation;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class MenuRequest {
    String typeName;
    List<TypeInformationDto> list;
    TypeInformationDto item;
    String itemCode;
    String name;
    double price;
    String newTypeName;
}
