package com.example.demo.service;

import com.example.demo.dao.MenuRepository;
import com.example.demo.dao.TypeInformationRepository;
import com.example.demo.dto.MenuDto;
import com.example.demo.dto.TypeInformationDto;
import com.example.demo.model.Menu;
import com.example.demo.model.TypeInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ServiceMenuImpl implements ServiceMenu {
    @Autowired
    MenuRepository menuRepository;
    @Autowired
    TypeInformationRepository typeInformationRepository;

    private TypeInformation toTypeInformation(TypeInformationDto dto) {
        TypeInformation detail = new TypeInformation();
        detail.setItemCode(dto.getItemCode());
        detail.setName(dto.getName());
        detail.setPrice(dto.getPrice());
        return detail;
    }

    @Transactional
    public void addNewBeverageToMenu(String typeName, List<TypeInformationDto> infos) {
        Menu beverageType = menuRepository.fetchByTypeName(typeName);
        if (beverageType == null) {
            beverageType = new Menu();
            beverageType.setTypeName(typeName);
        }

        List<TypeInformation> typeInformation = infos.stream()
                .map(this::toTypeInformation)
                .collect(Collectors.toList());

        beverageType.addBeverages(typeInformation);
        menuRepository.save(beverageType);
    }

    @Transactional
    public List<MenuDto> getAllBeverages() {
        return menuRepository
                .findAll()
                .stream()
                .map(this::toGeneralDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<TypeInformationDto> getAllItemsOfOneType(String typeName) {
        Menu type = menuRepository.fetchByTypeName(typeName);
        return toDTO(type.getListBeverages());
    }

    @Transactional
    public TypeInformationDto getItemInformation(String itemName) {
        return toDtoItem(typeInformationRepository.findSpecificItemByName(itemName));
    }

    @Transactional
    public void updateMainBeverage(String typeName, String newTypeName) {
        Menu beverage = menuRepository.fetchByTypeName(typeName);
        List<TypeInformation> itemsOfThisBeverage = beverage.getListBeverages();
        beverage.setTypeName(newTypeName);
        itemsOfThisBeverage
                .stream()
                .iterator().next()
                .setMenu(beverage);
        menuRepository.save(beverage);
    }

    @Transactional
    public void updateAnItemInAType(String itemName, TypeInformationDto newDetail) {
        TypeInformation item = typeInformationRepository.findSpecificItemByName(itemName);
        toTypeInformation(newDetail);
        typeInformationRepository.save(item);
    }

    //  DELETE ON ITEM IN MAIN TYPE
    @Transactional
    public void deleteAnItem(String itemName) {
        typeInformationRepository.deleteByName(itemName);
    }

    @Transactional
    public void deleteABeverageWithAllItems(String typename) {
        Menu beverage = menuRepository.fetchByTypeName(typename);
        typeInformationRepository.deleteByBeverageId(beverage.getId());
        menuRepository.deleteByTypeName(typename);
    }

    public List<TypeInformationDto> toDTO(List<TypeInformation> beverages) {
        return beverages.stream()
                .map(item -> new TypeInformationDto(item.getItemCode(), item.getName(), item.getPrice()))
                .collect(Collectors.toList());
    }

    public TypeInformationDto toDtoItem(TypeInformation item) {
        return new TypeInformationDto(item.getItemCode(), item.getName(), item.getPrice());
    }

    public MenuDto toGeneralDto(Menu beverage) {
        return new MenuDto(beverage.getTypeName(), toDTO(beverage.getListBeverages()));
    }
}
