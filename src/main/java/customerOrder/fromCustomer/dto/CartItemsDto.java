package customerOrder.fromCustomer.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartItemsDto {
    String itemCode;
    String itemName;
    double price;
    int quantity;

}
