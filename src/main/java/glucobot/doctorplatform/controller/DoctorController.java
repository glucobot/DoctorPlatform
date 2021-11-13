package glucobot.doctorplatform.controller;

import glucobot.doctorplatform.model.Measure;
import glucobot.doctorplatform.model.MeasureCollection;
import glucobot.doctorplatform.model.User;
import glucobot.doctorplatform.service.AuthenticationService;
import glucobot.doctorplatform.service.DI;
import glucobot.doctorplatform.service.PatientService;
import glucobot.doctorplatform.service.ViewManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.ValueAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class DoctorController implements Initializable {

    @FXML
    public TableView<User> patientsTable;
    @FXML
    public LineChart<Double, Double> chart;
    @FXML
    private Label nameLabel;

    private XYChart.Series<Double, Double> insulinSeries;
    private XYChart.Series<Double, Double> glycemiaSeries;

    private final PatientService patientService;
    private final User user;

    public DoctorController() {
        patientService = DI.get(PatientService.class);
        user = DI.get(User.class);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        nameLabel.setText(user.getFullName());

        setupPatientsTable();

        loadPatients();

        setupChart();
    }

    private void setupPatientsTable() {
        var columns = patientsTable.getColumns();

        TableColumn<User, String> column = new TableColumn<>("Last Name");
        column.setCellValueFactory(patient -> new SimpleStringProperty(patient.getValue().getLastName()));
        columns.add(column);

        column = new TableColumn<>("First Name");
        column.setCellValueFactory(patient -> new SimpleStringProperty(patient.getValue().getFirstName()));
        columns.add(column);

        patientsTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> onPatientSelected(newSelection)
        );

    }

    private void setupChart() {
        ValueAxis<Double> xAxis = (ValueAxis<Double>) chart.getXAxis();
        xAxis.setTickLabelFormatter(new StringConverter<>() {
            @Override
            public String toString(Double ticks) {

                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(ticks.longValue());

                return format.format(date);
            }

            @Override
            public Double fromString(String s) {
                return null;
            }
        });

        insulinSeries = new XYChart.Series<>();
        insulinSeries.setName("Insulin");

        glycemiaSeries = new XYChart.Series<>();
        glycemiaSeries.setName("Glycemia");

        chart.getData().addAll(glycemiaSeries, insulinSeries);
    }

    private void populatePatients(User... patients) {
        var items = patientsTable.getItems();

        items.clear();

        patientsTable.getItems().addAll(patients);
    }

    private void loadPatients() {
        User[] users = patientService.getPatients();
        populatePatients(users);
    }

    private void onPatientSelected(User newSelection) {

        if (newSelection == null) {
            chart.setVisible(false);
            insulinSeries.getData().clear();
            glycemiaSeries.getData().clear();
            return;
        }

        chart.setVisible(true);

        MeasureCollection mc = this.patientService.getMeasuresForPatient(newSelection);

        for (Measure measure : mc.getMeasures()) {
            insulinSeries.getData().add(new XYChart.Data<>(measure.getTimestamp(), measure.getInsulin()));
            glycemiaSeries.getData().add(new XYChart.Data<>(measure.getTimestamp(), measure.getGlycemia()));
        }
    }

    public void onLogoutClick() {
        AuthenticationService authenticationService = DI.get(AuthenticationService.class);
        authenticationService.logout();
        ViewManager vm = DI.get(ViewManager.class);
        try {
            vm.loadLoginPage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
