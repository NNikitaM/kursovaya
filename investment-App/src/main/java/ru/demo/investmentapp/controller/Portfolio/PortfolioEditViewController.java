package ru.demo.investmentapp.controller.Portfolio;

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
import ru.demo.investmentapp.model.Portfolio;
import ru.demo.investmentapp.model.User;
import ru.demo.investmentapp.service.PortfolioService;
import ru.demo.investmentapp.service.UserService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static ru.demo.investmentapp.util.Manager.*;

public class PortfolioEditViewController implements Initializable {
    @FXML
    private Button BtnCancel;

    @FXML
    private Button BtnSave;

    private PortfolioService portfolioService = new PortfolioService();
    private UserService userService  = new UserService();

    @FXML
    private TextField TextFieldPortfolioType;

    @FXML
    private TextField TextFieldPortfolioName;

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

        if (currentPortfolio != null) {
            TextFieldPortfolioName.setText(currentPortfolio.getPortfolioName());
            TextFieldPortfolioType.setText(currentPortfolio.getPortfolioType());
            if (currentPortfolio.getUser() != null) { // проверка на null
                ComboBoxUser.setValue(currentPortfolio.getUser().getUserName());
            }
        } else {
            currentPortfolio = new Portfolio();
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

        currentPortfolio.setPortfolioType(TextFieldPortfolioType.getText());
        currentPortfolio.setPortfolioName(TextFieldPortfolioName.getText());

        User user = findUser(ComboBoxUser.getValue());
        if (user == null){
            MessageBox("Ошибка", "Пользователь не найден", "Проверьте выбор пользователя.", Alert.AlertType.ERROR);
            return;
        }
        currentPortfolio.setUser(user);

        if (currentPortfolio.getPortfolioId() == null) {
            portfolioService.save(currentPortfolio);
            MessageBox("Информация", "", "Данные сохранены успешно", Alert.AlertType.INFORMATION);
        } else {
            portfolioService.update(currentPortfolio);
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
        if (TextFieldPortfolioName.getText().isEmpty()) {
            error.append("Укажите название\n");
        }
        if (ComboBoxUser.getValue() == null) {
            error.append("Выберите логин пользователя\n");
        }

        return error;
    }
}
