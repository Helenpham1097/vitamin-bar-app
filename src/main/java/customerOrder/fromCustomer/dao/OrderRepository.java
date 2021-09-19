package customerOrder.fromCustomer.dao;


import customerOrder.fromCustomer.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query(value = "Select * from bill where bill.order_number =:number", nativeQuery = true)
    Order getOrderByOrderNumber(@Param("number") String number);

    @Transactional
    @Modifying
    @Query(value = "Delete from bill where bill.order_number =:number", nativeQuery = true)
    void deleteBill(@Param("number") String orderNumber);
}
