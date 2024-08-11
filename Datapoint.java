import java.time.LocalTime;

/**
 * Want each data measurement to be linked to
 * a timestamp so we can display it when idle
 * and we can save it to the archive in chronological order.
 */
public class Datapoint {
    double value;
    LocalTime time;

    public Datapoint(double value) {
        this.value = value;
        time = LocalTime.now();
    }
}
