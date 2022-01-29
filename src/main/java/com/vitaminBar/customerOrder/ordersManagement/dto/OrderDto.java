package com.vitaminBar.customerOrder.ordersManagement.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@NoArgsConstructor
@Data
@AllArgsConstructor
public class OrderDto {
    @JsonProperty("order")
    String orderNumber;
    @JsonProperty("totalBill")
    double totalBill;

}
