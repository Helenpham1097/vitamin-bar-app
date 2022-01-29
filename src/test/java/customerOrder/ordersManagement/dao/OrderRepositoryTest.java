package customerOrder.ordersManagement.dao;

import com.vitaminBar.customerOrder.ordersManagement.dao.CustomerRepository;
import com.vitaminBar.customerOrder.ordersManagement.dao.ItemRepository;
import com.vitaminBar.customerOrder.ordersManagement.dao.OrderRepository;
import com.vitaminBar.customerOrder.ordersManagement.model.Customer;
import com.vitaminBar.customerOrder.ordersManagement.model.Item;
import com.vitaminBar.customerOrder.ordersManagement.model.Order;
import com.vitaminBar.customerOrder.ordersManagement.model.CompletedOrder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository repositoryTest;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ItemRepository itemRepository;

    private Order order;
    private Customer customer;
    private Set<Item> items;

    @BeforeEach
    void setUp() {
        this.order = new Order(1L, "THTR10030810",
                100.0, customer, Timestamp.valueOf(LocalDateTime.now()), this.items);

        this.customer = new Customer(1L,
                "Helen Pham",
                Date.valueOf("1997-10-10"),
                "0212971569",
                "helenpham1097@gmail.com",
                "29 Corricvale Way, Auckland",
                0,
                List.of(new Order()),
                List.of(new CompletedOrder()));

        this.items = Set.of(new Item(1L, "MT01",
                "Chocolate Milk Tea", 10.0,2,Set.of(this.order)));
    }

    @AfterEach
    void tearDown(){
        repositoryTest.deleteAll();
    }

    @Test
    void testingGetOrderByOrderNumber_returnExpectedValue_whenGivenAnOrderNumber() {
        String orderNumber = "THTR10030810";
        customerRepository.save(customer);
        repositoryTest.save(order);
        Order expectedOrderReturned = repositoryTest.getOrderByOrderNumber(orderNumber);
        assertThat(expectedOrderReturned.getOrderNumber()).isEqualTo(orderNumber);
    }

    @Test
    void testingDeleteBill_returnExpectedValue_whenGivenAnOrderNumber() {
        String orderNumber = "THTR10030810";
        customerRepository.save(customer);
        repositoryTest.save(order);
        itemRepository.saveAll(items);
        repositoryTest.deleteBill(orderNumber);

        Optional<Order> order = repositoryTest.findById(this.order.getOrder_id());
        if (order.isEmpty()) {
            System.out.println("Order has been deleted");
        }
    }
}