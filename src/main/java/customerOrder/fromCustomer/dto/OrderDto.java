package customerOrder.fromCustomer.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class OrderDto {
    String orderNumber;
    double totalBill;

}
