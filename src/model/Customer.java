/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Calendar;

/**
 *
 * @author yonij
 */
public class Customer {
    private int customerID;
    private String customerName;
    private int addressID;
//    private Calendar createDate;
//    private String createdBy;
//    private String lastUpdatedBy;
//    private Calendar lastUpdate;

    public Customer(int customerID, String customerName, int addressID) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.addressID = addressID;
//        this.createDate = createDate;
//        this.createdBy = createdBy;
//        this.lastUpdatedBy = lastUpdatedBy;
//        this.lastUpdate = lastUpdate;
    }

    public Customer() {
    }
    

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getAddressID() {
        return addressID;
    }

    public void setAddressID(int addressID) {
        this.addressID = addressID;
    }

//    public Calendar getCreateDate() {
//        return createDate;
//    }
//
//    public void setCreateDate(Calendar createDate) {
//        this.createDate = createDate;
//    }
//
//    public String getCreatedBy() {
//        return createdBy;
//    }
//
//    public void setCreatedBy(String createdBy) {
//        this.createdBy = createdBy;
//    }
//
//    public String getLastUpdatedBy() {
//        return lastUpdatedBy;
//    }
//
//    public void setLastUpdatedBy(String lastUpdatedBy) {
//        this.lastUpdatedBy = lastUpdatedBy;
//    }
//
//    public Calendar getLastUpdate() {
//        return lastUpdate;
//    }
//
//    public void setLastUpdate(Calendar lastUpdate) {
//        this.lastUpdate = lastUpdate;
//    }
    
    

}
