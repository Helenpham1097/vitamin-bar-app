package customerOrder.fromServer.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "completed_items_of_order")
public class CompletedItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long item_id;

    @Column(name = "item_code")
    String itemCode;

    @Column(name = "item_name")
    String itemName;

    @Column(name = "price")
    double price;

    @Column(name = "quantity")
    int quantity;

    @ManyToMany(mappedBy = "completedItems")
    Set<CompletedOrder> completedOrders = new HashSet<>();

    public long getItem_id() {
        return item_id;
    }

    public void setItem_id(long item_id) {
        this.item_id = item_id;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Set<CompletedOrder> getCompletedOrders() {
        return completedOrders;
    }

    public void setCompletedOrders(Set<CompletedOrder> completedOrders) {
        this.completedOrders = completedOrders;
    }
}
