package ru.demo.investmentapp.controller.Account;

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
import ru.demo.investmentapp.model.Account;
import ru.demo.investmentapp.model.Client;
import ru.demo.investmentapp.service.AccountService;
import ru.demo.investmentapp.service.ClientService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static java.lang.Double.parseDouble;
import static ru.demo.investmentapp.util.Manager.*;

public class AccountEditViewController implements Initializable {
    @FXML
    private Button BtnCancel;

    @FXML
    private Button BtnSave;

    private AccountService accountService = new AccountService();
    private ClientService clientService = new ClientService();

    @FXML
    private TextField TextFieldAccountId;

    @FXML
    private TextField TextFieldType;

    @FXML
    private TextField TextFieldBalance;

    @FXML
    private ComboBox<String> ComboBoxClient;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

        if (currentAccount != null) {
            TextFieldType.setText(currentAccount.getAccountType());
            TextFieldBalance.setText(currentAccount.getBalance().toString());
            if (currentAccount.getClient() != null) { // проверка на null
                ComboBoxClient.setValue(currentAccount.getClient().getFullName());
            }
        } else {
            currentAccount = new Account();
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

        currentAccount.setAccountType(TextFieldType.getText());
        currentAccount.setBalance(parseDouble(TextFieldBalance.getText()));

        Client client = findClient(ComboBoxClient.getValue());
        if (client == null){
            MessageBox("Ошибка", "Клиент не найден", "Проверьте выбор клиента.", Alert.AlertType.ERROR);
            return;
        }
        currentAccount.setClient(client);

        if (currentAccount.getAccountId() == null) {
            accountService.save(currentAccount);
            MessageBox("Информация", "", "Данные сохранены успешно", Alert.AlertType.INFORMATION);
        } else {
            accountService.update(currentAccount);
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
        if (TextFieldType.getText().isEmpty()) {
            error.append("Укажите тип аккаунта\n");
        }
        String balanceText = TextFieldBalance.getText();
        if (balanceText.isEmpty()) {
            error.append("Укажите баланс\n");
        } else {
            try {
                Double.parseDouble(balanceText);
            } catch (NumberFormatException e) {
                error.append("Баланс должен быть числом (например, 100 или 100.50)\n");
            }
        }
        if (ComboBoxClient.getValue() == null) {
            error.append("Выберите клиента\n");
        }

        return error;
    }
}
