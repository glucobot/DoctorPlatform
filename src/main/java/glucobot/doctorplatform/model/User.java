package glucobot.doctorplatform.model;

public class User {

    protected long id;
    protected String firstName;
    protected String lastName;

    public long getId() {
        return id;
    }


    public String getFirstName() {
        return firstName;
    }


    public String getLastName() {
        return lastName;
    }

    public String getFullName(){
        return firstName + " " + lastName.toUpperCase();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
