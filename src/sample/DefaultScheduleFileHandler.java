package sample;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kis on 28/05/2017.
 */
public class DefaultScheduleFileHandler implements ScheduleFileHandlable {
    private Student student;
    private Teacher teacher;

    public DefaultScheduleFileHandler(Teacher teacher) {
        this.teacher = teacher;
    }

    public static DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyyMMdd-HHmm");

    @Override
    public List<Map<DateTime, Appointment>> importSchedule(File file) throws IOException {
        List<Map<DateTime, Appointment>> scheduleFromFile = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(file.getPath()));

        DateTime dt = new DateTime(2017, 5, 1, 9, 00);

        for (int i = 0; i < Teacher.NUMBER_OF_DAYS; i++) {
            Map<DateTime, Appointment> daySchedule = new LinkedHashMap<>();
            scheduleFromFile.add(daySchedule);
        }

        for (String line: lines) {
            String[] tokens = line.split(" ");
            String studentName = tokens[0];
            String teacherName = tokens[1];
            String startTimeStr = tokens[2];
            String endTimeStr = tokens[3];

            DateTime startTime = dtf.parseDateTime(startTimeStr);
            DateTime endTime = dtf.parseDateTime(endTimeStr);

            int dayOfMonthIdx = startTime.getDayOfMonth() - 1;
            Map<DateTime, Appointment> dayScheduleMap = scheduleFromFile.get(dayOfMonthIdx);
            Appointment app = new Appointment(startTime, endTime, teacher, new Student(studentName, teacher));
            dayScheduleMap.putIfAbsent(startTime, app);
        }


        return scheduleFromFile;
    }

    @Override
    public void exportSchedule(List<Map<DateTime, Appointment>> src, File dest) {
        try{
            PrintWriter writer = new PrintWriter(dest, "UTF-8");

            for (Map<DateTime, Appointment> daySchedule: src) {
                for (Map.Entry<DateTime, Appointment> entry : daySchedule.entrySet()) {
                    // add to list
                    DateTime dt = entry.getKey();
                    writer.println(entry.getValue().getStudent().getName()
                            + " "
                            + entry.getValue().getTeacher().getName()
                            + " "
                            + dtf.print(entry.getValue().getStartTime())
                            + " "
                            + dtf.print(entry.getValue().getEndTime())
                    );
                }
            }
            writer.close();
        } catch (IOException e) {
            // do something
        }


    }
}
