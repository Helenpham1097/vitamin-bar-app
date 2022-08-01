package com.vitaminBar.customerOrder.financialManagement.service;


import com.vitaminBar.customerOrder.financialManagement.dto.DailyRevenueDto;
import com.vitaminBar.customerOrder.financialManagement.dto.MonthlyRevenueDto;

import java.sql.Date;
import java.util.List;

public interface FinancialManagement {
    List<DailyRevenueDto> getAllDailyRevenue();
    List<MonthlyRevenueDto> getAllMonthlyRevenue();
    DailyRevenueDto getASpecificDateRevenue(Date date);
    MonthlyRevenueDto getASpecificMonthRevenue(String month);
}
