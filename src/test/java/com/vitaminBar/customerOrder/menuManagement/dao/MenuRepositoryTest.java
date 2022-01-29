package com.vitaminBar.customerOrder.menuManagement.dao;

import com.vitaminBar.customerOrder.menuManagement.model.Menu;
import com.vitaminBar.customerOrder.menuManagement.model.TypeInformation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class MenuRepositoryTest {

    @Autowired
    MenuRepository menuRepositoryTest;
    private Menu menu;

    @BeforeEach
    void setUp() {
        menu = new Menu(1L, "MilkTea", List.of(new TypeInformation()));
    }

    @AfterEach
    void tearDown(){
        menuRepositoryTest.deleteAll();
    }

    @Test
    void testingFetchByTypeNameMethod_returnExpectedValue_WhenGivenExistingTypeName() {

        menuRepositoryTest.save(menu);
        Menu expectedMenu = menuRepositoryTest.fetchByTypeName("MilkTea");
        assertThat(expectedMenu.getTypeName()).isEqualTo(menu.getTypeName());

    }

    @Test
    void testingFetchByTypeNameMethod_returnNullValue_WhenGivenNonExistingTypeName(){

        Menu expectedMenu = menuRepositoryTest.fetchByTypeName(menu.getTypeName());
        assertThat(expectedMenu).isNull();
    }

    @Test
    void testingDeleteByTypeName_returnExpectedMassage_whenGivenExistingTypeNameOfMenu() {
        menuRepositoryTest.save(menu);
        menuRepositoryTest.deleteByTypeName(menu.getTypeName());
        Optional<Menu> optionalMenu = menuRepositoryTest.findById(menu.getId());
        if(optionalMenu.isEmpty()){
            System.out.println("Menu was deleted");
        }

    }

}