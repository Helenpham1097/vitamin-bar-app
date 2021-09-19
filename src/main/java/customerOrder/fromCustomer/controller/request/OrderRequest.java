package customerOrder.fromCustomer.controller.request;

import customerOrder.fromCustomer.dto.CartItemsDto;
import customerOrder.fromCustomer.dto.CustomerDto;
import customerOrder.fromCustomer.dto.OrderDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.sql.Date;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
public class OrderRequest {
    CustomerDto customer;
    String name;
    Date dateOfBirth;
    String phone;
    String email;
    String deliveryAddress;
    int point;

    List<OrderDto> orders;
    OrderDto order;
    String orderNumber;
    double totalBill;

    Set<CartItemsDto> items;
    String itemCode;
    String itemName;
    int quantity;
    double price;
}
