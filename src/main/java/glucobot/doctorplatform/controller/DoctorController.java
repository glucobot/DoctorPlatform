package glucobot.doctorplatform.controller;

import glucobot.doctorplatform.model.Patient;
import glucobot.doctorplatform.service.http.BasicAuthentication;
import glucobot.doctorplatform.service.http.HttpService;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DoctorController implements Initializable {

    @FXML
    public TableView<Patient> patientsTable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupPatientsTable();

        try {
            loadPatients();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupPatientsTable() {
        var columns = patientsTable.getColumns();

        TableColumn<Patient, String> column = new TableColumn<>("Last Name");
        column.setCellValueFactory(patient -> new SimpleStringProperty(patient.getValue().getLastName()));
        columns.add(column);

        column = new TableColumn<>("First Name");
        column.setCellValueFactory(patient -> new SimpleStringProperty(patient.getValue().getFirstName()));
        columns.add(column);

        patientsTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> onPatientSelected(newSelection)
        );

    }

    private void loadPatients() throws IOException {
        HttpService httpService = new HttpService("https://glucobot.ew.r.appspot.com", new BasicAuthentication("carl", "carl"));
        var t = httpService.get("patients", Patient.class);
    }

    private void onPatientSelected(Patient newSelection) {
        System.out.println(newSelection);
    }
}
