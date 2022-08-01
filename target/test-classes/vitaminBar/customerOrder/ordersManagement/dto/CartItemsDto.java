package com.vitaminBar.customerOrder.ordersManagement.dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
//@AllArgsConstructor
public class CartItemsDto {

    @JsonCreator
    public CartItemsDto(@JsonProperty("itemCode")String itemCode,@JsonProperty("itemName") String itemName, @JsonProperty("price")double price, @JsonProperty("quantity")int quantity) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }

    String itemCode;
    String itemName;
    double price;
    int quantity;

}
