package glucobot.doctorplatform.dto;

import glucobot.doctorplatform.model.Measure;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class MeasureDto {
    private String timestamp;
    private double insulin;
    private double glycemia;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public double getInsulin() {
        return insulin;
    }

    public void setInsulin(double insulin) {
        this.insulin = insulin;
    }

    public double getGlycemia() {
        return glycemia;
    }

    public void setGlycemia(double glycemia) {
        this.glycemia = glycemia;
    }

    public Measure toMeasure() {

        DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date date = null;
        try {
            date = utcFormat.parse(timestamp);
        } catch (ParseException e) {
            e.printStackTrace();
            date = new Date(0);
        }

        return new Measure(date.getTime(), glycemia, insulin);
    }
}
