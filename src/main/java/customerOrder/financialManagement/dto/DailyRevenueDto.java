package customerOrder.financialManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;

@Data
@AllArgsConstructor
public class DailyRevenueDto {
    Date date;
    double totalRevenue;
}
