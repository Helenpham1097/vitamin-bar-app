package com.vitaminBar.customerOrder.ordersManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    private String name;
    private Date dateOfBirth;
    private String phone;
    private String email;
    private String deliveryAddress;
    private int point;
}
