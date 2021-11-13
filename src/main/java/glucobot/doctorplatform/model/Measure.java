package glucobot.doctorplatform.model;

public class Measure {
    private double timestamp;
    private double glycemia;
    private double insulin;

    public Measure(double timestamp, double glycemia, double insulin) {
        this.timestamp = timestamp;
        this.glycemia = glycemia;
        this.insulin = insulin;
    }

    public double getTimestamp() {
        return timestamp;
    }

    public double getGlycemia() {
        return glycemia;
    }

    public double getInsulin() {
        return insulin;
    }

    @Override
    public String toString() {
        return "Measure{" +
                "timestamp=" + timestamp +
                ", glycemia=" + glycemia +
                ", insulin=" + insulin +
                '}';
    }
}
