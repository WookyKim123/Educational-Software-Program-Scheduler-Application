package sample;

import javafx.beans.property.SimpleStringProperty;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by kis on 23/04/2017.
 */
public class AppointmentForTable {
    private SimpleStringProperty startDateTime;
    private SimpleStringProperty endDateTime;
    private SimpleStringProperty teacher;
    private SimpleStringProperty student;

    public AppointmentForTable(Appointment appointment) {
        this.teacher = new SimpleStringProperty(appointment.getTeacher().getName());
        this.student = new SimpleStringProperty(appointment.getStudent().getName());
//        String startTimeStr = appointment.getStartTime().toString();
        DateTimeFormatter dtf = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");
        String startTimeStr = dtf.print(appointment.getStartTime());
        String endTimeStr = dtf.print(appointment.getEndTime());
        this.startDateTime = new SimpleStringProperty(startTimeStr);
        this.endDateTime = new SimpleStringProperty(endTimeStr);
    }

    public String getStartDateTime() {
        return startDateTime.get();
    }

    public SimpleStringProperty startDateTimeProperty() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime.set(startDateTime);
    }

    public String getEndDateTime() {
        return endDateTime.get();
    }

    public SimpleStringProperty endDateTimeProperty() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime.set(endDateTime);
    }

    public String getTeacher() {
        return teacher.get();
    }

    public SimpleStringProperty teacherProperty() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher.set(teacher);
    }

    public String getStudentName() {
        return student.get();
    }

    public SimpleStringProperty studentProperty() {
        return student;
    }

    public void setStudent(String student) {
        this.student.set(student);
    }
}
