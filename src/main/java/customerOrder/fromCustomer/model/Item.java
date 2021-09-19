package customerOrder.fromCustomer.model;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long item_id;

    @Column(name = "item_code")
    String itemCode;

    @Column(name = "item_name")
    String itemName;

    @Column (name = "price")
    double price;

    @Column(name = "quantity")
    int quantity;

    @ManyToMany(mappedBy = "items")
    private Set<Order> orders = new HashSet<>();

    public void addOrder(Order order){

    }

    public long getId() {
        return item_id;
    }

    public void setId(long id) {
        this.item_id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }
}
