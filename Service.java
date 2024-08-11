import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Service {
    //archive consists of hashmap with 4 entries, one for each sensor
    HashMap<String, List<Datapoint>> archive = new HashMap<String, List<Datapoint>>();

    public Service() {
        archive.put("temperature", new ArrayList<>());
        archive.put("humidity", new ArrayList<>());
        archive.put("radiation", new ArrayList<>());
        archive.put("pressure", new ArrayList<>());
    }

    /*
     * responsible for archiving and monitoring
     * 
     * return true if alarm should be triggered
     */
    public String measure(Sensor sensor, double dataVal) {
        //archiving
        Datapoint datapoint = new Datapoint(dataVal);
        archive.get(sensor.getName()).add(datapoint);

        //monitoring
        if (dataVal < sensor.getMin()) {
            return "too low";
        } else if (dataVal > sensor.getMax()) {
            return "too high";
        }

        return  "good";
    }

    public void saveToFile() throws IOException {
        
        for (String sensorName : archive.keySet()) {
            BufferedWriter writer = new BufferedWriter(new FileWriter(sensorName + ".txt"));
            for (Datapoint datapoint : archive.get(sensorName)) {
                writer.write(datapoint.value + " at " + datapoint.time.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM)));
                writer.newLine();
            }
            writer.close();
        }
    }
}