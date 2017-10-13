package sample;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kis on 23/04/2017.
 */
public class Student {
    private String name;
    private String teacherName;
    private Teacher teacher;
    private List<Appointment> acceptedAppointment;
    private List<Appointment> rejectedAppointment;

    public Student(String name, Teacher teacher) {
        this.name = name;
        this.teacher = teacher;
        acceptedAppointment = new ArrayList<>();
        rejectedAppointment = new ArrayList<>();
    }



    public boolean requestAppointment(String startDateTimeStr, String endDateTimeStr) {
        String dateTimePattern = "yyyyMMdd-HHmm";
        DateTime startDateTime = DateTime.parse(startDateTimeStr, DateTimeFormat.forPattern(dateTimePattern));
        DateTime endDateTime   = DateTime.parse(endDateTimeStr, DateTimeFormat.forPattern(dateTimePattern));

        // start time & end time validation
        if (startDateTime.getMillis() >= endDateTime.getMillis()) {
            return false;
        }

        Appointment newAppointment = new Appointment(startDateTime, endDateTime, this.teacher, this);
        boolean isSuccess = teacher.addAppointment(newAppointment);
        if (isSuccess) {
            acceptedAppointment.add(newAppointment);
        } else {
            rejectedAppointment.add(newAppointment);
        }
        return isSuccess;
    }


    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public String getName() {
        return name;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public List<Appointment> getAcceptedAppointment() {
        return acceptedAppointment;
    }
}
