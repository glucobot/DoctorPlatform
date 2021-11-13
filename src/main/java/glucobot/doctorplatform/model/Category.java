package glucobot.doctorplatform.model;

public class Category {

    public static final int PATIENT = 1;
    public static final int DOCTOR = 2;

    protected long id;
    protected String name;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
