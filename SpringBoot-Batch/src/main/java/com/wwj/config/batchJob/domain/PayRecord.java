package com.wwj.config.batchJob.domain;

import java.io.Serializable;

/*
* 扣费
* */
public class PayRecord implements Serializable{
    private String id;
	private Bill bill;
	private Double paidFees;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Bill getBill() {return bill;}
	public void setBill(Bill bill) {this.bill = bill;}
	public Double getPaidFees() {return paidFees;}
	public void setPaidFees(Double paidFees) {this.paidFees = paidFees;}

	@Override
	public String toString() {
		return "PayRecord{" +
				"id='" + id + '\'' +
				", bill=" + bill +
				", paidFees=" + paidFees +
				'}';
	}
}