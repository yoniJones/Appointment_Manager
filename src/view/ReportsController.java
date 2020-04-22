/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.text.DateFormatSymbols;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Records;
import static utils.DBConnection.conn;

/**
 * FXML Controller class
 *
 * @author yonij
 */
public class ReportsController implements Initializable {

    Records apt;

    @FXML
    private Button NumAptReportBtn;

    @FXML
    private Button schedulReportBtn;

    @FXML
    private Button logReportBtn;

    @FXML
    private Label repotLbl;

    @FXML
    private Button backBtn;

    @FXML
    private TextArea reportArea;

    private ArrayList<String> countriesList = new ArrayList<String>();
    Date d1;

    @FXML
    void NumberOfAppointmentsByMonth(MouseEvent event) {

        // sql statement the returns the numer of appointment typed for each month
        String sql = "SELECT MONTH(start) as month,  COUNT(DISTINCT type) as num FROM appointment group by MONTH(start);";
        int month, numApt;
        // clear the report area
        reportArea.clear();

        try {

            // statement object
            Statement stmt = conn.createStatement();
            // result set
            ResultSet result = stmt.executeQuery(sql);

            while (result.next()) {
                // get the month in an int format
                month = result.getInt("month");
                // gets the number of appointment types for each month
                numApt = result.getInt("num");

                reportArea.appendText("The number of appointment types for the month of " + getMonthName(month) + ": " + numApt + "\n");
            }

        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

    }

    @FXML
    void back(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
            view.MenuController controller = new MenuController(apt);
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
    void logReport(MouseEvent event) throws FileNotFoundException, IOException {

        reportArea.clear();
        File report = new File("login.txt"); 
        //  buffer reader
        BufferedReader br = new BufferedReader(new FileReader(report));
        String d;
        //write logins to text area
        while ((d = br.readLine()) != null) {
            reportArea.appendText(d + "\n");

        }

    }

    // list of all the countries
    @FXML
    void allCountries(MouseEvent event) {
        reportArea.clear();
        reportArea.setText("List of all customers countries:\n\n");
        //one line of code 'lambda' for iterating through the ArrayList
        countriesList.forEach(c -> reportArea.appendText(c + "\n"));
    }

    // schedule for each consultant 
    @FXML
    void scheduleReport(MouseEvent event) {
        //SELECT * FROM appointment ORDER BY userId;
        reportArea.clear();
        reportArea.setText("The schedule for each consultant based on their user ID \n");
        String sql = "SELECT * FROM appointment ORDER BY userId;";
        int userId;
        int t = 0;
        try {
            Statement stmt = conn.createStatement();
            ResultSet resultApt = stmt.executeQuery(sql);
            while (resultApt.next()) {
                userId = resultApt.getInt("userId");
                if (userId != t) { // make sure not to write the same report twice
                    reportArea.appendText("\n\nThe schedule for userId " + userId + " is:\n");
                    t = userId;
                }
                Timestamp start = convertTimeZones(resultApt.getTimestamp("start")); //converting to UTC

                Timestamp end = convertTimeZones(resultApt.getTimestamp("end"));

                reportArea.appendText("Appointment ID " + String.valueOf(resultApt.getInt("appointmentId"))
                        + " scheduled for " + start + " - " + end + "\n");

            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        // adding all the countries to the list
        for (int i = 0; i < apt.getAllCountries().size(); i++) {
            countriesList.add(apt.getAllCountries().get(i).getCountry());
        }
    }

    public ReportsController(Records apt) {
        this.apt = apt;

    }

    // sets the Timestamp to UTC and returns return a timestamp that is converted to
    // the systems default time zone
    public Timestamp convertTimeZones(Timestamp tStamp) {

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

    // returns the name of the month
    public String getMonthName(int m) {
        return new DateFormatSymbols().getMonths()[m - 1];
    }

}
