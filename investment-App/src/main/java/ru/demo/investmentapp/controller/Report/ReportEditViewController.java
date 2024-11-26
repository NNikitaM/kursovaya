package ru.demo.investmentapp.controller.Report;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ru.demo.investmentapp.model.*;
import ru.demo.investmentapp.service.*;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static ru.demo.investmentapp.util.Manager.*;

public class ReportEditViewController implements Initializable {
    @FXML
    private Button BtnCancel;

    @FXML
    private Button BtnSave;

    private ReportService reportService = new ReportService();
    private UserService userService  = new UserService();

    @FXML
    private TextField TextFieldType;

    @FXML
    private TextField TextFieldContent;

    @FXML
    private DatePicker DatePickerDate;

    @FXML
    private ComboBox<String> ComboBoxUser;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<User> users = userService.findAll();

        if (users == null) {
            MessageBox("Ошибка", "Список пользователей пуст", "Пожалуйста, проверьте данные.", Alert.AlertType.ERROR);
            return;
        }

        ObservableList<String> userName = FXCollections.observableArrayList(
                users.stream()
                        .map(User::getUserName)
                        .distinct()
                        .collect(Collectors.toList())
        );

        ComboBoxUser.setItems(userName);

        if (currentReport != null) {
            DatePickerDate.setValue(currentReport.getReportDate().toLocalDate());
            TextFieldContent.setText(currentReport.getReportContent());
            TextFieldType.setText(currentReport.getReportType());
            if (currentReport.getUser() != null) { // проверка на null
                ComboBoxUser.setValue(currentReport.getUser().getUserName());
            }
        } else {
            currentReport = new Report();
        }
    }

    @FXML
    void BtnCancelAction(ActionEvent event) {
        Stage stage = (Stage) BtnCancel.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

    @FXML
    void BtnSaveAction(ActionEvent event) throws IOException {
        String error = checkFields().toString();
        if (!error.isEmpty()) {
            MessageBox("Ошибка", "Заполните поля", error, Alert.AlertType.ERROR);
            return;
        }

        currentReport.setReportContent(TextFieldContent.getText());
        currentReport.setReportDate(Date.valueOf(DatePickerDate.getValue()));
        currentReport.setReportType(TextFieldType.getText());

        User user = findUser(ComboBoxUser.getValue());
        if (user == null){
            MessageBox("Ошибка", "Пользователь не найден", "Проверьте выбор пользователя.", Alert.AlertType.ERROR);
            return;
        }
        currentReport.setUser(user);

        if (currentReport.getReportId() == null) {
            reportService.save(currentReport);
            MessageBox("Информация", "", "Данные сохранены успешно", Alert.AlertType.INFORMATION);
        } else {
            reportService.update(currentReport);
            MessageBox("Информация", "", "Данные обновлены успешно", Alert.AlertType.INFORMATION);
        }
    }

    private User findUser(String userName) {
        List<User> users = userService.findAll();
        for (User user: users) {
            if (user.getUserName().equals(userName)) {
                return user;
            }
        }
        return null;
    }

    StringBuilder checkFields() {
        StringBuilder error = new StringBuilder();
        if (TextFieldType.getText().isEmpty()) {
            error.append("Укажите тип отчета\n");
        }
        if (DatePickerDate.getValue() == null) {
            error.append("Укажите дату\n");
        }
        if (ComboBoxUser.getValue() == null) {
            error.append("Выберите логин пользователя\n");
        }

        return error;
    }
}
