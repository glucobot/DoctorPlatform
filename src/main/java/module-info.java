module glucobot.doctorplatform.doctorplatform {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens glucobot.doctorplatform to javafx.fxml;
    exports glucobot.doctorplatform;
    exports glucobot.doctorplatform.controller;
    opens glucobot.doctorplatform.controller to javafx.fxml;
}