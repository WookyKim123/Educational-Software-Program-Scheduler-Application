package sample;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * Created by kis on 23/04/2017.
 */
public class Appointment {
    private DateTime startDateTime;
    private DateTime endDateTime;
    private Teacher teacher;
    private Student student;

    public DateTime getStartTime() {
        return startDateTime;
    }

    public String getStartTimeStr() {
        return DefaultScheduleFileHandler.dtf.print(this.startDateTime);
    }

    public String getEndTimeStr() {
        return DefaultScheduleFileHandler.dtf.print(this.endDateTime);
    }


    public DateTime getEndTime() {
        return endDateTime;
    }

    public Appointment(DateTime startDateTime, DateTime endDateTime, Teacher teacher, Student student) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.teacher = teacher;
        this.student = student;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public Student getStudent() {
        return student;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                '}';
    }

    public boolean isEqual(String studentName, String startTime) {
        if (studentName != null &&
                !studentName.isEmpty() && studentName.equals(student.getName())) {
            DateTimeFormatter dtf = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");
            String startTimeStr = dtf.print(startDateTime);

            if (startTimeStr != null && startTimeStr.equals(startTime)) {
                return true;
            }
        }
        return false;
    }
}
