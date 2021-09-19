package com.example.demo.service;

import com.example.demo.dto.MenuDto;
import com.example.demo.dto.TypeInformationDto;

import java.util.List;

public interface ServiceMenu {
    List<MenuDto> getAllBeverages();
    List<TypeInformationDto> getAllItemsOfOneType(String typeName);
    TypeInformationDto getItemInformation(String itemName);

    void addNewBeverageToMenu(String typeName, List<TypeInformationDto> infos);
    void updateMainBeverage(String typeName, String newTypeName);
    void updateAnItemInAType(String itemName, TypeInformationDto newDetail);
    void deleteAnItem(String itemName);
    void deleteABeverageWithAllItems(String typename);


}
