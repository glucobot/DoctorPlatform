package glucobot.doctorplatform.controller;

import glucobot.doctorplatform.model.Measure;
import glucobot.doctorplatform.model.MeasureCollection;
import glucobot.doctorplatform.model.User;
import glucobot.doctorplatform.service.AuthenticationService;
import glucobot.doctorplatform.service.DI;
import glucobot.doctorplatform.service.PatientService;
import glucobot.doctorplatform.service.ViewManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.ValueAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Duration;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class DoctorController implements Initializable {

    private static final long UPDATE_INTERVAL = 5;

    @FXML
    public TableView<User> patientsTable;
    @FXML
    public LineChart<Double, Double> chart;
    @FXML
    private Label nameLabel;

    private final PatientService patientService;
    private final User user;

    private UpdateChartTask updateChartTask;

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

        XYChart.Series<Double, Double> glycemiaSeries = new XYChart.Series<>();
        glycemiaSeries.setName("Glycemia");
        chart.getData().addAll(glycemiaSeries);

        updateChartTask = new UpdateChartTask(glycemiaSeries);
        updateChartTask.setPeriod(Duration.seconds(UPDATE_INTERVAL));
        updateChartTask.setOnSucceeded(e -> {

                    MeasureCollection measureCollection = updateChartTask.getValue();

                    for (Measure measure : measureCollection.getMeasures()) {
                        glycemiaSeries.getData().add(new XYChart.Data<>(measure.getTimestamp(), measure.getGlycemia()));
                    }
                }
        );
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

        stopUpdateChartTask();

        if (newSelection == null) {
            chart.setVisible(false);
            return;
        }

        chart.setVisible(true);

        startUpdateChartTask(newSelection);
    }

    private void startUpdateChartTask(User user) {
        stopUpdateChartTask();

        updateChartTask.reset(user);

        updateChartTask.start();
    }

    private void stopUpdateChartTask() {
        updateChartTask.cancel();
    }

    public void onLogoutClick() {

        stopUpdateChartTask();

        AuthenticationService authenticationService = DI.get(AuthenticationService.class);
        authenticationService.logout();
        ViewManager vm = DI.get(ViewManager.class);
        try {
            vm.loadLoginPage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class UpdateChartTask extends ScheduledService<MeasureCollection> {

        private User user;
        private String lastTimestamp;
        private final XYChart.Series<Double, Double> glycemiaSeries;

        private final PatientService patientService;

        public UpdateChartTask(XYChart.Series<Double, Double> glycemiaSeries) {
            patientService = DI.get(PatientService.class);

            this.glycemiaSeries = glycemiaSeries;

        }

        public void reset(User user) {
            this.user = user;

            Calendar calendar = new GregorianCalendar();
            calendar.add(Calendar.DATE, -3);
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            this.lastTimestamp = format.format(calendar.getTime());

            glycemiaSeries.getData().clear();

            this.reset();
        }

        @Override
        protected Task<MeasureCollection> createTask() {
            return new Task<>() {
                @Override
                protected MeasureCollection call() {
                    MeasureCollection measureCollection = patientService.getMeasuresForPatient(user, lastTimestamp);

                    lastTimestamp = measureCollection.getLastTimestamp();

                    return measureCollection;
                }
            };
        }
    }
}
