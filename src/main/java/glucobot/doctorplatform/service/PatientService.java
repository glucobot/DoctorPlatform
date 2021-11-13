package glucobot.doctorplatform.service;

import glucobot.doctorplatform.dto.MeasureCollectionDto;
import glucobot.doctorplatform.model.MeasureCollection;
import glucobot.doctorplatform.model.User;
import glucobot.doctorplatform.service.http.HttpService;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class PatientService {

    private final HttpService httpService;

    public PatientService() {
        httpService = DI.get(HttpService.class);
    }

    public User[] getPatients() {
        try {
            return httpService.get("patients", User[].class);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public MeasureCollection getMeasuresForPatient(User user) {
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, -3);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return getMeasuresForPatient(user, format.format(calendar.getTime()));
    }

    public MeasureCollection getMeasuresForPatient(User user, String minTimestamp) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("minTimestamp", minTimestamp);
            MeasureCollectionDto measureCollectionDto = httpService.get("patients/" + user.getId() + "/measures", params, MeasureCollectionDto.class);
            return measureCollectionDto.toMeasureCollection();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}

