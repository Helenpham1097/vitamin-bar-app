package com.vitaminBar.customerOrder.ordersManagement.dto;
import lombok.*;


@NoArgsConstructor
@Data
@AllArgsConstructor
public class OrderDto {
    String orderNumber;
    double totalBill;

}
