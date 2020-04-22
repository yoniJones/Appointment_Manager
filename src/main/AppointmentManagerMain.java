/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Address;
import model.Appointment;
import model.City;
import model.Country;
import model.Customer;
import model.Records;
import utils.DBConnection;
import static utils.DBConnection.conn;

/**
 *
 * @author yonij
 */
public class AppointmentManagerMain extends Application {
    Customer cus = new Customer();
    Appointment apt = new Appointment();
    Records rec = new Records();
   
    
    @Override
    public void start(Stage stage) throws Exception {
        Records rec = new Records();
        addData(rec);
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
        view.LoginController controller = new view.LoginController(rec);
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
                
    }
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try{
            //open connection to database
            DBConnection.strartConnection();
            
            launch(args);
            // close connection to database
            DBConnection.closeConnection(); 
        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }

    }
    
    public void addData(Records r){
        try {
            // statement object
            Statement stmt = conn.createStatement();
            
            // sql statement for selecting all fields from appointment tabel
            String sqlStatementAppointment = "SELECT * FROM appointment;";
            // sql statment for selecting all fields from Customer table
            String sqlStatementCustomer ="SELECT * FROM customer;";
            // sql statment for selecting all fields from city table
            String sqlStatementCity = "SELECT * FROM city;";
             // sql statment for selecting all fields from country table
            String sqlStatementCountry = "SELECT * FROM country;";
             // sql statment for selecting all fields from address table            
            String sqlStatementAddress = "SELECT * FROM address;";
            

            // result set and execute statement for the appointment table
            ResultSet resultApt = stmt.executeQuery(sqlStatementAppointment);
            while(resultApt.next()){

                int aptID = resultApt.getInt("appointmentId");
                int custID = resultApt.getInt("customerId");
                int userID = resultApt.getInt("userId");
                String tital = resultApt.getString("title");
                String location = resultApt.getString("location");
                String description = resultApt.getString("description");
                String contact = resultApt.getString("contact");
                String type = resultApt.getString("type");
                
                // converting the timezone to local        
                Timestamp start = convertTimeZones(resultApt.getTimestamp("start"));
                
                Timestamp end = convertTimeZones(resultApt.getTimestamp("end"));
               
                // adding the appointment to list
                r.addAppointment(new Appointment(aptID, custID, userID, tital, location, contact, type, description, start, end)); 
            }
            // result set and execute statement for the customer table
            ResultSet resultCust = stmt.executeQuery(sqlStatementCustomer);
            while(resultCust.next()){
                int custId = resultCust.getInt("customerId");
                String name = resultCust.getString("customerName");
                int addressId = resultCust.getInt("addressId");
                r.addCustomer(new Customer(custId, name, addressId));
            }
       
            // result set and execute statement for the address table
            ResultSet resultAddress = stmt.executeQuery(sqlStatementAddress);
            while(resultAddress.next()){
                int addressID = resultAddress.getInt("addressId");
                String address = resultAddress.getString("address");
                String address2 = resultAddress.getString("address2");
                int cityID = resultAddress.getInt("cityId");
                String postalCode = resultAddress.getString("postalCode");
                String phone = resultAddress.getString("phone");
                r.addAddress(new Address(cityID, addressID, address, address2, postalCode, phone));
            }
            
            // result set and execute statement for the city table
            ResultSet resultCity = stmt.executeQuery(sqlStatementCity);
            while(resultCity.next()){
                int cityId = resultCity.getInt("cityId");
                String city = resultCity.getString("city");
                int countryID = resultCity.getInt("countryId");
                r.addCity(new City(cityId, city, countryID));
            }
            // result set and execute statement for the country table
            ResultSet resultCountry = stmt.executeQuery(sqlStatementCountry);
            while(resultCountry.next()){
                int countryId = resultCountry.getInt("countryId");
                String country = resultCountry.getString("country");
                r.addCountry(new Country(countryId, country));
            }
            
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
    
    // sets the the Timestamp to UTC and returns return a timestamp that is converted to
    // the systems default time zone
    public Timestamp convertTimeZones(Timestamp tStamp){
              
                // converting the timestamp into a LocalDateTime
                LocalDateTime ldtStart = tStamp.toLocalDateTime();   
               //Setting the zoneId to UTC
		ZoneId zid = ZoneId.of("UTC");
                // setting the zoneID value from the resultSet to UTC
                ZonedDateTime zdtStart = ldtStart.atZone(zid);
		
                //Converting UTC time to the systems default timezone
                ZonedDateTime utcStart = zdtStart.withZoneSameInstant(ZoneId.systemDefault());
		
                // parsing the zonedDateTime to a TimeStamp
                tStamp = Timestamp.valueOf(utcStart.toLocalDateTime());
                return tStamp;
    }
    
}
