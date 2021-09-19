package customerOrder.financialManagement.dao;

import customerOrder.financialManagement.dto.DailyRevenueDto;
import customerOrder.financialManagement.model.DailyRevenue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

@Component
@Repository
public interface DailyRevenueRepo extends JpaRepository<DailyRevenue, Long> {
    //    @Query(value = "Select * from daily_revenue WHERE EXTRACT(MONTH FROM order_date) =:monthInNumber")
//    List<DailyRevenue> dailyRevenueOfAMonth(@Param("monthInNumber") int monthInNumber);
    @Transactional
    @Procedure(procedureName = "calculate_daily_revenue")
    //@Query(value = "call calculate_daily_revenue()", nativeQuery = true)
    void calculateDailyRevenue();

    @Transactional
    @Procedure(procedureName = "calculate_monthly_revenue")
    void calculateMonthlyRevenue();

    @Query(value = "Select * from daily_revenue where date(date_released) =:date", nativeQuery = true)
    DailyRevenue getThisDateRevenue(@Param("date") Date date);
}
