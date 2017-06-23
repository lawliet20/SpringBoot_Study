package com.wwj.config.batchJob.domain;

import java.io.Serializable;

/**
 * 账单
 * Created by sherry on 2017/6/8.
 */
public class Bill implements Serializable{

    private long id;
    private User user;
    private double fees;
    private Double paidFees;
    private Double unpaidFees;
    private int payStatus;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getFees() {
        return fees;
    }

    public void setFees(double fees) {
        this.fees = fees;
    }

    public Double getPaidFees() {
        return paidFees;
    }

    public void setPaidFees(Double paidFees) {
        this.paidFees = paidFees;
    }

    public Double getUnpaidFees() {
        return unpaidFees;
    }

    public void setUnpaidFees(Double unpaidFees) {
        this.unpaidFees = unpaidFees;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }
}
