package customerOrder.fromServer.dto;

import customerOrder.fromCustomer.dto.CartItemsDto;
import customerOrder.fromCustomer.dto.OrderDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class CompletedOrderDto {
   OrderDto orderDto;
   Set<CartItemsDto> cartItemsDto;
}
