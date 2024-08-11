import java.util.Random;

public class Sensor {
    //variables
    private State curState;
    private double min;
    private double max;
    private String sensorName;
    private boolean canNegative;

    //constructor
    public Sensor(State initialState, double min, double max, String sensorName, boolean canNegative) {
        curState = initialState;
        this.min = min;
        this.max = max;
        this.sensorName = sensorName;
        this.canNegative = canNegative;
    }

    //getters
    public State getState() {
        return this.curState;
    }

    public double getMin() {
        return this.min;
    }

    public double getMax() {
        return this.max;
    }

    public String getName() {
        return this.sensorName;
    }

    //setters
    public void setState(State newState) {
        this.curState = newState;
    }
    
    /* 
     * generate data for each sensor by randomly sampling from a gaussian
     * distribution with a mean that is the midpoint of its allowed interval
     */
    public double generateData() {
        double mean = (this.min + this.max) / 2;
        double stdDev = 0.3 * (this.max - this.min);
        Random rand = new Random();
        double randNum = rand.nextGaussian() * stdDev + mean;
        if (!this.canNegative) {
            while(randNum < 0) {
                randNum = rand.nextGaussian() * stdDev + mean;
            }
        }
        return randNum;
    }
}