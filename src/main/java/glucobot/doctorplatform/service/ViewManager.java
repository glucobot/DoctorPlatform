package glucobot.doctorplatform.service;

import glucobot.doctorplatform.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ViewManager {

    private final Stage stage;

    public ViewManager(Stage stage) {
        this.stage = stage;
    }

    public void loadLoginPage() throws IOException {
        loadScene("login-view.fxml");
    }

    public void loadDoctorView() throws IOException {
        loadScene("doctor-view.fxml");
    }


    private void loadScene(String file) throws IOException {
        Scene scene = new Scene(FXMLLoader.load(Objects.requireNonNull(Main.class.getResource(file))));
        stage.setScene(scene);
    }


}

