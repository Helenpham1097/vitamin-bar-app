package customerOrder.fromServer.model;


import customerOrder.fromCustomer.model.Customer;
import customerOrder.fromServer.model.CompletedItem;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Entity
@Table(name = "completed_order")
public class CompletedOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long order_id;

    @Column(name = "order_number")
    private String orderNumber;



    @Column(name = "total_bill")
    private double totalBill;

    @CreatedDate
    @Column(name = "order_date")
    private Timestamp orderDate;


    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    Customer customer;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "completed_order_item",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    Set<CompletedItem> completedItems = new HashSet<>();

    public void addCompletedItem(CompletedItem completedItem) {
        this.completedItems.add(completedItem);
        completedItem.getCompletedOrders().add(this);
    }

    public void addCompletedItems(Set<CompletedItem> completedItems) {
        this.completedItems.addAll(completedItems);
        completedItems
                .stream()
                .map(item -> item.getCompletedOrders().add(this));
    }

    public void removeCompletedItem(CompletedItem completedItem) {
        this.completedItems.remove(completedItem);
        completedItem.getCompletedOrders().remove(this);
    }

    public void removeCompletedItems(Set<CompletedItem> completedItems) {
        Iterator<CompletedItem> iterator = completedItems.iterator();
        while (iterator.hasNext()) {
            CompletedItem completedItem = iterator.next();
            completedItem.completedOrders.remove(this);
            iterator.remove();
        }
    }

    public long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public double getTotalBill() {
        return totalBill;
    }

    public void setTotalBill(double totalBill) {
        this.totalBill = totalBill;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Set<CompletedItem> getCompletedItems() {
        return completedItems;
    }

    public void setCompletedItems(Set<CompletedItem> completedItems) {
        this.completedItems = completedItems;
    }
}
