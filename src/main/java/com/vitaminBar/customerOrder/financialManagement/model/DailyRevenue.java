package com.vitaminBar.customerOrder.financialManagement.model;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "daily_revenue")
public class DailyRevenue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "date_released")
    Date date;

    @Column(name = "total_revenue")
    double totalRevenue;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}
