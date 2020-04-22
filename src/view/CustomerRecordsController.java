/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.io.IOException;
import java.sql.Timestamp;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Address;
import model.City;
import model.Country;
import model.Customer;
import model.Records;
import static utils.DBConnection.conn;
import utils.SQLinteractivity;

/**
 * FXML Controller class
 *
 * @author yonij
 */
public class CustomerRecordsController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private TextField phoneTxt;

    @FXML
    private TextField nameTxt;

    @FXML
    private TextField addressTxt;

    @FXML
    private TextField Address2Txt;

    @FXML
    private TextField cityTxt;

    @FXML
    private TextField countryTxt;
    
    private Records r;


    @FXML
    private TextField zipTxt;

    

    String countryName, cityName, stName, stName2, cusName, phone, zip;
    int cityId, countryId, addId, cusId;
    boolean update;

    Date date = new Date();
    Timestamp ts = new Timestamp(System.currentTimeMillis());

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @FXML
    private TableView<Customer> customerTlv;

    @FXML
    void back(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
            view.MenuController controller = new MenuController(r);
            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void delete(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Are you sure you want to delete this customer?");
        alert.showAndWait().ifPresent((response -> {
            if (response == ButtonType.OK) {
                        try {
            Customer selected = customerTlv.getSelectionModel().getSelectedItem();
            if (selected == null) {
                errorWindow(1);
                return;
            } else {
                cusId = selected.getCustomerID();

                String sqlDeleteApp = "DELETE FROM appointment WHERE customerId =" + String.valueOf(cusId) + ";";
                String sqlDeleteCus = "DELETE FROM customer WHERE customerId =" + String.valueOf(cusId) + ";";
                customerTlv.refresh();

                try {
                    PreparedStatement ps = conn.prepareStatement(sqlDeleteApp); // deleting the appointment that is assoisiatetes with the customer
                    ps.execute();
                    ps.executeUpdate();
                    r.deleteAllAppointmentsForCustomer(cusId);
                } catch (SQLException ex) {
                    System.out.println("Error 6: " + ex.getMessage());
                }
                try {
                    PreparedStatement ps = conn.prepareStatement(sqlDeleteCus);
                    ps.execute();
                    ps.executeUpdate();
                    r.deleteCustomer(cusId);
                } catch (SQLException ex) {
                    System.out.println("Error 5: " + ex.getMessage());
                }

            }
        } catch (Exception e) {
            return;
        }
            }
        }));

    }

    @FXML
    void selectedCustomerBtn(MouseEvent event) {
        update = true;
        try {

            Customer selected = customerTlv.getSelectionModel().getSelectedItem();
            if (selected == null) {
                errorWindow(1);
            } else {
                
                // used for testing if user updated
                addId = selected.getAddressID();
                cityId = r.getCityID(addId);
                countryName = r.getcountryName(r.getCityID(addId));
                cityName = r.getCityName(cityId);
                stName = r.getaddress(selected.getCustomerID());
                stName2 = r.getaddress2(addId);
                cusName = selected.getCustomerName();
                phone = r.getCustomerPhone(selected.getCustomerID());
                zip = r.getpostalCode(addId);

                cusId = selected.getCustomerID();

                // setting the text in the textField if user clicks on a customer in the table
                nameTxt.setText(selected.getCustomerName());
                phoneTxt.setText(r.getCustomerPhone(selected.getCustomerID()));
                addressTxt.setText(r.getaddress(selected.getCustomerID()));
                Address2Txt.setText(r.getaddress2(selected.getAddressID()));
                cityTxt.setText(cityName);
                zipTxt.setText(r.getpostalCode(selected.getAddressID()));
                countryTxt.setText(r.getcountryName(r.getCityID(selected.getAddressID())));
            }
        } catch (Exception e) {
            return;
        }

    }
// clear all entered data
    @FXML
    void clear(MouseEvent event) {
        update = false;
        nameTxt.setText("");
        phoneTxt.setText("");
        addressTxt.setText("");
        Address2Txt.setText("");
        cityTxt.setText("");
        zipTxt.setText("");
        countryTxt.setText("");
    }

    @FXML
    void update(MouseEvent event) {

        if (update == false) {
            return;
        }
        if (validateInput()) {
            // if the customer name was changed
            if (cusName.toLowerCase().trim() != nameTxt.getText().toLowerCase().trim()) {

                //sql statement
                String sqlUpdateCustomer = "UPDATE customer SET customerName ='" + nameTxt.getText() + "', lastUpdate = '" + df.format(ts)
                        + "' WHERE customerId =" + String.valueOf(cusId) + ";";
                try {
                    // executing sql statement and updating the database
                    PreparedStatement ps = conn.prepareStatement(sqlUpdateCustomer);
                    ps.execute();
                    ps.executeUpdate();

                    // updating the observableList
                    r.updateCustomerName(nameTxt.getText(), cusId);

                } catch (SQLException ex) {
                    System.out.println("Error 6: " + ex.getMessage());
                }
            }
            // update if any changes were made to the address
            if (stName.toLowerCase().trim() != addressTxt.getText().trim().toLowerCase()
                    || stName2.toLowerCase().trim() != Address2Txt.getText().trim().toLowerCase()
                    || zip.trim() != zipTxt.getText().trim() || phone.toLowerCase().trim() != phoneTxt.getText().toLowerCase().trim()) {

                String sqlUpdateAddress = "UPDATE address SET address ='" + addressTxt.getText() + "', address2 ='"
                        + Address2Txt.getText() + "',postalCode ='" + zipTxt.getText() + "', phone ='" + phoneTxt.getText()
                        + "', lastUpdate = '" + df.format(ts) + "' WHERE addressId = " + String.valueOf(addId) + ";";
                try {
                    // update the address in the database
                    PreparedStatement ps = conn.prepareStatement(sqlUpdateAddress);
                    ps.execute();
                    ps.executeUpdate();

                    // Update the address in the ubservableList
                    r.updateAddress(new Address(cityId, addId, addressTxt.getText(), Address2Txt.getText(),
                            zipTxt.getText(), phoneTxt.getText()));

                } catch (SQLException ex) {
                    System.out.println("Error 8: " + ex.getMessage());
                }

            }

            // update if user makes any changes to the city name
            if (cityName.toLowerCase().trim() != cityTxt.getText().toLowerCase().trim()) {

                String sqlUpdateCity = "UPDATE city SET city = '" + cityTxt.getText() + "', lastUpdate = '"
                        + df.format(ts) + "'WHERE cityId = " + String.valueOf(cityId).trim() + ";";

                try {
                    // update the address in the database
                    PreparedStatement ps = conn.prepareStatement(sqlUpdateCity);
                    ps.execute();
                    ps.executeUpdate();

                    // Update the city in the ubservableList
                    r.updateCity(cityTxt.getText(), cityId);

                } catch (SQLException ex) {
                    System.out.println("Error 8: " + ex.getMessage());
                }

            }

            // if the country was updated
            if (countryName.toLowerCase().trim() != countryTxt.getText().toLowerCase().trim()) {

                // retrieving the country ID 
                countryId = r.getCountryID(cityId);
                // sql statement
                String sqlUpdateCountry = "UPDATE country SET country = '" + countryTxt.getText() + "', lastUpdate = '"
                        + df.format(ts) + "' WHERE countryId = " + String.valueOf(countryId) + ";";
                try {
                    // updating the database
                    PreparedStatement ps = conn.prepareStatement(sqlUpdateCountry);
                    ps.execute();
                    ps.executeUpdate();

                    // updating the observableList
                    r.updateCountry(countryTxt.getText(), countryId);

                } catch (SQLException ex) {
                    System.out.println("Error 9: " + ex.getMessage());
                }
            }
            customerTlv.refresh();
            update = false;
        }

    }

    @FXML
    void createNew(MouseEvent event) {
        if (update == true) {
            errorWindow(8);
            return;
        }

        if (validateInput()) {

            // adding the new country
            String query = "INSERT INTO country(country, createDate, createdBy, lastUpdate, lastUpdateBy) "
                    + "VALUES ('" + countryTxt.getText() + "','" + df.format(ts) + "','test','" + df.format(ts) + "','test');";

            try {
                PreparedStatement ps = conn.prepareStatement(query);
                ps.execute();
                ps = conn.prepareStatement("SELECT LAST_INSERT_ID() FROM country");
                ResultSet rs = ps.executeQuery();
                rs.next();
                countryId = rs.getInt(1);

                r.addCountry(new Country(countryId, countryTxt.getText())); // adding new country to the abservable list for countries

            } catch (SQLException ex) {
                System.out.println("Error 1: " + ex.getMessage());
            }

            // adding the new city
            query = "INSERT INTO city(city, countryId, createDate, createdBy, lastUpdate,lastUpdateBy) "
                    + "VALUES('" + cityTxt.getText() + "'," + String.valueOf(countryId) + ",'" + df.format(ts)
                    + "','test','" + df.format(ts) + "','test');";
            try {
                PreparedStatement ps = conn.prepareStatement(query);
                ps.execute();
                ps = conn.prepareStatement("SELECT LAST_INSERT_ID() FROM city");
                ResultSet rs = ps.executeQuery();
                rs.next();
                cityId = rs.getInt(1);

                r.addCity(new City(cityId, cityTxt.getText(), countryId)); // adding new city to the abservable list for cities

            } catch (SQLException ex) {
                System.out.println("Error 2: " + ex.getMessage());
            }

            // adding the new address
            query = "INSERT INTO address(address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy)"
                    + "VALUES ( '" + addressTxt.getText() + "','" + Address2Txt.getText() + "'," + String.valueOf(cityId) + ",'"
                    + zipTxt.getText() + "','" + phoneTxt.getText() + "','" + df.format(ts) + "','test','" + df.format(ts) + "','test');";
            try {
                PreparedStatement ps = conn.prepareStatement(query);
                ps.execute();
                ps = conn.prepareStatement("SELECT LAST_INSERT_ID() FROM address");
                ResultSet rs = ps.executeQuery();
                rs.next();
                addId = rs.getInt(1);

                r.addAddress(new Address(cityId, addId, addressTxt.getText(), Address2Txt.getText(),
                        zipTxt.getText(), phoneTxt.getText())); // adding new address to the abservable list for addresses

            } catch (SQLException ex) {
                System.out.println("Error 3: " + ex.getMessage());
            }

            // adding the new customer
            query = "INSERT INTO customer(customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy)"
                    + " values( '" + nameTxt.getText() + "'," + String.valueOf(addId) + "," + String.valueOf(1) + ",'" + df.format(ts)
                    + "','test','" + df.format(ts) + "','test');";
            try {
                PreparedStatement ps = conn.prepareStatement(query);
                ps.execute();
                ps = conn.prepareStatement("SELECT LAST_INSERT_ID() FROM customer");
                ResultSet rs = ps.executeQuery();
                rs.next();
                cusId = rs.getInt(1);

                r.addCustomer(new Customer(cusId, nameTxt.getText(), addId)); // adding new address to the abservable list for addresses

            } catch (SQLException ex) {
                System.out.println("Error 4: " + ex.getMessage());
            }
            customerTlv.refresh();
        }
    }

    // validate user input
    public boolean validateInput() {
        int zip;

        if (nameTxt.getText().isEmpty() || nameTxt.getText() == null) {
            errorWindow(3);
            return false;
        }
        if (phoneTxt.getText().isEmpty() || phoneTxt.getText() == null) {
            errorWindow(4);
            return false;
        }
        if (addressTxt.getText().isEmpty() || addressTxt.getText() == null) {
            errorWindow(5);
            return false;
        }
        if (Address2Txt.getText().isEmpty() || Address2Txt.getText() == null) {
            String add2 = " "; // address2 is optional
        }
        if (cityTxt.getText().isEmpty() || cityTxt.getText() == null) {
            errorWindow(6);
            return false;
        }
        if (countryTxt.getText().isEmpty() || countryTxt.getText() == null) {
            errorWindow(7);
            return false;
        }
         try {
            zip = Integer.parseInt(zipTxt.getText());
        } catch (NumberFormatException e) {
            errorWindow(2);
            return false;
        }
        return true;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        customerTlv.setItems(r.getAllCustomers());
                

    }

    public CustomerRecordsController(Records customers) {
        this.r = customers;
        update = false;
    }

    public void errorWindow(int i) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (i == 1) {
            alert.setTitle("No selection!");
            alert.setHeaderText(null);
            alert.setContentText("Select an appointment!");
            alert.showAndWait();
        }
        if (i == 2) {
            alert.setTitle("Incurrect data entry");
            alert.setHeaderText(null);
            alert.setContentText("Enter integers for customer id, city id, address id, zip, and country id!");
            alert.showAndWait();
        }
        if (i == 3) {
            alert.setTitle("Data missing!");
            alert.setHeaderText(null);
            alert.setContentText("Must enter customer name!");
            alert.showAndWait();
        }
        if (i == 4) {
            alert.setTitle("Data missing!");
            alert.setHeaderText(null);
            alert.setContentText("Must enter phone number!");
            alert.showAndWait();
        }
        if (i == 5) {
            alert.setTitle("Data missing!");
            alert.setHeaderText(null);
            alert.setContentText("Must enter an address!");
            alert.showAndWait();
        }
        if (i == 6) {
            alert.setTitle("Data missing!");
            alert.setHeaderText(null);
            alert.setContentText("Must enter city name!");
            alert.showAndWait();
        }
        if (i == 7) {
            alert.setTitle("Data missing!");
            alert.setHeaderText(null);
            alert.setContentText("Must enter country name!");
            alert.showAndWait();
        }
        if (i == 8) {
            alert.setTitle("Duplicate!");
            alert.setHeaderText(null);
            alert.setContentText("This customer already exist on record!");
            alert.showAndWait();
        }
    }

}
