package com.vitaminBar.customerOrder.ordersManagement.model;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long customer_id;

    @Column(name = "name")
    private String name;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @Column(name = "point")
    private int point;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "customer")
    private List<Order> orderList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "customer")
    private List<CompletedOrder> completedOrders = new ArrayList<>();


    public void addOrder(Order order) {
        this.orderList.add(order);
        order.setCustomer(this);
    }

    public void addOrders(List<Order> orders) {
        this.orderList.addAll(orders);
        orders.forEach(order->order.setCustomer(this));
    }

    public void removeOrder(Order order) {
        order.setCustomer(null);
        this.orderList.remove(order);
    }

    public void deleteOrders() {
        Iterator<Order> listFound = this.orderList.iterator();
        while (listFound.hasNext()) {
            Order order = listFound.next();
            order.setCustomer(null);
            this.orderList.remove(order);
        }
    }

    public void addCompletedOrder(CompletedOrder completedOrder) {
        this.completedOrders.add(completedOrder);
        completedOrder.setCustomer(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public long getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(long customer_id) {
        this.customer_id = customer_id;
    }

    public List<CompletedOrder> getCompletedOrders() {
        return completedOrders;
    }

    public void setCompletedOrders(List<CompletedOrder> completedOrders) {
        this.completedOrders = completedOrders;
    }
}
