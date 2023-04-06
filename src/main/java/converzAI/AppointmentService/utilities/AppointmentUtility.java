package converzAI.AppointmentService.utilities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentUtility {

    public static List<String> getMergedIntervalsForOpenSlots(List<LocalDateTime> openSlots) {
        List<String> result = new ArrayList<>();
        List<int[]> intervals = new ArrayList<>();
        int startHour = openSlots.get(0).getHour();
        int previousHour = startHour;
        for(int i=1;i<openSlots.size();i++) {
            int currentHour = openSlots.get(i).getHour();
            if (currentHour != previousHour + 1) {
                intervals.add(new int[]{startHour, previousHour});
                startHour = currentHour;
            }
            previousHour = currentHour;
        }
        intervals.add(new int[]{startHour, previousHour});
        intervals.forEach(interval -> result.add(String.format(interval[0] + " - " + interval[1])));
        return result;
    }

}
