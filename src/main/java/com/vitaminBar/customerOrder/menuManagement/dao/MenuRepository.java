package com.vitaminBar.customerOrder.menuManagement.dao;

import com.vitaminBar.customerOrder.menuManagement.exceptionHandler.MenuNotFoundException;
import com.vitaminBar.customerOrder.menuManagement.model.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

class MenuRepositoryDelegate {

    @Autowired
    private MenuRepository menuRepository;

    public Menu fetchByTypeName(String name) {
        Menu menu = menuRepository.fetchByTypeName(name);
        if(menu == null) {
            throw new MenuNotFoundException("xxx");
        }

        return menu;
    }
}
@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Query(value = "Select * from menu where menu.type =:name", nativeQuery = true)
    Menu fetchByTypeName(@Param("name") String name);

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "DELETE FROM Menu WHERE menu.type =:name", nativeQuery = true)
    void deleteByTypeName(@Param("name")String name);
}
