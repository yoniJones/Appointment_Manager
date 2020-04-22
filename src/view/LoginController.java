/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import static java.time.ZonedDateTime.now;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import model.Records;
import static utils.DBConnection.conn;


/**
 *
 * @author yonij
 */
public class LoginController implements Initializable {

    Records apt;
    // using Hebrew as second language 
    ResourceBundle rb = ResourceBundle.getBundle("languageProperties/Nat_iw", Locale.getDefault());

    public LoginController(Records apt) {
        this.apt = apt;
    }

    
    @FXML
    private TextField UserNameTxt;

    @FXML
    private PasswordField passwordTxt;

    @FXML
    private Label passwordLBL;
    
    @FXML
    private Label amsLbl;

    @FXML
    private Label userNameLbl;
    
    @FXML
    private Button submitBtn;
      
    @FXML
    private Label timelbl;
    
   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-mm-dd 'at' hh:mm a z");
        

    @FXML
    void Submit(ActionEvent event) {
        if(!"u".equals(UserNameTxt.getText()) || !"p".equals(passwordTxt.getText())){
        
            Alert alert = new Alert(Alert.AlertType.ERROR);
            if(Locale.getDefault().getLanguage().equals("iw")){
                alert.setTitle("");
                alert.setContentText(rb.getString("user") + " " + rb.getString("name") + " " + rb.getString("or")
                + " " + rb.getString("password") + " " + rb.getString("incorrect"));
                alert.showAndWait();
                return;
            }else{
                 alert.setTitle("");
                 alert.setContentText("Incorrect username or password");
                 alert.showAndWait();
                 return;
            }
        }else{
                LocalDateTime now = LocalDateTime.now();
                writeLoginToFile(now);
                alertIfUpcomingAppointment();
                try{
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
                    view.MenuController controller = new MenuController(apt);
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
    }
    
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-mm-dd 'at' hh:mm a z");
        ZonedDateTime now = ZonedDateTime.now();
        String dateTimeString = now.format(formatter);  //2019-03-28 14:47:33 PM
        timelbl.setText(dateTimeString);
        if(Locale.getDefault().getLanguage().equals("iw")){
            ResourceBundle hb = ResourceBundle.getBundle("languageProperties/Nat_iw", Locale.getDefault());
            amsLbl.setTextAlignment(TextAlignment.RIGHT);
            amsLbl.setLayoutX(170);
            amsLbl.setText( hb.getString("system") + " " + hb.getString("managing") + " " + hb.getString("appointment"));
            passwordLBL.setText(hb.getString("password"));
            userNameLbl.setText(hb.getString("user" ) + " " + hb.getString("name"));
            submitBtn.setText(hb.getString("enter"));
         
            // C:\Users\yonij\OneDrive\Documents\NetBeansProjects\AppointmentManager\build\classes\reports\report
            
            //System.out.println(rb.getString("hello") + " " +  rb.getString("world"));
        }
    }   
    // used to captue timestamp logins and write to file
    public void writeLoginToFile(LocalDateTime n) {
        
        try {
            // creating a printWriter
            PrintWriter pw = new PrintWriter(new FileOutputStream(
                    new File("login.txt"),true /* append = true */));
            
            
            pw.append("User 1 logged in at: " + n + "\n");
            pw.close();
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void alertIfUpcomingAppointment(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        
        long min = 20;
        
        ZoneId zid = ZoneId.of("UTC");
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime zdtNow = now.toLocalDateTime().atZone(zid);
        Timestamp a, b;
        a = Timestamp.valueOf(now.toLocalDateTime());
        try{
            Statement stmt = conn.createStatement();
            
            // sql statement for selecting all fields from appointment table
            String sqlStatementAppointment = "SELECT * FROM appointment;";
            
            // result set and execute statement for the appointment table
            ResultSet resultApt = stmt.executeQuery(sqlStatementAppointment);
            
            while(resultApt.next()){
                b = resultApt.getTimestamp("start");
                
                min = ChronoUnit.MINUTES.between(a.toLocalDateTime(), b.toLocalDateTime());
                if(min < 16 && min > 0){
                     alert.setTitle("");
                     alert.setContentText("You have appointment an upcoming appointment "  +
                          + min + " minutes");
                    alert.showAndWait();
                }
            }
        }catch(SQLException ex){
            System.out.println("Error: " + ex.getMessage());
        }
        
    }
    
}
