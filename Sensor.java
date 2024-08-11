import java.util.Random;

public class Sensor {
    private State curState;
    private double min;
    private double max;
    private String sensorName;
    private boolean canNegative;

    public Sensor(State initialState, double min, double max, String sensorName, boolean canNegative) {
        curState = initialState;
        this.min = min;
        this.max = max;
        this.sensorName = sensorName;
        this.canNegative = canNegative;
    }

    /**
     * 
     * @return current state of sensor
     */
    public State getState() {
        return this.curState;
    }

    /**
     * 
     * @return lower bound of sensor's allowed interval
     */
    public double getMin() {
        return this.min;
    }

    /**
     * 
     * @return upper bound of sensor's allowed interval
     */
    public double getMax() {
        return this.max;
    }

    /**
     * 
     * @return name of sensor 
     */
    public String getName() {
        return this.sensorName;
    }

    /**
     * 
     * @param newState passes in what the new state of sensor will be
     */
    public void setState(State newState) {
        this.curState = newState;
    }
    
    /**
     * Generate data for each sensor by randomly sampling from a gaussian
     * distribution with a mean that is the midpoint of its allowed interval;
     * standard deviation is scaled to the width of the interval. 
     * 
     * @return the pseudorandomized datapoint
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