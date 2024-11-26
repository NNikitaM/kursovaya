package ru.demo.investmentapp.controller.User;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.demo.investmentapp.model.Client;
import ru.demo.investmentapp.model.Role;
import ru.demo.investmentapp.model.User;
import ru.demo.investmentapp.service.ClientService;
import ru.demo.investmentapp.service.RoleService;
import ru.demo.investmentapp.service.UserService;
import ru.demo.investmentapp.util.Manager;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static ru.demo.investmentapp.util.Manager.*;

public class UserEditViewController implements Initializable {
    @FXML
    private Button BtnCancel;

    @FXML
    private Button BtnSave;

    private UserService userService = new UserService();
    private RoleService roleService = new RoleService();
    private ClientService clientService = new ClientService();
    private Boolean isNew = false;
    @FXML
    private TextField TextFieldUserName;

    @FXML
    private TextField TextFieldPassword;

    @FXML
    private ComboBox<Role> ComboBoxRole;

    @FXML
    private ComboBox<String> ComboBoxClient;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ComboBoxRole.setItems(FXCollections.observableArrayList(roleService.findAll()));
        List<Client> clients = clientService.findAll();

        if (clients == null) {
            MessageBox("Ошибка", "Список клиентов пуст", "Пожалуйста, проверьте данные.", Alert.AlertType.ERROR);
            return;
        }

        ObservableList<String> clientFullName = FXCollections.observableArrayList(
                clients.stream()
                        .map(Client::getFullName)
                        .distinct()
                        .collect(Collectors.toList())
        );

        ComboBoxClient.setItems(clientFullName);

        if (Manager.currentEditUser != null) {
            TextFieldUserName.setEditable(false);
            TextFieldUserName.setText(currentEditUser.getUserName());
            TextFieldPassword.setText(currentEditUser.getPassword());
            ComboBoxRole.setValue(currentEditUser.getRole());
            if (currentEditUser.getClient() != null) { // проверка на null
                ComboBoxClient.setValue(currentEditUser.getClient().getFullName());
            }
        } else {
            isNew = true;
            currentEditUser = new User();
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
        currentEditUser.setUserName(TextFieldUserName.getText());
        currentEditUser.setPassword(TextFieldPassword.getText());
        currentEditUser.setRole(ComboBoxRole.getValue());

        Client client = findClient(ComboBoxClient.getValue());
        if (client == null){
            MessageBox("Ошибка", "Клиент не найден", "Проверьте выбор клиента.", Alert.AlertType.ERROR);
            return;
        }
        currentEditUser.setClient(client);

        if (isNew) {
            currentEditUser.setUserName(TextFieldUserName.getText());
            userService.save(currentEditUser);
            MessageBox("Информация", "", "Данные сохранены успешно", Alert.AlertType.INFORMATION);
        } else {
            userService.update(currentEditUser);
            MessageBox("Информация", "", "Данные обновлены успешно", Alert.AlertType.INFORMATION);
        }
    }

    private Client findClient(String clientFullName) {
        List<Client> clients = clientService.findAll();
        for (Client client : clients) {
            if (client.getFullName().equals(clientFullName)) {
                return client;
            }
        }
        return null;
    }

    StringBuilder checkFields() {
        StringBuilder error = new StringBuilder();
        if (TextFieldUserName.getText().isEmpty()) {
            error.append("Укажите логин пользователя\n");
        }
        if (TextFieldPassword.getText().isEmpty()) {
            error.append("Укажите пароль\n");
        }
        if (ComboBoxClient.getValue() == null) {
            error.append("Выберите клиента\n");
        }

        return error;
    }
}
