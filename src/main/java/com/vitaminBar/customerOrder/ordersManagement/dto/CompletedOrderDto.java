package com.vitaminBar.customerOrder.ordersManagement.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompletedOrderDto {

   OrderDto orderDto;

   Set<CartItemsDto> cartItemsDto;
}
