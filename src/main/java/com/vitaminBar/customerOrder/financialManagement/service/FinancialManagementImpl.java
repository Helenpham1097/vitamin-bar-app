package com.vitaminBar.customerOrder.financialManagement.service;


import com.vitaminBar.customerOrder.financialManagement.dto.DailyRevenueDto;
import com.vitaminBar.customerOrder.financialManagement.dto.MonthlyRevenueDto;
import com.vitaminBar.customerOrder.financialManagement.model.DailyRevenue;
import com.vitaminBar.customerOrder.financialManagement.model.MonthlyRevenue;
import com.vitaminBar.customerOrder.ordersManagement.dao.CompletedOrderRepository;
import com.vitaminBar.customerOrder.financialManagement.dao.DailyRevenueRepo;
import com.vitaminBar.customerOrder.financialManagement.dao.MonthlyRevenueRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FinancialManagementImpl implements FinancialManagement {

    @Autowired
    DailyRevenueRepo dailyRevenueRepo;

    @Autowired
    CompletedOrderRepository completedOrderRepository;

    @Autowired
    MonthlyRevenueRepo monthlyRevenueRepo;


    @Transactional
    @Scheduled(cron = "00 59 23 * * ? ")
    public void calculateDailyRevenue() {
        System.out.println("calculated");
        dailyRevenueRepo.calculateDailyRevenue();
    }

    @Transactional
    @Scheduled(cron = "00 59 23 L * ?")
    public void calculateMonthlyRevenue() {
        System.out.println("calculated");
        dailyRevenueRepo.calculateMonthlyRevenue();
    }

    public List<DailyRevenueDto> toDailyRevenuesDto(List<DailyRevenue> dailyRevenues) {
        return dailyRevenues.stream()
                .map(dailyRevenue -> new DailyRevenueDto(dailyRevenue.getDate(), dailyRevenue.getTotalRevenue()))
                .collect(Collectors.toList());
    }

    public List<MonthlyRevenueDto> toMonthLyRevenuesDto(List<MonthlyRevenue> monthlyRevenues) {
        return monthlyRevenues.stream()
                .map(monthlyRevenue -> new MonthlyRevenueDto(monthlyRevenue.getMonth(), monthlyRevenue.getMonthlyRevenue()))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<DailyRevenueDto> getAllDailyRevenue() {
        List<DailyRevenue> dailyRevenues = dailyRevenueRepo.findAll();
        return toDailyRevenuesDto(dailyRevenues);
    }

    @Transactional
    public List<MonthlyRevenueDto> getAllMonthlyRevenue() {
        List<MonthlyRevenue> monthlyRevenues = monthlyRevenueRepo.findAll();
        return toMonthLyRevenuesDto(monthlyRevenues);
    }

    @Transactional
    public DailyRevenueDto getASpecificDateRevenue(Date date) {
        DailyRevenue revenue = dailyRevenueRepo.getThisDateRevenue(date);
        return new DailyRevenueDto(revenue.getDate(), revenue.getTotalRevenue());
    }

    @Transactional
    public MonthlyRevenueDto getASpecificMonthRevenue(String month) {
        MonthlyRevenue monthlyRevenue = monthlyRevenueRepo.getMonthRevenue(month);
        return new MonthlyRevenueDto(monthlyRevenue.getMonth(), monthlyRevenue.getMonthlyRevenue());
    }


}
