package com.vitaminBar.customerOrder.ordersManagement.dao;

import com.vitaminBar.customerOrder.ordersManagement.model.Item;
import com.vitaminBar.customerOrder.ordersManagement.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.Set;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository repositoryTest;

    private Item item;

    @BeforeEach
    void setUp(){
        this.item = new Item(1L, "MT01",
                "Chocolate Milk Tea", 10.0,2,Set.of(new Order()));
    }
    @Test
    void testingDeleteItemByName_returnExpectedValue_whenGivenAnItemName() {
        repositoryTest.save(item);
        repositoryTest.deleteItemByName(this.item.getItemName());
        Optional<Item> expectedValue = repositoryTest.findById(item.getId());
        if(expectedValue.isEmpty()){
            System.out.println("deleted");
        }

    }
}