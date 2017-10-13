package sample;

import org.joda.time.DateTime;

import java.util.*;

/**
 * Created by kis on 23/04/2017.
 */
public class Teacher {
    public static final int NUMBER_OF_DAYS = 30;
    private int scheduleUnitTimeInMinute;
    private String name;

    private String openTimeStart; // HHmm
    private String openTimeEnd; // HHmm
    private List<Map<DateTime, Appointment>> schedule;

    public void setSchedule(List<Map<DateTime, Appointment>> schedule) {
        this.schedule = schedule;
    }

    public List<Map<DateTime, Appointment>> getSchedule() {
        return schedule;
    }

    public Teacher(int scheduleUnitTimeInMinute, String name, String  openTimeStart, String  openTimeEnd) {
        this.scheduleUnitTimeInMinute = scheduleUnitTimeInMinute;
        this.name = name;
        this.openTimeStart = openTimeStart;
        this.openTimeEnd = openTimeEnd;
        initSchedule();
    }

    public Teacher(String Name, String openTimeStart, String openTimeEnd) {
        this.scheduleUnitTimeInMinute = 60; // 60 mins
        this.openTimeStart = openTimeStart;
        this.openTimeEnd = openTimeEnd;
        this.schedule = new ArrayList<>();
        initSchedule();
    }

    public void initSchedule() {
        this.schedule = new ArrayList<>();

        Map<DateTime, Appointment> daySchedule = new LinkedHashMap<>();
        DateTime dt = new DateTime(2017, 5, 1, 9, 00);
        DateTime openTimeEndDt = new DateTime(2017, 5, 1,
                Integer.parseInt(openTimeEnd.substring(0, 2)),
                Integer.parseInt(openTimeEnd.substring(2, 4)
                ));

        for (int i = 0; i < NUMBER_OF_DAYS; i++) {
            while (true) {
                System.out.println(dt);
                dt = dt.plusMinutes(this.scheduleUnitTimeInMinute);
                if (dt.isAfter(openTimeEndDt)) {
                    break;
                }
            }
            this.schedule.add(daySchedule);
            dt = new DateTime(dt.getYear(), dt.getMonthOfYear(), dt.getDayOfMonth() + 1, 9, 00);
            openTimeEndDt = openTimeEndDt.plusDays(1);
        }
    }

    public boolean addAppointment(Appointment newAppointment) {
//        int day = Integer.parseInt(newAppointment.getDate().substring(2,4)) - 1; // the indices of schedule list starts from 0
        int dayIndex = newAppointment.getStartTime().getDayOfMonth() - 1;

        Appointment ret = this.schedule.get(dayIndex).putIfAbsent(newAppointment.getStartTime(), newAppointment);
        if (ret == null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean removeAppointment(Appointment newAppointment) {
        return this.schedule.get(newAppointment.getStartTime().getDayOfMonth() - 1)
                .remove(newAppointment.getStartTime(), newAppointment);
    }

    public void showSchedule() {
        for (Map.Entry<DateTime, Appointment> entry : schedule.get(0).entrySet()) {
            DateTime key = entry.getKey();
            Appointment value = entry.getValue();
            System.out.println(key + ";" +  value);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
