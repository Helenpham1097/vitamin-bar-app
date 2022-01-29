package com.vitaminBar.customerOrder.ordersManagement.dao;


import com.vitaminBar.customerOrder.ordersManagement.model.CompletedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompletedItemRepository extends JpaRepository<CompletedItem, Long> {
}
