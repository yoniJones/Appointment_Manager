/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author yonij
 */
public class DBConnection {
    // JDBC connection variables for URL
    public static final String protocol = "jdbc";
    public static final String vendorName = ":mysql:";
    public static final String ipAddress = "//3.227.166.251/U06oV8";
    
    //URL for jdbc
    public static final String jdbcURL = protocol + vendorName + ipAddress;
    
    //driver and connection reference
    public static final String MYSQLjdbcDriver = "com.mysql.jdbc.Driver";
    public static Connection conn = null;
    
    //username and password
    public static final String userName = "U06oV8";
    public static final String password = "53688827144";
    
    // insert sql statements
    public static String insertAddress = "INSERT INTO address(addressId, address, address2, "
            + "cityId, postalCode, phone, createdBy, lastUpdate, lastUpdateBy)" + "VALUES(";
    public static String insertCountry = "INSERT INTO country(countryId, country)VALUES(";
    public static String insertCity = "INSERT INTO city(cityId, city, countryId) VALUES(";
    public static String insertCustomer = "INSERT INTO customer(customerId, customerName, addressId) VALUES(";
    
    // delete sql statements
    public static String deleteCustomer = "DELETE FROM customer WHERE customerId =";
    public static String deleteAddress = "DELETE FROM address WHERE addressId =";
    public static String deleteCity = "DELETE FROM city WHERE cityId =";
    public static String deleteCountry = "DELETE FROM country WHERE countryId =";
    
    public static Connection strartConnection(){
        try{
           Class.forName(MYSQLjdbcDriver);
           conn = (Connection)DriverManager.getConnection(jdbcURL, userName, password);
           System.out.println("connection succesfull");
        }
        catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        
        return conn;
    }
    // close database connection
    public static void closeConnection(){
        try{
            conn.close();
            System.out.println("Connection closed!");
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        
    }
   // insert a new address into address table
   public void insertIntoAddress(int addressID, String address, String address2, int cityID, String zip, String phone){
       try{
           Statement stmt = conn.createStatement();
           insertAddress +=  String.valueOf(addressID) + "," +  address + "," + address2 + "," + String.valueOf(cityID)
                   + "," + zip + "," + phone + ";)";
           stmt.executeUpdate(insertAddress);
       }catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
   }
   // insert methods
   public void insertIntoCountry(int id, String name){
       try{
           Statement stmt = conn.createStatement();
           insertCountry += String.valueOf(id) + "," + name + ");";
           stmt.executeUpdate(insertCountry);
       }catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
       
   }
   // from here down, not used at the moment
   public void insertIntoCity(int cityID, String name, int countryID){
       try{
        Statement stmt = conn.createStatement();
        insertCity += String.valueOf(cityID) + "," + name + "," + String.valueOf(countryID) + ");";
        stmt.executeUpdate(insertCity);
       }catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }  
   }
   
   public void inserCustomer(int customerID, String name, int addressID){
       try{
        Statement stmt = conn.createStatement();
        insertCustomer += String.valueOf(customerID) + "," + name + "," + String.valueOf(addressID) + ");";
        stmt.executeUpdate(insertCustomer);
       }catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }      
   }
  // delete sql methods 
   public void deleteCustomer(int customerID){
       try{
           Statement stmt = conn.createStatement();
           deleteCustomer += String.valueOf(customerID) + ";";
           stmt.executeUpdate(deleteCustomer);
       }catch (SQLException ex){
           System.out.println("Error: " + ex.getMessage());
       }
   }
   
   public void deleteAddress(int addressID){
       try{
           Statement stmt = conn.createStatement();
           deleteAddress += String.valueOf(addressID) + ";";
           stmt.executeUpdate(deleteAddress);
       }catch (SQLException ex){
           System.out.println("Error: " + ex.getMessage());
       }
   }
   public void deleteCity(int cityID){
       try{
           Statement stmt = conn.createStatement();
           deleteCity += String.valueOf(cityID) + ";";
           stmt.executeUpdate(deleteCity);
       }catch (SQLException ex){
           System.out.println("Error: " + ex.getMessage());
       }
   }
   public void deleteCountry(int countryID){
       try{
           Statement stmt = conn.createStatement();
           deleteCountry += String.valueOf(countryID) + ";";
           stmt.executeUpdate(deleteCountry);
       }catch (SQLException ex){
           System.out.println("Error: " + ex.getMessage());
       }
   }

}
