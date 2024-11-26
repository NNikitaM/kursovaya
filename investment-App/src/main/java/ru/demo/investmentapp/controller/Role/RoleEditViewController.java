package ru.demo.investmentapp.controller.Role;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.demo.investmentapp.model.Role;
import ru.demo.investmentapp.service.RoleService;
import ru.demo.investmentapp.util.Manager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static ru.demo.investmentapp.util.Manager.MessageBox;

public class RoleEditViewController implements Initializable {
    @FXML
    private Button BtnSave;
    @FXML
    private Button BtnCancel;
    private RoleService roleService = new RoleService();

    @FXML
    private TextField TextFieldTitle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (Manager.currentRole != null) {
            TextFieldTitle.setText(Manager.currentRole.getTitle());
        } else {
            Manager.currentRole = new Role();
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
        Manager.currentRole.setTitle(TextFieldTitle.getText());
        if (Manager.currentRole.getRoleId() == null) {
            Manager.currentRole.setTitle(TextFieldTitle.getText());
            roleService.save(Manager.currentRole);
            MessageBox("Информация", "", "Данные сохранены успешно", Alert.AlertType.INFORMATION);
        } else {
            roleService.update(Manager.currentRole);
            MessageBox("Информация", "", "Данные обновлены успешно", Alert.AlertType.INFORMATION);
        }
    }

    StringBuilder checkFields() {
        StringBuilder error = new StringBuilder();
        if (TextFieldTitle.getText().isEmpty()) {
            error.append("Укажите название\n");
        }
        return error;
    }
}
