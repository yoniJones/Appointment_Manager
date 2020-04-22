/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Records;

/**
 NAME: Jonathan Jones
 * Login password:
 * username = u
 * password = p
 */
public class MenuController implements Initializable {

    Records apt;
    
    @FXML
    private Label lblMenu;
    
    
    @FXML
    private Button manageAptBtn;

    @FXML
    private Button aptCalendarBtn;

    @FXML
    private Button customerRecBtn;

    @FXML
    private Button userLogBtn;

    @FXML
            // load calendar page
    void aptCalendar(MouseEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Calendar.fxml"));
            view.CalendarController controller = new CalendarController(apt);
            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }catch(IOException e){
            e.printStackTrace();      
        }
    }

    @FXML // Load customer records page
    void customerRec(MouseEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerRecords.fxml"));
            view.CustomerRecordsController controller = new CustomerRecordsController(apt);
            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }catch(IOException e){
            e.printStackTrace();      
        }
    }

    @FXML // need to delete this! 
    void mannageApt(MouseEvent event) {

    }

    @FXML // load report page
    void report(MouseEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Reports.fxml"));
            view.ReportsController controller = new ReportsController(apt);
            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }catch(IOException e){
            e.printStackTrace();      
        }
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }  

    // constructor
    public MenuController(Records apt) {
        this.apt = apt;
    }
  
    
}
