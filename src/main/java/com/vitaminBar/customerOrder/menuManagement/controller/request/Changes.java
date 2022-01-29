package com.vitaminBar.customerOrder.menuManagement.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Changes {
    private String currentName;
    private String newName;
}
