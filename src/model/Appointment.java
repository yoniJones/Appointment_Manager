/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author yonij
 */
public class Appointment {
    private int appointmentID;
    private int userID;
    private int customerID;
    
    private String title;
    private String location;
    private String contact;
    private String type;
    private String description;

    private Timestamp start;
    private Timestamp end;

// constructor
    public Appointment(int appointmentID, int customerID, int userID, String title, String location, String contact, String type, String description, Timestamp start, Timestamp end) {
        this.appointmentID = appointmentID;
        this.userID = userID;
        this.customerID = customerID;
        this.title = title;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.description = description;
       // this.url = url;
        this.start = new java.sql.Timestamp(start.getTime());
        this.end = new java.sql.Timestamp(end.getTime());
       /// this.createDate = new java.sql.Timestamp(createDate.getTime());
        //this.createdBy = createdBy;
       // this.lastUpdatedBy = lastUpdatedBy;
        //this.lastUpdate = new java.sql.Timestamp(lastUpdate.getTime());
    }

    // empty constructor
    public Appointment() {
    }

    // getters and setters
    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        this.start = new java.sql.Timestamp(start.getTime());
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = new java.sql.Timestamp(end.getTime());
    }
    
    
}
