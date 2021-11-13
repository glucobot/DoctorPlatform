package glucobot.doctorplatform.model;

public class UserWithCategory extends User {

    protected Category category;

    public Category getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "UserWithCategory{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", category=" + category +
                '}';
    }
}
