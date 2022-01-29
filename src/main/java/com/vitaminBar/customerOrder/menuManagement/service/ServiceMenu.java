package com.vitaminBar.customerOrder.menuManagement.service;

import com.vitaminBar.customerOrder.menuManagement.dto.MenuDto;
import com.vitaminBar.customerOrder.menuManagement.dto.TypeInformationDto;

import java.util.List;

public interface ServiceMenu {
    List<MenuDto> getAllBeverages();
    List<TypeInformationDto> getAllItemsOfOneType(String typeName);
    TypeInformationDto getItemInformation(String itemName);
    void addNewBeverageToMenu(MenuDto menuDto);
    void updateMainBeverage(String typeName, String newTypeName);
    void updateAnItemInAType(String itemName, TypeInformationDto newDetail);
    void deleteAnItem(String itemName);
    void deleteABeverageWithAllItems(String typename);


}
