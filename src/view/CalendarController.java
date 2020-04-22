/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import static java.time.ZonedDateTime.now;
import java.time.format.DateTimeFormatter;
import static java.time.temporal.ChronoField.YEAR_OF_ERA;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Appointment;
import model.Records;
import static utils.DBConnection.conn;
import utils.SQLinteractivity;

/**
 * FXML Controller class
 *
 * @author yonij
 */
public class CalendarController implements Initializable {

    Records r;
    ObservableList<String> nameAndId = FXCollections.observableArrayList();
    ObservableList<String> hourList = FXCollections.observableArrayList();
    ObservableList<String> minutList = FXCollections.observableArrayList();

    @FXML
    private Button byMonthBtn;

    @FXML
    private Button byWeekBtn;

    @FXML
    private Button backBtn;

    @FXML
    private TableView<Appointment> shcedualTvw;

    @FXML
    private TextField titleTxt;

    @FXML
    private TextField descriptionTxt;

    @FXML
    private TextField locationTxt;

    @FXML
    private TextField ContactTxt;

    @FXML
    private TextField typeTxt;

    @FXML
    private DatePicker datePcr;

    @FXML
    private ComboBox customerCbox;

    @FXML
    private Label aptIDlbl;

    @FXML
    private Label makeORupdateLbl;

    @FXML
    private Label IdLbl;

    @FXML
    private ComboBox startHourCmx;

    @FXML
    private ComboBox startMinutsCmx;

    @FXML
    private ComboBox endHoursCmx;

    @FXML
    private ComboBox endMinutsCmx;

    @FXML
    private Label hoursOfOperationLbl;

    private boolean update;
    int id, sHour, eHour, sMin, eMin, userId, cusId, aptID;

    LocalDate date;

    // used for populating the created on and last update fields in the database
    ZonedDateTime now;
    Date dateStamp, stDate;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-mm-dd hh:mm");

    // used for making a query
    String query;

    Date dateTest = new Date();
    Timestamp ts = new Timestamp(System.currentTimeMillis());
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    Timestamp tsStartDB, tsEndDB;
    Calendar startCalendar, endCalendar;

    // used for displaying hours of operations
    LocalDateTime HStart, Hend;

    // used for testing schedule conflictions
    LocalDateTime Cstart, Cend;

    @FXML
    void createOrUpdate(MouseEvent event) {
        // update appointment
        if (update == true) {
            updateAppointment();
        }
        if (update == false) {
            createNew();
        }
    }

    // load the menue page
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
    void byMonth(MouseEvent event) {
        r.clearAllFilteredAppointments(); // clear appointments so we can populate list with 'by month' appointments

        try {
            // statement object
            Statement stmt = conn.createStatement();
            String sqlStatement = "SELECT * FROM appointment WHERE MONTH(start) = MONTH(CURRENT_DATE()) "
                    + "AND YEAR(start) = YEAR(CURRENT_DATE())"; // sql statement for retrieving appointments by month
            ResultSet result = stmt.executeQuery(sqlStatement);
            while (result.next()) {
                int aptID = result.getInt("appointmentId");
                int custID = result.getInt("customerId");
                int userID = result.getInt("userId");
                String title = result.getString("title");
                String location = result.getString("location");
                String description = result.getString("description");
                String contact = result.getString("contact");
                String type = result.getString("type");
                Timestamp start = result.getTimestamp("start");
                Timestamp end = result.getTimestamp("end");
                // add filtered appointments by month
                r.addFilteredAppointment(new Appointment(aptID, custID, userID, title, location, contact, type, description, start, end));
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        shcedualTvw.setItems(r.getFilteredAppointments()); // set filtered 'by month appointments
        shcedualTvw.refresh();

    }

    @FXML
    void byWeek(MouseEvent event) {
        r.clearAllFilteredAppointments(); // clear list 

        try {
            // statement object
            Statement stmt = conn.createStatement();
            // sql statement for filtering appointments by week
            String sqlStatement = "SELECT * FROM   appointment  WHERE  YEARWEEK(`start`, 1) = YEARWEEK(CURDATE(), 1);";
            ResultSet result = stmt.executeQuery(sqlStatement);

            while (result.next()) {
                int aptID = result.getInt("appointmentId");
                int custID = result.getInt("customerId");
                int userID = result.getInt("userId");
                String title = result.getString("title");
                String location = result.getString("location");
                String description = result.getString("description");
                String contact = result.getString("contact");
                String type = result.getString("type");
                Timestamp start = result.getTimestamp("start");
                Timestamp end = result.getTimestamp("end");
                r.addFilteredAppointment(new Appointment(aptID, custID, userID, title, location, contact, type, description, start, end));
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        shcedualTvw.setItems(r.getFilteredAppointments()); // add 'by week' filtered appointments to table
        shcedualTvw.refresh();
    }

    @FXML
    void all(MouseEvent event) {
        r.clearAllFilteredAppointments(); // clear list
        try { // connect to database and add all appointments
            Statement stmt = conn.createStatement();
            // sql statement for selecting all fields from appointment table
            String sqlStatementAppointment = "SELECT * FROM appointment;";
            // result set and execute statement for the appointment table
            ResultSet resultApt = stmt.executeQuery(sqlStatementAppointment);

            while (resultApt.next()) {
                int aptID = resultApt.getInt("appointmentId");
                int custID = resultApt.getInt("customerId");
                int userID = resultApt.getInt("userId");
                String title = resultApt.getString("title");
                String location = resultApt.getString("location");
                String description = resultApt.getString("description");
                String contact = resultApt.getString("contact");
                String type = resultApt.getString("type");
                Timestamp start = resultApt.getTimestamp("start");
                Timestamp end = resultApt.getTimestamp("end");

                r.addAppointment(new Appointment(aptID, custID, userID, title, location, contact, type, description, start, end));
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        shcedualTvw.setItems(r.getAllAppointments());
        shcedualTvw.refresh();
    }

    @FXML
    void delete(MouseEvent event) {
        // I used a lambda expression with an alert because the code is much cleaner and easier
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Are you sure you want to delete this appointment?");
        alert.showAndWait().ifPresent((response -> {
            if (response == ButtonType.OK) { // if user clicks ok then appointment gets deleted
            }
            try {

                Appointment selected = shcedualTvw.getSelectionModel().getSelectedItem();
                if (selected == null) {
                    errorWindow(1); // the user didnt select
                    return;
                }

                id = selected.getAppointmentID(); // get the selected appointment ID
                String sqlDeleteApp = "DELETE FROM appointment WHERE appointmentId =" + String.valueOf(id) + ";";
                PreparedStatement ps = conn.prepareStatement(sqlDeleteApp); // deleting appointment from database 
                ps.execute();
                ps.executeUpdate();
                r.deleteAppointment(id);
            } catch (SQLException ex) {
                System.out.println("Error deleting appointment: " + ex.getMessage());
            }
        }));
    }

    @FXML
    void Clear(MouseEvent event) { //clear entered data
        clearText();
    }

    // if user selects an appointment from the table
    @FXML
    void select(MouseEvent event) {
        try {
            Appointment selected = shcedualTvw.getSelectionModel().getSelectedItem();
            if (selected == null) {
                errorWindow(1);
            } else {
                aptID = selected.getAppointmentID();
                userId = selected.getUserID();
                cusId = selected.getCustomerID();
                makeORupdateLbl.setText("Update Appointment");
                aptIDlbl.setText("Appointment ID: ");
                IdLbl.setText(String.valueOf(selected.getAppointmentID()));
                titleTxt.setText(selected.getTitle());
                typeTxt.setText(selected.getType());
                descriptionTxt.setText(selected.getDescription());
                locationTxt.setText(selected.getLocation());
                ContactTxt.setText(selected.getContact());
                customerCbox.setValue(String.valueOf(cusId) + r.getCustomerName(cusId));

                update = true;

            }

        } catch (Exception e) {
            return;
        }

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        HStart = convertHoursToLocal(LocalDateTime.of(2020, 03, 2, 8, 00));
        Hend = convertHoursToLocal(LocalDateTime.of(2020, 03, 2, 17, 00));
        hoursOfOperationLbl.setText("Hours you enter will be converted to UTC. \n "
                + "Hours of operation are between 8:00 - 17:00. \n"
                + "Based on your location, it will be between " + HStart.getHour() + ":00"
                + " - " + Hend.getHour() + ":00");
        shcedualTvw.setItems(r.getAllAppointments());
        setNameAndIDlist();
        addTimeToAbservableList();
        customerCbox.setItems(nameAndId); // set the and ID of the customer
        startHourCmx.setItems(hourList);
        endHoursCmx.setItems(hourList);
        startMinutsCmx.setItems(minutList);
        endMinutsCmx.setItems(minutList);

    }
// constructor

    public CalendarController(Records apt) {
        this.update = false;
        this.r = apt;
        this.customerCbox = new ComboBox(apt.getAllCustomers());

    }
// error alert window method

    public void errorWindow(int i) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (i == 1) {
            alert.setTitle("No selection!");
            alert.setHeaderText(null);
            alert.setContentText("Select an appointment!");
            alert.showAndWait();
        }
        if (i == 2) {
            alert.setTitle("Missing Entry");
            alert.setHeaderText(null);
            alert.setContentText("Must Enter a Title!");
            alert.showAndWait();
        }
        if (i == 3) {
            alert.setTitle("Missing Entry");
            alert.setHeaderText(null);
            alert.setContentText("Must Enter a Description!");
            alert.showAndWait();
        }
        if (i == 4) {
            alert.setTitle("Missing Entry");
            alert.setHeaderText(null);
            alert.setContentText("Must Enter a Location!");
            alert.showAndWait();
        }
        if (i == 5) {
            alert.setTitle("Missing Entry");
            alert.setHeaderText(null);
            alert.setContentText("Must Enter Contact Information!");
            alert.showAndWait();
        }
        if (i == 6) {
            alert.setTitle("Appointment Overlap");
            alert.setHeaderText(null);
            alert.setContentText("your appointment overlaps another appointment!");
            alert.showAndWait();
        }
        if (i == 7) {
            alert.setTitle("Conflicting Hours");
            alert.setHeaderText(null);
            alert.setContentText("Appointment must be scheduled in operation hours between 8:00 and 17:00!");
            alert.showAndWait();
        }
        if (i == 8) {
            alert.setTitle("Conflicting Hours");
            alert.setHeaderText(null);
            alert.setContentText("Start Time cannot be scheduled at the same time or after end time");
            alert.showAndWait();
        }
        if (i == 9) {
            alert.setTitle("Missing Entry");
            alert.setHeaderText(null);
            alert.setContentText("Must select a date!");
            alert.showAndWait();
        }
        if (i == 10) {
            alert.setTitle("Missing Entry");
            alert.setHeaderText(null);
            alert.setContentText("Must select a start and end time!");
            alert.showAndWait();
        }
        if (i == 11) {
            alert.setTitle("Appointment Overlap");
            alert.setHeaderText(null);
            alert.setContentText("incorrect time entry!");
            alert.showAndWait();
        }
        if (i == 12) {
            alert.setTitle("Missing Entry");
            alert.setHeaderText(null);
            alert.setContentText("Must Enter a Type!");
            alert.showAndWait();
        }
        if (i == 13) {
            alert.setTitle("Missing Entry");
            alert.setHeaderText(null);
            alert.setContentText("Must select a customer!");
            alert.showAndWait();
        }
    }
// used to concat username and ID 

    public void setNameAndIDlist() {
        for (int i = 0; i < r.getAllCustomers().size(); i++) {
            nameAndId.add(String.valueOf(r.getAllCustomers().get(i).getCustomerID() + " " + r.getAllCustomers().get(i).getCustomerName()));
        }
    }

// used to format dated but currently not used
    public LocalDate getLocalDateFormat(String s) throws ParseException {
        String pat = " at:(\\d{4}-\\d{2}-\\d{2}) Notes:";
        Matcher a = Pattern.compile(pat).matcher(s);
        if (a.find()) {
            Date d = new SimpleDateFormat("yyyy-MM-dd").parse(a.group(1));
            return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        return null;
    }
// adding hours and minut selection to observableList later to use in combo box

    public void addTimeToAbservableList() {
        hourList.addAll("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11",
                "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23");
        minutList.addAll("00", "15", "30", "45");
    }

    // used to validate user created/updated appointments for overlaps or miss entries
    public boolean validateAppointmentOverlap(Timestamp s, Timestamp e, int id) {
        Timestamp rs, re;
        int rId;

        try {
            // statement object
            Statement stmt = conn.createStatement();
            String sqlStatement = "SELECT appointmentId, start, end FROM appointment;";
            ResultSet result = stmt.executeQuery(sqlStatement);

            while (result.next()) {
                rId = result.getInt("appointmentId");
                rs = result.getTimestamp("start");
                re = result.getTimestamp("end");
                if (id != rId) { // so not to calculate the date that is being updated
                    // check for overlaps
                    System.out.println(rId);
                    if (s.before(rs) && e.after(rs)) {
                        System.out.println("A");
                        return false;
                    }
                    if (s.after(rs) && s.before(re)){ 
                        return false;
                    }
                    if (e.after(rs) && e.before(re)) {
                        return false;
                    }
                    if (s.equals(rs) || e.equals(re)) {
                        System.out.println("D");
                        return false;
                    }
                    if (e.before(s) || s.equals(e)) {
                        System.out.println("D");
                        return false;
                    }
                }

            }

        } catch (SQLException ex) {
            System.out.println("Error with comparing overlap: " + ex.getMessage());
        }
        return true;
    }

    // method for reading the appointments into the observablelist after a new appointment is created or if an
    // appointment is updated
    public void reAddappointmentsToObservableList() {
        try {

            // statement object
            Statement stmt = conn.createStatement();

            // sql statement for selecting all fields from appointment table
            String sqlStatementAppointment = "SELECT * FROM appointment;";

            // result set and execute statement for the appointment table
            ResultSet resultApt = stmt.executeQuery(sqlStatementAppointment);
            while (resultApt.next()) {
                int aptID = resultApt.getInt("appointmentId");
                int custID = resultApt.getInt("customerId");
                int userID = resultApt.getInt("userId");
                String title = resultApt.getString("title");
                String location = resultApt.getString("location");
                String description = resultApt.getString("description");
                String contact = resultApt.getString("contact");
                String type = resultApt.getString("type");

                // converting the timezone
                Timestamp start = convertTimeZones(resultApt.getTimestamp("start"));

                Timestamp end = convertTimeZones(resultApt.getTimestamp("end"));

                r.addAppointment(new Appointment(aptID, custID, userID, title, location, contact, type, description, start, end));
            }
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
// if update appointment is clicked

    public void updateAppointment() {
        id = Integer.valueOf(IdLbl.getText());
        // validate data entry
        if (descriptionTxt.getText().isEmpty() || descriptionTxt.getText() == "" || descriptionTxt.getText() == null) {
            errorWindow(2);
            return;
        }
        if (titleTxt.getText().isEmpty() || titleTxt.getText() == "" || titleTxt.getText() == null) {
            errorWindow(3);
            return;
        }
        if (locationTxt.getText().isEmpty() || locationTxt.getText() == "" || locationTxt.getText() == null) {
            errorWindow(4);
            return;
        }
        if (ContactTxt.getText().isEmpty() || ContactTxt.getText() == "" || ContactTxt.getText() == null) {
            errorWindow(5);
            return;
        }
        if (typeTxt.getText().isEmpty() || typeTxt.getText() == "" || typeTxt.getText() == null) {
            errorWindow(12);
            return;
        }
        if (startHourCmx.getValue() == null || endHoursCmx.getValue() == null || endMinutsCmx.getValue() == null || startMinutsCmx.getValue() == null) {
            errorWindow(10);
            return;
        }
        if (datePcr.getValue() == null) {
            errorWindow(9);
            return;
        }

        date = datePcr.getValue();
        sHour = Integer.valueOf(startHourCmx.getValue().toString());
        eHour = Integer.valueOf(endHoursCmx.getValue().toString());
        sMin = Integer.valueOf(startMinutsCmx.getValue().toString());
        eMin = Integer.valueOf(endMinutsCmx.getValue().toString());
        System.out.println("sHour + SMin: " + sHour + ":" + eMin);

        // convert date to utc
        Cstart = convertHoursToUTC(LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), sHour, sMin));
        Cend = convertHoursToUTC(LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getMonthValue(), eHour, eMin));

        System.out.println("Cstart :" + Cstart.getHour() + ":" + Cstart.getMinute());
        System.out.println("CEnd :" + Cend.getHour() + ":" + Cend.getMinute());
        // validate houre entry
        if (sHour > 17 || sHour < 8) {
            errorWindow(7);
            return;
        }
        if (sHour > eHour || (sHour == eHour && sMin >= eMin)) {
            errorWindow(8);
            return;
        }

        LocalDateTime startDate = LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(),
                sHour, sMin);

        LocalDateTime endDate = LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(),
                eHour, eMin);

        Timestamp tsStart = Timestamp.valueOf(startDate);
        Timestamp tsEnd = Timestamp.valueOf(endDate);

        if (validateAppointmentOverlap(tsStart, tsEnd, 0) == false) {
            errorWindow(6);
            return;
        }

// update appointment sql string
        query = "UPDATE appointment SET title = '" + titleTxt.getText() + "', description = '" + descriptionTxt.getText() + "', location = '" + locationTxt.getText()
                + "', contact = '" + ContactTxt.getText() + "', type = '" + typeTxt.getText() + "', start = '" + startDate + "', end = '"
                + endDate + "', lastUpdate = '" + df.format(ts) + "', lastUpdateBy = 'user 1'"
                + "WHERE appointmentId = " + IdLbl.getText() + ";";

        SQLinteractivity.createQuery(query);
        r.getAllAppointments().clear(); // clear appointments
        // add all appointments with new updated one
        reAddappointmentsToObservableList();

        shcedualTvw.refresh();
        clearText();
    }

    public void createNew() {
        if (descriptionTxt.getText().isEmpty() || descriptionTxt.getText() == "" || descriptionTxt.getText() == null) {
            errorWindow(2);
            return;
        }
        if (titleTxt.getText().isEmpty() || titleTxt.getText() == "" || titleTxt.getText() == null) {
            errorWindow(3);
            return;
        }
        if (locationTxt.getText().isEmpty() || locationTxt.getText() == "" || locationTxt.getText() == null) {
            errorWindow(4);
            return;
        }
        if (ContactTxt.getText().isEmpty() || ContactTxt.getText() == "" || ContactTxt.getText() == null) {
            errorWindow(5);
            return;
        }
        if (typeTxt.getText().isEmpty() || typeTxt.getText() == "" || typeTxt.getText() == null) {
            errorWindow(12);
            return;
        }
        if (startHourCmx.getValue() == null || endHoursCmx.getValue() == null || endMinutsCmx.getValue() == null || startMinutsCmx.getValue() == null) {
            errorWindow(10);
            return;
        }
        if (datePcr.getValue() == null) {
            errorWindow(9);
            return;
        }
        if (customerCbox.getValue() == null) {
            errorWindow(13);
            return;
        }
        // getting the first character from the id + name string in the customer combo box
        // and parsing it to an int to get the user id
        String num = customerCbox.getValue().toString();
        String a = num.substring(0, num.indexOf(" "));
        cusId = Integer.parseInt(a);
        System.out.println(cusId);
        date = datePcr.getValue();
        sHour = Integer.valueOf(startHourCmx.getValue().toString());
        eHour = Integer.valueOf(endHoursCmx.getValue().toString());
        sMin = Integer.valueOf(startMinutsCmx.getValue().toString());
        eMin = Integer.valueOf(endMinutsCmx.getValue().toString());
        if (sHour > 17 || sHour < 8) {
            errorWindow(7);
            return;
        }
        if (sHour > eHour || (sHour == eHour && sMin >= eMin)) {
            errorWindow(8);
            return;
        }

        LocalDateTime startDate = LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(),
                sHour, sMin);

        LocalDateTime endDate = LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(),
                eHour, eMin);

        Timestamp tsStart = Timestamp.valueOf(startDate);
        Timestamp tsEnd = Timestamp.valueOf(endDate);

        if (validateAppointmentOverlap(tsStart, tsEnd, id) == false) {
            errorWindow(6);
            return;
        }
        now = ZonedDateTime.now();
        dateStamp = new Date(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), now.getHour(), now.getMinute());
        query = "INSERT INTO appointment (customerId, userId, title, description, location, contact, "
                + "type, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy)"
                + "VALUES( " + String.valueOf(cusId) + "," + String.valueOf(1) + ",'" + titleTxt.getText()
                + "','" + descriptionTxt.getText() + "','"
                + locationTxt.getText() + "','" + ContactTxt.getText() + "','" + typeTxt.getText() + "','not needed','"
                + startDate + "','" + endDate + "', '" + now().toLocalDateTime()
                + "','user 1', '" + now().toLocalDateTime() + "', 'user 1');";
        SQLinteractivity.createQuery(query);
        r.getAllAppointments().clear();
        reAddappointmentsToObservableList();
        clearText();
    }

    public LocalDateTime convertHoursToLocal(LocalDateTime tStamp) {

        LocalDateTime localTime;

        //Setting the zoneId to UTC
        ZoneId zid = ZoneId.of("UTC");
        // setting the zoneID value from the resultSet to UTC
        ZonedDateTime zdtStart = tStamp.atZone(zid);

        //Converting UTC time to the systems default timezone
        ZonedDateTime dt = zdtStart.withZoneSameInstant(ZoneId.systemDefault());

        // converting zonedDateTime to LocalDateTime
        localTime = dt.toLocalDateTime();

        return localTime;
    }

    public LocalDateTime convertHoursToUTC(LocalDateTime tStamp) {

        LocalDateTime utc;

        //Setting the zoneId to UTC
        ZoneId zid = ZoneId.of("UTC");
        // setting the zoneID value from the resultSet to UTC
        ZonedDateTime zdtStart = tStamp.atZone(zid);

        //Converting UTC time to the systems default timezone
        ZonedDateTime dt = zdtStart.withZoneSameInstant(zid);

        // converting zonedDateTime to LocalDateTime
        utc = dt.toLocalDateTime();

        return utc;
    }

    public Timestamp convertDateToLocal(Timestamp tStamp) {

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
    
    // clear the text from the text field
    public void clearText(){
        makeORupdateLbl.setText("Make Appointment");
        aptIDlbl.setText("");
        IdLbl.setText("");
        titleTxt.setText("");
        typeTxt.setText("");
        descriptionTxt.setText("");
        locationTxt.setText("");
        ContactTxt.setText("");
        customerCbox.setValue(null);
        startHourCmx.getSelectionModel().clearSelection();
        startMinutsCmx.getSelectionModel().clearSelection();
        endHoursCmx.getSelectionModel().clearSelection();
        endMinutsCmx.getSelectionModel().clearSelection();
        datePcr.getEditor().clear();
        update = false;
    }

}
