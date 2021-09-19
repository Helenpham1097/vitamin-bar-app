package customerOrder.financialManagement.controller;


import customerOrder.financialManagement.dto.DailyRevenueDto;
import customerOrder.financialManagement.dto.MonthlyRevenueDto;
import customerOrder.financialManagement.service.FinancialManagementImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("revenue")
public class RevenueController {

    @Autowired
    FinancialManagementImpl financialManagement;

    @Autowired
    public void setFinancialManagement(FinancialManagementImpl financialManagement) {
        this.financialManagement = financialManagement;
    }

    @GetMapping("/daily-revenue")
    public List<DailyRevenueDto> getAllDailyRevenues(){
       return financialManagement.getAllDailyRevenue();
    }

    @GetMapping("/monthly-revenue")
    public List<MonthlyRevenueDto> getAllCurrentMonthlyRevenues(){
        return financialManagement.getAllMonthlyRevenue();
    }

    @GetMapping("/monthly-revenue/{month}")
    public MonthlyRevenueDto getRevenueOfASpecificMonth(String month){
        return financialManagement.getASpecificMonthRevenue(month);
    }

    @GetMapping("/daily-revenue/{date}")
    public DailyRevenueDto getDailyRevenueOfADate(Date date){
        return financialManagement.getASpecificDateRevenue(date);
    }
}
