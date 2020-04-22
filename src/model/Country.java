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
public class Country {
    private int countryID;
    private String country;

// constructor
    public Country(int countryID, String country) {
        this.countryID = countryID;
        this.country = country;
    }


// getters and setters     
    public int getCountryID() {
        return countryID;
    }

    public String getCountry() {
        return country;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    
    
}

