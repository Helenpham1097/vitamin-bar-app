package com.vitaminBar.customerOrder.financialManagement.model;

import javax.persistence.*;

@Entity
@Table(name = "monthly_revenue")
public class MonthlyRevenue {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    long id;

    @Column(name = "month_released")
    String month;

    @Column(name = "total_revenue")
    double monthlyRevenue;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public double getMonthlyRevenue() {
        return monthlyRevenue;
    }

    public void setMonthlyRevenue(double monthlyRevenue) {
        this.monthlyRevenue = monthlyRevenue;
    }
}
