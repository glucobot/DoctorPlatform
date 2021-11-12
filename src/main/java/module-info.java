module glucobot.doctorplatform.doctorplatform {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens glucobot.doctorplatform.doctorplatform to javafx.fxml;
    exports glucobot.doctorplatform.doctorplatform;
}