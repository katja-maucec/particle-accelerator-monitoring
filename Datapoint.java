import java.time.LocalTime;

public class Datapoint {
    double value;
    LocalTime time;

    public Datapoint(double value) {
        this.value = value;
        time = LocalTime.now();
    }

    

}
