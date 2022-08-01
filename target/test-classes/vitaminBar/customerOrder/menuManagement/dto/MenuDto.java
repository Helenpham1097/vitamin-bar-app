package com.vitaminBar.customerOrder.menuManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuDto {
    String typeName;
    List<TypeInformationDto> details;
}
