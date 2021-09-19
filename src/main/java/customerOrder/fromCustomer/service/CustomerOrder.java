package customerOrder.fromCustomer.service;

import customerOrder.fromCustomer.dto.CartItemsDto;
import customerOrder.fromCustomer.dto.CustomerDto;
import customerOrder.fromCustomer.dto.OrderDto;
import java.util.List;
import java.util.Set;

public interface CustomerOrder {
    List<CustomerDto> getAllCustomersInformation();
    List<OrderDto> findAllOrdersOfACustomer(String phoneNumber);
    Set<CartItemsDto> getItemsOfAOrder(String orderNumber);
    void orderedReceiveFromCustomer(String customerPhone, CustomerDto customerInfo, OrderDto orderDto, Set<CartItemsDto> itemDtos);
    void updateQuantityOfItemsInAnOrder(String orderNumber, String itemCode, int quantity);
    void deleteAnItemOfOrder(String itemName);
    void deleteOrder(String orderNumber);

}
