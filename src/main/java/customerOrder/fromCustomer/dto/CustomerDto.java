package customerOrder.fromCustomer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.sql.Date;

@Data
@AllArgsConstructor
public class CustomerDto {
    private String name;
    private Date dateOfBirth;
    private String phone;
    private String email;
    private String deliveryAddress;
    private int point;
}
