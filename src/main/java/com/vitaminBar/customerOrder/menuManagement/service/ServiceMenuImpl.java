package com.vitaminBar.customerOrder.menuManagement.service;

import com.vitaminBar.customerOrder.menuManagement.dao.MenuRepository;
import com.vitaminBar.customerOrder.menuManagement.dao.TypeInformationRepository;
import com.vitaminBar.customerOrder.menuManagement.dto.MenuDto;
import com.vitaminBar.customerOrder.menuManagement.dto.TypeInformationDto;
import com.vitaminBar.customerOrder.menuManagement.exceptionHandler.MenuNotFoundException;
import com.vitaminBar.customerOrder.menuManagement.exceptionHandler.TypeInformationNotFoundException;
import com.vitaminBar.customerOrder.menuManagement.model.Menu;
import com.vitaminBar.customerOrder.menuManagement.model.TypeInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ServiceMenuImpl implements ServiceMenu {

    private final MenuRepository menuRepository;

    private final TypeInformationRepository typeInformationRepository;

    @Autowired
    public ServiceMenuImpl(MenuRepository menuRepository,
                           TypeInformationRepository typeInformationRepository) {
        this.menuRepository = menuRepository;
        this.typeInformationRepository = typeInformationRepository;
    }

    private TypeInformation toTypeInformation(TypeInformationDto dto) {
        TypeInformation detail = new TypeInformation();
        detail.setItemCode(dto.getItemCode());
        detail.setName(dto.getName());
        detail.setPrice(dto.getPrice());
        return detail;
    }

    @Transactional
    public void addNewBeverageToMenu(MenuDto menuDto) {
        Menu beverageType = menuRepository.fetchByTypeName(menuDto.getTypeName());
        if (beverageType == null) {
            beverageType = new Menu();
            beverageType.setTypeName(menuDto.getTypeName());
        }

        List<TypeInformation> typeInformation = menuDto.getDetails().stream()
                .map(this::toTypeInformation)
                .collect(Collectors.toList());

        beverageType.addBeverages(typeInformation);
        menuRepository.save(beverageType);
    }

    @Transactional
    public List<MenuDto> getAllBeverages() {
        List<Menu> menu = menuRepository.findAll();
        if(menu==null){
            throw new MenuNotFoundException("There is no beverage available");
        }
        return menu
                .stream()
                .map(this::toGeneralDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<TypeInformationDto> getAllItemsOfOneType(String typeName) throws MenuNotFoundException {
        Menu type = menuRepository.fetchByTypeName(typeName);
        if(type == null){
            throw new MenuNotFoundException("Menu: "+ typeName + " is not valid");
        }
        return toDTO(type.getListBeverages());
    }

    @Transactional
    public TypeInformationDto getItemInformation(String itemName) throws TypeInformationNotFoundException {
        TypeInformation typeInformation = typeInformationRepository.findSpecificItemByName(itemName);
        if(typeInformation==null){
            throw new TypeInformationNotFoundException("Beverage " + itemName + " is not available");
        }
        return toDtoItem(typeInformation);
    }

    @Transactional
    public void updateMainBeverage(String typeName, String newTypeName) throws MenuNotFoundException {
        Menu menu = menuRepository.fetchByTypeName(typeName);
        if(menu == null){
            throw new MenuNotFoundException("Your current beverage is not available");
        }
        List<TypeInformation> itemsOfThisBeverage = menu.getListBeverages();
        menu.setTypeName(newTypeName);
        itemsOfThisBeverage
                .stream()
                .iterator().next()
                .setMenu(menu);
        menuRepository.save(menu);
    }

    @Transactional
    public void updateAnItemInAType(String itemName, TypeInformationDto newDetail) throws TypeInformationNotFoundException{
        TypeInformation item = typeInformationRepository.findSpecificItemByName(itemName);
        if(item==null){
            throw new TypeInformationNotFoundException("Your current beverage is not available");
        }
        item.setName(newDetail.getName());
        item.setItemCode(newDetail.getItemCode());
        item.setPrice(newDetail.getPrice());
        typeInformationRepository.save(item);
    }

    //  DELETE ON ITEM IN MAIN TYPE
    @Transactional
    public void deleteAnItem(String itemName) throws TypeInformationNotFoundException {
        TypeInformation itemDetail = typeInformationRepository.findSpecificItemByName(itemName);
        if(itemDetail==null){
            throw new TypeInformationNotFoundException("This beverage is not available");
        }
        typeInformationRepository.deleteByName(itemName);
    }

    @Transactional
    public void deleteABeverageWithAllItems(String typename) throws MenuNotFoundException{
        Menu menu = menuRepository.fetchByTypeName(typename);
        if(menu == null){
            throw new MenuNotFoundException("Menu "+ menu.getTypeName()+" is not available");
        }
        typeInformationRepository.deleteByBeverageId(menu.getId());
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
