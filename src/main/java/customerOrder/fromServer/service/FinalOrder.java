package customerOrder.fromServer.service;

import customerOrder.fromServer.dto.CompletedOrderDto;

public interface FinalOrder {
    void getCompletedOrderAfterCalculate(String customerPhone, String orderNumber);
    CompletedOrderDto getCompletedOrder(String orderNumber);
    void deleteCompletedOrder(String orderNumber);
}
