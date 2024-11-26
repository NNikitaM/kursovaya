package ru.demo.investmentapp.controller.Commission;

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
import ru.demo.investmentapp.model.*;
import ru.demo.investmentapp.service.CommissionService;
import ru.demo.investmentapp.service.TransactionService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static java.lang.Double.parseDouble;
import static ru.demo.investmentapp.util.Manager.*;

public class CommissionEditViewController implements Initializable {
    @FXML
    private Button BtnCancel;

    @FXML
    private Button BtnSave;

    private CommissionService commissionService = new CommissionService();
    private TransactionService transactionService = new TransactionService();

    @FXML
    private TextField TextFieldType;

    @FXML
    private TextField TextFieldAmount;

    @FXML
    private ComboBox<String> ComboBoxTransaction;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Transaction> transactions = transactionService.findAll();

        if (transactions == null) {
            MessageBox("Ошибка", "Список операций пуст", "Пожалуйста, проверьте данные.", Alert.AlertType.ERROR);
            return;
        }

        ObservableList<String> transaction = FXCollections.observableArrayList(
                transactions.stream()
                        .map(Transaction::getTransactionType)
                        .distinct()
                        .collect(Collectors.toList())
        );

        ComboBoxTransaction.setItems(transaction);

        if (currentCommission != null) {
            TextFieldType.setText(currentCommission.getCommissionType());
            TextFieldAmount.setText(currentCommission.getAmount().toString());
            if (currentCommission.getTransaction() != null) { // проверка на null
                ComboBoxTransaction.setValue(currentCommission.getTransaction().getTransactionType());
            }
        } else {
            currentCommission = new Commission();
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

        currentCommission.setCommissionType(TextFieldType.getText());
        currentCommission.setAmount(parseDouble(TextFieldAmount.getText()));

        Transaction transaction = findTransaction(ComboBoxTransaction.getValue());
        if (transaction == null) {
            MessageBox("Ошибка", "Операция не найдена", "Проверьте выбор операции.", Alert.AlertType.ERROR);
            return;
        }
        currentCommission.setTransaction(transaction);

        if (currentCommission.getCommissionId() == null) {
            commissionService.save(currentCommission);
            MessageBox("Информация", "", "Данные сохранены успешно", Alert.AlertType.INFORMATION);
        } else {
            commissionService.update(currentCommission);
            MessageBox("Информация", "", "Данные обновлены успешно", Alert.AlertType.INFORMATION);
        }
    }

    private Transaction findTransaction(String transactionType) {
        List<Transaction> transactions = transactionService.findAll();
        for (Transaction transaction : transactions) {
            if (transaction.getTransactionType().equals(transactionType)) {
                return transaction;
            }
        }
        return null;
    }

    StringBuilder checkFields() {
        StringBuilder error = new StringBuilder();
        if (TextFieldType.getText().isEmpty()) {
            error.append("Укажите тип аккаунта\n");
        }
        String amountText = TextFieldAmount.getText();
        if (amountText.isEmpty()) {
            error.append("Укажите сумму комиссии\n");
        } else {
            try {
                Double.parseDouble(amountText);
            } catch (NumberFormatException e) {
                error.append("Сумма комиссии должна быть числом\n");
            }
            if (ComboBoxTransaction.getValue() == null) {
                error.append("Выберите тип операции\n");
            }
        }
            return error;
        }
}
