package sample;

import com.jfoenix.controls.JFXTimePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.chrono.Chronology;
import java.time.chrono.IsoChronology;
import java.util.*;

public class Controller implements Initializable {
    public static final String OPEN_TIME_START = "0900";
    public static final String OPEN_TIME_END = "1800";
    public static final int SCHEDULE_UNIT_TIME_IN_MINUTE = 60;

    @FXML
    private TextField studentNameTextField;

//    @FXML
//    private TextField teacherNameTextField;

    @FXML
    private DatePicker datePickerTextField;

    @FXML
    private Button submitButton;

    @FXML
    private JFXTimePicker startTimePicker;

    @FXML
    private JFXTimePicker endTimePicker;

    @FXML
    private javafx.scene.control.TableView<AppointmentForTable> timeTable = new javafx.scene.control.TableView<AppointmentForTable>();

    @FXML
    private Button deleteBtn;

    private Teacher defaultTeacher = new Teacher(SCHEDULE_UNIT_TIME_IN_MINUTE, "Ms.scates",
            OPEN_TIME_START, OPEN_TIME_END);

    public ScheduleFileHandlable scheduleFileHandlable = new DefaultScheduleFileHandler(defaultTeacher);

    ObservableList<AppointmentForTable> value = FXCollections.observableArrayList();

    File file = new File("/Users/kis/IdeaProjects/EducationalSoftwareProject/src/sample/schedule.txt");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Map<DateTime, Appointment>> scheduleFromFile = null;
        try {
            scheduleFromFile = scheduleFileHandlable.importSchedule(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        defaultTeacher.setSchedule(scheduleFromFile);

        for (Map<DateTime, Appointment> daySchedule:scheduleFromFile) {
            for (Map.Entry<DateTime, Appointment> entry : daySchedule.entrySet()) {
                // add to list
                DateTime dt = entry.getKey();
                System.out.println(dt);
                value.add(new AppointmentForTable(
                        new Appointment(dt, entry.getValue().getEndTime(), entry.getValue().getTeacher(), entry.getValue().getStudent())));
            }
        }

        // set cols
        ObservableList<TableColumn<AppointmentForTable,?>> cols = timeTable.getColumns();
        for (TableColumn col: cols) {
            System.out.println(col.getText());
        }
        TableColumn studentNameCol = cols.get(0);
        TableColumn teacherNameCol = cols.get(1);
        TableColumn startTimeCol = cols.get(2);
        TableColumn endTimeCol = cols.get(3);

        studentNameCol.setCellValueFactory(new PropertyValueFactory<AppointmentForTable, String>("student"));
        teacherNameCol.setCellValueFactory(new PropertyValueFactory<AppointmentForTable, String>("teacher"));
        startTimeCol.setCellValueFactory(new PropertyValueFactory<AppointmentForTable, String>("startDateTime"));
        endTimeCol.setCellValueFactory(new PropertyValueFactory<AppointmentForTable, String>("endDateTime"));

        timeTable.setItems(value);

        // Initialize studentNameTextField
        studentNameTextField.setText("");
        // Initialize datePickerTextField
        Chronology chronology = IsoChronology.INSTANCE;
        datePickerTextField.setChronology(chronology);

        deleteBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AppointmentForTable selectedItem = timeTable.getSelectionModel().getSelectedItem();

                List<Map<DateTime, Appointment>> schedule = defaultTeacher.getSchedule();
                for (Map<DateTime, Appointment> daySchedule: schedule) {
                    Iterator<Map.Entry<DateTime, Appointment>> iter = daySchedule.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry<DateTime, Appointment> entry = iter.next();

                        // "MM/dd/yyyy HH:mm:ss" start time pattern
                        if (entry.getValue().isEqual(selectedItem.getStudentName(), selectedItem.getStartDateTime())) {
                            defaultTeacher.removeAppointment(entry.getValue());
//                            iter.remove();
                            break;
                        }
                    }
                }
                timeTable.getItems().remove(selectedItem);
                scheduleFileHandlable.exportSchedule(defaultTeacher.getSchedule(), file);
            }
        });

        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // validation
                System.out.println(studentNameTextField.getText());

                if (isEmptyString(studentNameTextField.getText())) {
                    throw new RuntimeException("invalid input");
                }

                String yearString = String.valueOf(datePickerTextField.valueProperty().getValue().getYear());
                String monthStr = String.valueOf(datePickerTextField.valueProperty().getValue().getMonth().getValue());
                monthStr = leftZeroPad(monthStr);
                String dayOfMonthStr = String.valueOf(datePickerTextField.valueProperty().getValue().getDayOfMonth());
                dayOfMonthStr = leftZeroPad(dayOfMonthStr);
                String dateStr = monthStr + dayOfMonthStr;

                int startHour = startTimePicker.getValue().getHour();
                int startMinute = startTimePicker.getValue().getMinute();

                int endHour = endTimePicker.getValue().getHour();
                int endMinute = endTimePicker.getValue().getMinute();

                String startDtStr = yearString + monthStr + dayOfMonthStr + "-" + String.valueOf(startHour)
                        + String.valueOf(startMinute);
                String endDtStr = yearString + monthStr + dayOfMonthStr + "-" + String.valueOf(endHour)
                        + String.valueOf(endMinute);
                LocalDate localDt = datePickerTextField.valueProperty().getValue();
                DateTime startDt = new DateTime(localDt.getYear(), localDt.getMonth().getValue(), localDt.getDayOfMonth(), startHour, startMinute);
                DateTime endDt = new DateTime(localDt.getYear(), localDt.getMonth().getValue(), localDt.getDayOfMonth(), endHour, endMinute);
                Appointment newApp = new Appointment(startDt, endDt,  defaultTeacher, new Student(studentNameTextField.getText(), defaultTeacher));
                boolean isSuccess = defaultTeacher.addAppointment(newApp);
                if (isSuccess) {
                    defaultTeacher.showSchedule();
                    System.out.println("Appointment has been successfully made!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    submitButton.setStyle("-fx-background-color: #00FF00");

                    // add to list
                    DateTime jodaDt = new DateTime(Integer.parseInt(yearString), Integer.parseInt(monthStr),
                            Integer.parseInt(dayOfMonthStr), startHour, startMinute, 0);
                    System.out.println(jodaDt);

                    DateTime endTime = new DateTime(Integer.parseInt(yearString), Integer.parseInt(monthStr),
                            Integer.parseInt(dayOfMonthStr), endHour, endMinute, 0);

                    value.add(new AppointmentForTable(
                            new Appointment(jodaDt, endTime, defaultTeacher, new Student(studentNameTextField.getText(), defaultTeacher))));
                    scheduleFileHandlable.exportSchedule(defaultTeacher.getSchedule(), file);
                } else {
                    studentNameTextField.setText("");
                    System.out.println("fail");
                }
            }
        });
    }

    private boolean isEmptyString(String str) {
        return studentNameTextField.getText() == null || studentNameTextField.getText().equals("");
    }

    private String leftZeroPad(String src) {
        return String.format("%02d", Integer.parseInt(src));
    }
}
