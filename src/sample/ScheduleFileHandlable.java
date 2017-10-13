package sample;

import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by kis on 28/05/2017.
 */
interface ScheduleFileHandlable {
    List<Map<DateTime, Appointment>> importSchedule(File file) throws IOException;
    void exportSchedule(List<Map<DateTime, Appointment>> src, File dest);

}
