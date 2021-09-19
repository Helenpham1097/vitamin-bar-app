package customerOrder.fromCustomer.model;


import customerOrder.fromCustomer.model.Customer;
import customerOrder.fromCustomer.model.Item;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Entity
@Table(name = "bill")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long order_id;


    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "total_bill")
    private double totalBill;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    Customer customer;

    //new column
    @CreatedDate
    @Column(name = "order_date")
    private Timestamp orderDate;

    public long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate() {
        this.orderDate = Timestamp.from(Instant.now());
    }

    // new column

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "order_item",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private Set<Item> items = new HashSet<>();

    public void addItem(Item item) {
        this.items.add(item);
        item.getOrders().add(this);
    }

    public void addItems(Set<Item> items){
        this.items.addAll(items);
        items.forEach(item -> item.getOrders().add(this));
    }

    public void removeItem(Item item) {
        this.items.remove(item);
        item.getOrders().remove(this);
    }

    public void removeItems() {
        Iterator<Item> iterator = this.items.iterator();
        while (iterator.hasNext()) {
            Item item = iterator.next();
            item.getOrders().remove(this);
            iterator.remove();
        }
    }

    public double getTotalBill() {
        return totalBill;
    }

    public void setTotalBill(double totalBill) {
        this.totalBill = totalBill;
    }

    public long getId() {
        return order_id;
    }

    public void setId(long id) {
        this.order_id= id;
    }


    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }
}
