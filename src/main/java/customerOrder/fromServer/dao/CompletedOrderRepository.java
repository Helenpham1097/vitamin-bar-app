package customerOrder.fromServer.dao;


import customerOrder.fromServer.model.CompletedOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

@Repository
public interface CompletedOrderRepository extends JpaRepository<CompletedOrder, Long> {
    @Query (value = "Select * from completed_order where completed_order.order_number =:number", nativeQuery = true)
    CompletedOrder getCompletedOrder(@Param("number") String orderNumber);

    @Transactional
    @Modifying
    @Query(value = "Delete from completed_order where completed_order.order_number =:number", nativeQuery = true)
    void deleteCompletedOrder(@Param("number") String orderNumber);

    @Query (value = "Select * from completed_order where date(order_date) =: orderDate", nativeQuery = true)
    List<CompletedOrder> getCompletedOrdersByDate(@Param("orderDate") Date orderDate);

    @Query (value = "select * from completed_order WHERE EXTRACT(MONTH FROM order_date) =:monthInNumber", nativeQuery = true)
    List<CompletedOrder> getCompletedOrdersByMonth(@Param("monthInNumber") int monthInNumber);


}
