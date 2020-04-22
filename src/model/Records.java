/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author yonij
 */
public class Records {

    private ArrayList<Appointment> appointment;
    private ArrayList<Customer> customer;
    private ArrayList<Address> address;
    private ArrayList<City> city;
    private ArrayList<Country> country;

    private ObservableList<Appointment> allAppointments;
    private ObservableList<Appointment> filteredAppointments; // used to hold filtered appointments by week/month
    private ObservableList<Customer> allCustomers;
    private ObservableList<Address> allAddresses;
    private ObservableList<City> allCities;
    private ObservableList<Country> allCountries;

    public Records() {
        appointment = new ArrayList<>();
        customer = new ArrayList<>();
        address = new ArrayList<>();
        city = new ArrayList<>();
        country = new ArrayList<>();
        allAppointments = FXCollections.observableList(appointment);
        filteredAppointments = FXCollections.observableList(appointment);
        allCustomers = FXCollections.observableList(customer);
        allAddresses = FXCollections.observableList(address);
        allCities = FXCollections.observableList(city);
        allCountries = FXCollections.observableList(country);
    }

    public ArrayList<Address> getAddress() {
        return address;
    }

    public void setAddress(ArrayList<Address> address) {
        this.address = address;
    }

    public ArrayList<City> getCity() {
        return city;
    }

    public void setCity(ArrayList<City> city) {
        this.city = city;
    }

    public ArrayList<Country> getCountry() {
        return country;
    }

    public void setCountry(ArrayList<Country> country) {
        this.country = country;
    }

    public ObservableList<Address> getAllAddresses() {
        return allAddresses;
    }

    public void setAllAddresses(ObservableList<Address> allAddresses) {
        this.allAddresses = allAddresses;
    }

    public ObservableList<City> getAllCities() {
        return allCities;
    }

    public void setAllCities(ObservableList<City> allCities) {
        this.allCities = allCities;
    }

    public ObservableList<Country> getAllCountries() {
        return allCountries;
    }

    public void setAllCountries(ObservableList<Country> allCountries) {
        this.allCountries = allCountries;
    }

    public ArrayList<Appointment> getAppointment() {
        return appointment;
    }

    public void setAppointment(ArrayList<Appointment> appointment) {
        this.appointment = appointment;
    }

    public ArrayList<Customer> getCustomer() {
        return customer;
    }

    public void setCustomer(ArrayList<Customer> customer) {
        this.customer = customer;
    }

    public ObservableList<Appointment> getAllAppointments() {
        return allAppointments;
    }

    public void setAllAppointments(ObservableList<Appointment> allAppointments) {
        this.allAppointments = allAppointments;
    }

    public ObservableList<Customer> getAllCustomers() {
        return allCustomers;
    }

    public void setAllCustomers(ObservableList<Customer> allCustomers) {
        this.allCustomers = allCustomers;
    }

    public void addAppointment(Appointment a) {
        if (a != null) {
            appointment.add(a);

        }
    }

    public void addCustomer(Customer c) {
        if (c != null) {
            customer.add(c);

        }
    }

    public void addCity(City c) {
        if (c != null) {
            city.add(c);

        }
    }

    public void addAddress(Address a) {
        if (a != null) {
            address.add(a);

        }
    }

    public void addCountry(Country c) {
        if (c != null) {
            country.add(c);

        }
    }
    // return the name and address for a customer 

    public String getCustumerInfo(int id) {
        String r;
        for (int i = 0; i < allCustomers.size(); i++) {
            if (id == allCustomers.get(i).getCustomerID()) {
                r = (" Name: " + allCustomers.get(i).getCustomerName());
                for (int d = 0; d < allAddresses.size(); d++) {
                    if (allCustomers.get(i).getAddressID() == allAddresses.get(d).getAddressID()) {
                        r += (" Phone: " + allAddresses.get(d).getPhone() + " Address: " + allAddresses.get(d).getAddress());
                        for (int c = 0; c < allCities.size(); c++) {
                            if (allAddresses.get(d).getCityID() == allCities.get(c).getCityID()) {
                                r += (" " + allCities.get(c).getCity() + " ");
                                for (int s = 0; s < allCountries.size(); s++) {
                                    if (allCities.get(c).getCountryID() == allCountries.get(s).getCountryID()) {
                                        r += allCountries.get(s).getCountry();
                                        return r;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return "";
    }

    public String getCustomerPhone(int id) {
        for (int i = 0; i < allAddresses.size(); i++) {
            if (allAddresses.get(i).getAddressID() == id) {
                return allAddresses.get(i).getPhone();
            }
        }
        return " ";
    }

    public String getpostalCode(int id) {
        for (int i = 0; i < allAddresses.size(); i++) {
            if (allAddresses.get(i).getAddressID() == id) {
                return allAddresses.get(i).getPostalCode();
            }
        }
        return " ";
    }

    public String getaddress(int id) {
        for (int i = 0; i < allAddresses.size(); i++) {
            if (allAddresses.get(i).getAddressID() == id) {
                return allAddresses.get(i).getAddress();
            }
        }
        return " ";
    }

    public String getaddress2(int id) {
        for (int i = 0; i < allAddresses.size(); i++) {
            if (allAddresses.get(i).getAddressID() == id) {
                if (allAddresses.get(i).getAddress2().isEmpty() || allAddresses.get(i).getAddress2() == null) {
                    return " ";
                } else {
                    return allAddresses.get(i).getAddress2();
                }
            }
        }
        return " ";
    }

    // gets the city id using the addressId
    public int getCityID(int addId) {
        for (int i = 0; i < allAddresses.size(); i++) {
            if (allAddresses.get(i).getAddressID() == addId) {
                return allAddresses.get(i).getCityID();
            }
        }
        return 0;
    }

    // gets the cityName. used in the add/update customer page
    public String getCityName(int cityId) {
        String n = "";
        for (int i = 0; i < allCities.size(); i++) {
            if (allCities.get(i).getCityID() == cityId) {
                System.out.println("1" + allCities.get(i).getCity());
                n = allCities.get(i).getCity().trim();
                break;
            }
        }
        System.out.println("2" + n);
        return n;
    }

    // gets the country with a city ID input
    public int getCountryID(int id) {
        for (int i = 0; i < allCities.size(); i++) {
            if (allCities.get(i).getCountryID() == id) {
                return allCities.get(i).getCountryID();
            }
        }
        return 0;
    }

    public String getcountryName(int id) {
        for (int i = 0; i < allCountries.size(); i++) {
            if (allCountries.get(i).getCountryID() == id) {
                return allCountries.get(i).getCountry();
            }
        }
        return " ";
    }
// used if you don't want duplicate. currently not used

    public boolean FindIfCountryExist(String name) {
        for (int i = 0; i < allCountries.size(); i++) {
            if (allCountries.get(i).getCountry().trim().toLowerCase() == name.trim().toLowerCase()) {
                return true;
            }
        }
        return false;
    }

    public boolean FindIfCityExist(String name) {
        for (int i = 0; i < allCities.size(); i++) {
            if (allCities.get(i).getCity().trim().toLowerCase() == name.trim().toLowerCase()) {
                return true;
            }
        }
        return false;
    }

// used to avoid duplicates. currently not used    
    public boolean findCountryID(int id) {
        for (int i = 0; i < allCountries.size(); i++) {
            if (allCountries.get(i).getCountryID() == id) {
                return true;
            }
        }
        return false;
    }

    // used to avoid duplicates. currently not used  
    // returns the country id using the country name
    public int FindCountryIdUsingName(String n) {
        for (int i = 0; i < allCountries.size(); i++) {
            if (allCountries.get(i).getCountry().toLowerCase() == n.toLowerCase()) {
                return allCountries.get(i).getCountryID();
            }
        }
        return 1;
    }

    // used to avoid duplicates. currently not used  
    // returns the city id using the city name
    public int FindCityIdUsingName(String n) {
        for (int i = 0; i < allCities.size(); i++) {
            if (allCities.get(i).getCity().toLowerCase().trim() == n.toLowerCase().trim()) {
                return allCities.get(i).getCountryID();
            }
        }
        return 1;
    }

    // updating the an address
    public void updateAddress(Address a) {
        int b = a.getAddressID();
        for (int i = 0; i < allAddresses.size(); i++) {
            if (allAddresses.get(i).getAddressID() == b) {
                allAddresses.get(i).setAddress(a.getAddress());
                allAddresses.get(i).setAddress2(a.getAddress2());
                allAddresses.get(i).setCityID(a.getCityID());
                allAddresses.get(i).setPhone(a.getPhone());
                allAddresses.get(i).setPostalCode(a.getPostalCode());
                return;
            }
        }
    }

    public void updateAppointment(Appointment a) {
        int b = a.getAppointmentID();
        for (int i = 0; i < allAppointments.size(); i++) {
            if (allAppointments.get(i).getAppointmentID() == b) {
                allAppointments.get(i).setContact(a.getContact());
                allAppointments.get(i).setDescription(a.getDescription());
                allAppointments.get(i).setLocation(a.getLocation());
                allAppointments.get(i).setTitle(a.getTitle());
                allAppointments.get(i).setType(a.getType());
                allAppointments.get(i).setStart(a.getStart());
                allAppointments.get(i).setEnd(a.getEnd());
                allAppointments.get(i).setUserID(a.getUserID());
                return;
            }
        }
    }

    public void updateCustomer(Customer c) {
        int id = c.getCustomerID();
        for (int i = 0; i < allCustomers.size(); i++) {
            if (allCustomers.get(i).getCustomerID() == id) {
                allCustomers.get(i).setAddressID(c.getAddressID());
                allCustomers.get(i).setCustomerName(c.getCustomerName());
                return;
            }
        }
    }

    public void updateCustomerName(String name, int id) {
        for (int i = 0; i < allCustomers.size(); i++) {
            if (allCustomers.get(i).getCustomerID() == id) {
                allCustomers.get(i).setCustomerName(name);
                return;
            }
        }
    }

    public void updateCity(String name, int id) {
        for (int i = 0; i < allCities.size(); i++) {
            if (allCities.get(i).getCityID() == id) {
                allCities.get(i).setCity(name);
                return;
            }
        }
    }

    public void updateCountry(String name, int id) {
        for (int i = 0; i < allCountries.size(); i++) {
            if (allCountries.get(i).getCountryID() == id) {
                allCountries.get(i).setCountry(name);
                return;
            }
        }
    }

    // returns customer name with customer id as an input
    public String getCustomerName(int id) {
        for (int i = 0; i < allCustomers.size(); i++) {
            if (id == allCustomers.get(i).getCustomerID()) {
                return allCustomers.get(i).getCustomerName();
            }
        }
        return "";
    }

    // deletes a customer from the observable list
    public void deleteCustomer(int id) {
        for (int i = 0; i < allCustomers.size(); i++) {
            if (id == allCustomers.get(i).getCustomerID()) {
                allCustomers.remove(allCustomers.get(i));
                return;
            }
        }
    }

    // deletes all the appointments for entered custumer ID
    public void deleteAllAppointmentsForCustomer(int cusId) {
        for (int i = 0; i < allAppointments.size(); i++) {
            if (cusId == allAppointments.get(i).getCustomerID()) {
                allAppointments.remove(allAppointments.get(i));
            }
        }
    }

    // deletes an appointment
    public void deleteAppointment(int aptId) {
        for (int i = 0; i < allAppointments.size(); i++) {
            if (aptId == allAppointments.get(i).getAppointmentID()) {
                allAppointments.remove(allAppointments.get(i));
                return;
            }
        }
    }
// used in the filtered appointment page
    public ObservableList<Appointment> getFilteredAppointments() {
        return filteredAppointments;
    }

    public void setFilteredAppointments(ObservableList<Appointment> filteredAppointments) {
        this.filteredAppointments = filteredAppointments;
    }

    public void clearAllFilteredAppointments() {
        filteredAppointments.clear();
    }

    public void addFilteredAppointment(Appointment a) {
        if (a != null) {
            filteredAppointments.add(a);

        }
    }

}
