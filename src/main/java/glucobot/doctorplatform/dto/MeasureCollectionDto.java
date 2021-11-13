package glucobot.doctorplatform.dto;

import glucobot.doctorplatform.model.MeasureCollection;

import java.util.List;
import java.util.stream.Collectors;

public class MeasureCollectionDto {
    private int count;
    private String firstTimestamp, lastTimestamp;
    private List<MeasureDto> measures;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getFirstTimestamp() {
        return firstTimestamp;
    }

    public void setFirstTimestamp(String firstTimestamp) {
        this.firstTimestamp = firstTimestamp;
    }

    public String getLastTimestamp() {
        return lastTimestamp;
    }

    public void setLastTimestamp(String lastTimestamp) {
        this.lastTimestamp = lastTimestamp;
    }

    public List<MeasureDto> getMeasures() {
        return measures;
    }

    public void setMeasures(List<MeasureDto> measures) {
        this.measures = measures;
    }

    public MeasureCollection toMeasureCollection() {
        return new MeasureCollection(count, firstTimestamp, lastTimestamp, measures.stream().map(MeasureDto::toMeasure).collect(Collectors.toList()));
    }
}
