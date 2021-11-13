package glucobot.doctorplatform;

import glucobot.doctorplatform.service.AuthenticationService;
import glucobot.doctorplatform.service.DI;
import glucobot.doctorplatform.service.PatientService;
import glucobot.doctorplatform.service.ViewManager;
import glucobot.doctorplatform.service.http.BasicAuthentication;
import glucobot.doctorplatform.service.http.HttpAuthorizationProvider;
import glucobot.doctorplatform.service.http.HttpService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        DI.register(ViewManager.class, new ViewManager(stage));

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Glucobot");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        DI.register(HttpService.class, new HttpService("https://glucobot.ew.r.appspot.com"));

        DI.register(PatientService.class, new PatientService());
        DI.register(AuthenticationService.class, new AuthenticationService());

        launch();
    }
}