module glucobot.doctorplatform.doctorplatform {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires com.fasterxml.jackson.databind;
    requires java.net.http;
    exports glucobot.doctorplatform;
    exports glucobot.doctorplatform.controller;
    exports glucobot.doctorplatform.model;
    opens glucobot.doctorplatform.controller to javafx.fxml;
    opens glucobot.doctorplatform.model to com.fasterxml.jackson.databind;
    exports glucobot.doctorplatform.dto;
    opens glucobot.doctorplatform.dto to com.fasterxml.jackson.databind;
}