package com.vitaminBar.customerOrder.financialManagement.dao;

import com.vitaminBar.customerOrder.financialManagement.model.MonthlyRevenue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MonthlyRevenueRepo extends JpaRepository<MonthlyRevenue, Long> {

    @Query(value = "select * from monthly_revenue m where m.month_released =:month", nativeQuery = true)
    MonthlyRevenue getMonthRevenue (@Param("month") String month);
}
