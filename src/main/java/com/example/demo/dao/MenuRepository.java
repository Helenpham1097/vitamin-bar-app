package com.example.demo.dao;


import com.example.demo.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Query(value = "Select * from menu where menu.type =:name", nativeQuery = true)
    Menu fetchByTypeName(@Param("name") String name);

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "DELETE FROM Menu WHERE menu.type =:name", nativeQuery = true)
    void deleteByTypeName(@Param("name")String name);
}
