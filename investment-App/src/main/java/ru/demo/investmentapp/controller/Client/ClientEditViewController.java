package ru.demo.investmentapp.controller.Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.demo.investmentapp.model.Client;
import ru.demo.investmentapp.service.ClientService;
import ru.demo.investmentapp.util.Manager;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

import static ru.demo.investmentapp.util.Manager.MessageBox;

public class ClientEditViewController implements Initializable {
    @FXML
    private Button BtnSave;
    @FXML
    private Button BtnCancel;
    private ClientService clientService = new ClientService();

    @FXML
    private TextField TextFieldFirstName;
    @FXML
    private TextField TextFieldLastName;
    @FXML
    private TextField TextFieldMiddleName;
    @FXML
    private TextField TextFieldAddress;
    @FXML
    private TextField TextFieldEmail;
    @FXML
    private TextField TextFieldPhone;
    @FXML
    private TextField TextFieldClientType;
    @FXML
    private DatePicker DatePickerBirthday;
    @FXML
    private TextField TextFieldPassportNumber;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (Manager.currentClient != null) {
            TextFieldFirstName.setText(Manager.currentClient.getFirstName());
            TextFieldLastName.setText(Manager.currentClient.getLastName());
            TextFieldMiddleName.setText(Manager.currentClient.getMiddleName());
            TextFieldAddress.setText(Manager.currentClient.getAddress());
            TextFieldEmail.setText(Manager.currentClient.getEmail());
            TextFieldPhone.setText(Manager.currentClient.getPhone());
            DatePickerBirthday.setValue(Manager.currentClient.getDateOfBirth());
            TextFieldClientType.setText(Manager.currentClient.getClientType());
            TextFieldPassportNumber.setText(Manager.currentClient.getPassportNumber());
        } else {
            Manager.currentClient = new Client();
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
        Manager.currentClient.setFirstName(TextFieldFirstName.getText());
        Manager.currentClient.setLastName(TextFieldLastName.getText());
        Manager.currentClient.setMiddleName(TextFieldMiddleName.getText());
        Manager.currentClient.setAddress(TextFieldAddress.getText());
        Manager.currentClient.setEmail(TextFieldEmail.getText());
        Manager.currentClient.setPhone(TextFieldPhone.getText());
        Manager.currentClient.setClientType(TextFieldClientType.getText());
        Manager.currentClient.setDateOfBirth(DatePickerBirthday.getValue());
        Manager.currentClient.setPassportNumber(TextFieldPassportNumber.getText());
        if (Manager.currentClient.getClientId() == null) {
            Manager.currentClient.setFirstName(TextFieldFirstName.getText());
            clientService.save(Manager.currentClient);
            MessageBox("Информация", "", "Данные сохранены успешно", Alert.AlertType.INFORMATION);
        } else {
            clientService.update(Manager.currentClient);
            MessageBox("Информация", "", "Данные обновлены успешно", Alert.AlertType.INFORMATION);
        }
    }

    StringBuilder checkFields() {
        StringBuilder error = new StringBuilder();
        if (TextFieldFirstName.getText().isEmpty()) {
            error.append("Укажите фамилию\n");
        }
        if (TextFieldLastName.getText().isEmpty()) {
            error.append("Укажите имя\n");
        }
        if (TextFieldAddress.getText().isEmpty()) {
            error.append("Укажите адрес\n");
        }
        if (TextFieldEmail.getText().isEmpty()) {
            error.append("Укажите email\n");
        } else if (!TextFieldEmail.getText().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            error.append("Неверный формат email\n");
        }
        if (DatePickerBirthday.getValue() == null) {
            error.append("Укажите дату рождения\n");
        }
        if (TextFieldEmail.getText().isEmpty()) {
            error.append("Укажите email\n");
        }
        if (TextFieldPhone.getText().isEmpty()) {
            error.append("Укажите телефон\n");
        }
        if (TextFieldPassportNumber.getText().isEmpty()) {
            error.append("Укажите паспорт\n");
        }
        if (TextFieldClientType.getText().isEmpty()) {
            error.append("Укажите тип клиента\n");
        }
        return error;
    }
}
