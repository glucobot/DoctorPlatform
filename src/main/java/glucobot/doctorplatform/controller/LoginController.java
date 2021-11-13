package glucobot.doctorplatform.controller;

import glucobot.doctorplatform.service.AuthenticationService;
import glucobot.doctorplatform.service.DI;
import glucobot.doctorplatform.service.ViewManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private Label errorLabel;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;

    private final AuthenticationService authenticationService;

    public LoginController() {
        authenticationService = DI.get(AuthenticationService.class);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        errorLabel.setText("");
    }

    public void onLoginClick() {
        errorLabel.setText("");

        String username = usernameTextField.getText();
        String password = passwordTextField.getText();

        AuthenticationService.AuthenticationResult result = authenticationService.login(username, password);

        switch (result) {
            case WrongCredentials -> errorLabel.setText("Wrong credentials");
            case Fail -> errorLabel.setText("An error has occurred!");
            case Success -> goToDoctorView();
        }
    }

    public void goToDoctorView() {
        ViewManager vm = DI.get(ViewManager.class);
        try {
            vm.loadDoctorView();
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("An error has occurred!");
        }
    }
}
