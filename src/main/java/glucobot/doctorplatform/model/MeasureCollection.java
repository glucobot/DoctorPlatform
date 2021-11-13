package glucobot.doctorplatform.model;

import java.util.List;

public class MeasureCollection {
    private int count;
    private String firstTimestamp, lastTimestamp;
    private List<Measure> measures;

    public MeasureCollection(int count, String firstTimestamp, String lastTimestamp, List<Measure> measures) {
        this.count = count;
        this.firstTimestamp = firstTimestamp;
        this.lastTimestamp = lastTimestamp;
        this.measures = measures;
    }

    public int getCount() {
        return count;
    }

    public String getFirstTimestamp() {
        return firstTimestamp;
    }

    public String getLastTimestamp() {
        return lastTimestamp;
    }

    public List<Measure> getMeasures() {
        return measures;
    }
}
