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
class TypeInformationRepositoryTest {

    @Autowired
    TypeInformationRepository repositoryTest;
    @Autowired
    MenuRepository menuRepository;
    private TypeInformation typeInformation;
    private Menu menu;

    @BeforeEach
    void setUp(){
        menu = new Menu(1L, "MilkTea", List.of(new TypeInformation()));
        typeInformation = new TypeInformation(1L, "MT01", "Chocolate Milk Tea", 20,menu);
    }

    @AfterEach
    void tearDown(){
        repositoryTest.deleteAll();
    }

    @Test
    void testingFetchBySpecificName_returnExpectedValue_WhenGivenAnExistingBeverage() {
        menuRepository.save(menu);
        repositoryTest.save(typeInformation);
        TypeInformation expectedValue = repositoryTest.findSpecificItemByName(typeInformation.getName());
        assertThat(expectedValue.getName()).isEqualTo(typeInformation.getName());
    }

    @Test
    void testingFetchBySpecificName_returnNullValue_WhenGivenANonExistingBeverage() {
        TypeInformation expectedValue = repositoryTest.findSpecificItemByName(typeInformation.getName());
        assertThat(expectedValue).isNull();
    }

    @Test
    void testFindingSpecificItemByItsCode_returnExpectedValue_whenGivenAnExistingBeverage() {
        menuRepository.save(menu);
        repositoryTest.save(typeInformation);
        TypeInformation expectedValue = repositoryTest.findSpecificItemByItsCode(typeInformation.getItemCode());
        assertThat(expectedValue.getItemCode()).isEqualTo(typeInformation.getItemCode());
    }

    @Test
    void testingFindingSpecificItemByItsCode_returnNullValue_WhenGivenANonExistingBeverage() {
        TypeInformation expectedValue = repositoryTest.findSpecificItemByItsCode(typeInformation.getItemCode());
        assertThat(expectedValue).isNull();
    }

    @Test
    void testingDeleteByName_returnDeleteMessage_whenGivenAnExistingBeverage() {
        menuRepository.save(menu);
        repositoryTest.save(typeInformation);
        repositoryTest.deleteByName(typeInformation.getName());
        Optional<TypeInformation> optional = repositoryTest.findById(typeInformation.getId());
        if(optional.isEmpty()){
            System.out.println("Deleted");
        }
    }

    @Test
    void testDeleteByBeverageId_returnDeleteMessage_whenGivenAnExistingBeverage() {
        menuRepository.save(menu);
        repositoryTest.save(typeInformation);
        repositoryTest.deleteByBeverageId(typeInformation.getId());
        Optional<TypeInformation> optional = repositoryTest.findById(typeInformation.getId());
        if(optional.isEmpty()){
            System.out.println("Deleted");
        }
    }
}