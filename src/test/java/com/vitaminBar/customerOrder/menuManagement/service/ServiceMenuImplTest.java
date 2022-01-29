package com.vitaminBar.customerOrder.menuManagement.service;

import com.vitaminBar.customerOrder.menuManagement.dao.MenuRepository;
import com.vitaminBar.customerOrder.menuManagement.dao.TypeInformationRepository;
import com.vitaminBar.customerOrder.menuManagement.dto.MenuDto;
import com.vitaminBar.customerOrder.menuManagement.dto.TypeInformationDto;
import com.vitaminBar.customerOrder.menuManagement.exceptionHandler.MenuNotFoundException;
import com.vitaminBar.customerOrder.menuManagement.exceptionHandler.TypeInformationNotFoundException;
import com.vitaminBar.customerOrder.menuManagement.model.Menu;
import com.vitaminBar.customerOrder.menuManagement.model.TypeInformation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceMenuImplTest {

    @Mock
    MenuRepository menuRepository;
    @Mock
    TypeInformationRepository typeInformationRepository;

    private ServiceMenuImpl serviceTest;

    @BeforeEach
    void setUp() {
        serviceTest = new ServiceMenuImpl(menuRepository, typeInformationRepository);
    }

    @Test
    void testingAddNewBeverageToMenu_returnExpectedValue_WhenGivenNonExistingMenu() {

        given(menuRepository.fetchByTypeName("Milk Tea")).willReturn(null);
        serviceTest.addNewBeverageToMenu(new MenuDto("Milk Tea", List.of(new TypeInformationDto())));

        ArgumentCaptor<Menu> menuArgumentCaptor = ArgumentCaptor.forClass(Menu.class);
//        ArgumentCaptor<TypeInformation> typeInformationArgumentCaptor = ArgumentCaptor.forClass(TypeInformation.class);

        verify(menuRepository).save(menuArgumentCaptor.capture());
//        verify(typeInformationRepository).save(typeInformationArgumentCaptor.capture());

        assertThat("Milk Tea").isEqualTo(menuArgumentCaptor.getValue().getTypeName());
        assertThat(1).isEqualTo(menuArgumentCaptor.getValue().getListBeverages().size());

    }

    @Test
    void testingAddNewBeverageToMenu_returnExpectedValue_WhenGivenExistingMenu() {

        List<TypeInformation> mocKTypeInformation = new ArrayList<>();

        given(menuRepository.fetchByTypeName("Milk Tea"))
                .willReturn(new Menu(1L, "Milk Tea", mocKTypeInformation));

        serviceTest.addNewBeverageToMenu(new MenuDto("Milk Tea", List.of(new TypeInformationDto("MT02", "Chocolate Milk Tea", 10.0))));

        ArgumentCaptor<Menu> menuArgumentCaptor = ArgumentCaptor.forClass(Menu.class);

        verify(menuRepository).save(menuArgumentCaptor.capture());

        assertThat(menuArgumentCaptor.getValue().getListBeverages().size()).isEqualTo(1);

    }

    @Test
    void testingGetAllBeverages_returnExpectedList() {
        serviceTest.getAllBeverages();
        verify(menuRepository, times(1)).findAll();
    }

    @Test
    void testingGetAllBeverages_returnException_whenGivenNonExistingBeverage() {
        when(menuRepository.findAll()).thenReturn(null);
        assertThatThrownBy(() -> serviceTest.getAllBeverages())
                .isInstanceOf(MenuNotFoundException.class)
                .hasMessageContaining("There is no beverage available");
    }

    @Test
    void testingGetAllItemsOfOneType_returnExpectedDetailList_whenGivenAnExistingType() {
        Menu menu = new Menu(1L, "Milk Tea", List.of(new TypeInformation()));
        given(menuRepository.fetchByTypeName(any())).willReturn(menu);
        List<TypeInformationDto> expectedList = serviceTest.getAllItemsOfOneType("Milk Tea");
        assertThat(1).isEqualTo(expectedList.size());
    }

    @Test
    void testingGetAllItemsOfOneType_returnException_whenGivenNonExistingType() {
        Menu menu = new Menu(1L, "Milk Tea", List.of(new TypeInformation()));
        given(menuRepository.fetchByTypeName(any())).willReturn(null);
        assertThatThrownBy(() -> serviceTest.getAllItemsOfOneType("Milk Tea"))
                .isInstanceOf(MenuNotFoundException.class)
                .hasMessageContaining("Menu: " + menu.getTypeName() + " is not valid");
    }

    @Test
    void testingGetItemInformation_returnExpectedValue_whenGivenExistingTypeInformation() {
        String itemName = "Brown Sugar Milk Tea";
        when(typeInformationRepository.findSpecificItemByName(itemName))
                .thenReturn(new TypeInformation(1L, "MT01", itemName, 10.0, new Menu()));
        TypeInformationDto expectedValue = serviceTest.getItemInformation(itemName);
        assertThat(expectedValue.getItemCode()).isEqualTo("MT01");
    }

    @Test
    void testingGetItemInformation_returnException_whenGivenNonExistingTypeInformation() {
        String itemName = "Brown Sugar Milk Tea";
        when(typeInformationRepository.findSpecificItemByName(itemName))
                .thenReturn(null);
        assertThatThrownBy(() -> serviceTest.getItemInformation(itemName))
                .isInstanceOf(TypeInformationNotFoundException.class)
                .hasMessageContaining("Beverage " + itemName + " is not available");
    }

    @Test
    void testingUpdateMainBeverage_returnExpectedValue_whenGivenExistingBeverageType() {
        String typeName = "Milk Tea";
        String newTypeName = "Cheesy Tea";
        when(menuRepository.fetchByTypeName(typeName)).thenReturn(new Menu(1L, "Milk Tea", List.of(new TypeInformation())));
        serviceTest.updateMainBeverage(typeName, newTypeName);
        ArgumentCaptor<Menu> menuArgumentCaptor = ArgumentCaptor.forClass(Menu.class);
        verify(menuRepository).save(menuArgumentCaptor.capture());
        assertThat(menuArgumentCaptor.getValue().getTypeName()).isEqualTo(newTypeName);
    }

    @Test
    void testingUpdateMainBeverage_returnException_whenGivenNonExistingBeverageType() {
        String typeName = "Milk Tea";
        String newTypeName = "Cheesy Tea";
        when(menuRepository.fetchByTypeName(typeName)).thenReturn(null);
        assertThatThrownBy(() -> serviceTest.updateMainBeverage(typeName, newTypeName))
                .isInstanceOf(MenuNotFoundException.class)
                .hasMessageContaining("Your current beverage is not available");
    }

    @Test
    void testingUpdateAnItemInAType_returnExpectedValue_whenGivenExistingItem() {

        TypeInformation currentInformation = new TypeInformation(1L, "MT1", "Milk Tea", 12.0, new Menu());
        TypeInformationDto typeInformationDto = new TypeInformationDto("CT03", "Cheesy Peachy Tea", 10.0);
        when(typeInformationRepository.findSpecificItemByName(currentInformation.getName())).thenReturn(currentInformation);
        serviceTest.updateAnItemInAType(currentInformation.getName(), typeInformationDto);
        ArgumentCaptor<TypeInformation> argumentCaptor = ArgumentCaptor.forClass(TypeInformation.class);
        verify(typeInformationRepository).save(argumentCaptor.capture());
        String expectNewItemName = argumentCaptor.getValue().getName();
        assertThat(expectNewItemName).isEqualTo("Cheesy Peachy Tea");
    }

    @Test
    void testingUpdateAnItemInAType_returnException_whenGivenExistingItem() {
        String itemName = "Milk Tea";
        TypeInformationDto typeInformationDto = new TypeInformationDto("CT03", "Cheesy Peachy Tea", 10.0);
        when(typeInformationRepository.findSpecificItemByName(itemName)).thenReturn(null);
        assertThatThrownBy(() -> serviceTest.updateAnItemInAType(itemName, typeInformationDto))
                .isInstanceOf(TypeInformationNotFoundException.class)
                .hasMessageContaining("Your current beverage is not available");
    }

    @Test
    void testingDeleteAnItem_returnExpectedValue_whenGivenNonExistingItem() {
        String itemName = "Milk Tea";
        when(typeInformationRepository.findSpecificItemByName(itemName)).thenReturn(new TypeInformation(1L, "MT01", itemName, 10.0, new Menu()));
        serviceTest.deleteAnItem(itemName);
        verify(typeInformationRepository, times(1)).deleteByName(itemName);
    }

    @Test
    void testingDeleteAnItem_returnException_whenGivenNonExistingItem() {
        String itemName = "Milk Tea";
        when(typeInformationRepository.findSpecificItemByName(itemName)).thenReturn(null);
        assertThatThrownBy(() -> serviceTest.deleteAnItem(itemName))
                .isInstanceOf(TypeInformationNotFoundException.class)
                .hasMessageContaining("This beverage is not available");
    }

    @Test
    void testingDeleteABeverageWithAllItems_returnExpectedValue_whenGivenExistingMenuType() {
        String typeName = "Milk Tea";
        when(menuRepository.fetchByTypeName(typeName)).thenReturn(new Menu(1L, typeName, List.of(new TypeInformation())));
        serviceTest.deleteABeverageWithAllItems(typeName);
        verify(menuRepository, times(1)).deleteByTypeName(typeName);
        //       verify(typeInformationRepository,times(1)).deleteByName(typeName);
    }

    @Test
    void toDTO() {
        List<TypeInformation> list = List.of(new TypeInformation(1L, "MT01", "Milk Tea", 10.0, new Menu()));
        List<TypeInformationDto> expectedList = serviceTest.toDTO(list);
        assertThat(expectedList.size()).isEqualTo(list.size());
        assertThat(expectedList.get(0).getName()).isEqualTo(list.get(0).getName());
    }

    @Test
    void toDtoItem() {
        TypeInformation info = new TypeInformation(1L,"MT01", "Milk Tea", 10.0, new Menu());
        TypeInformationDto expected = serviceTest.toDtoItem(info);
        assertThat(expected.getName()).isEqualTo(info.getName());
    }

    @Test
    void toGeneralDto() {
        Menu menu = new Menu (1L, "Milk tea", List.of(new TypeInformation()));
        MenuDto menuDto = serviceTest.toGeneralDto(menu);
        assertThat(menu.getListBeverages().size()).isEqualTo(menuDto.getDetails().size());
    }
}