package ru.demo.investmentapp.controller.Transaction;

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

import static java.lang.Double.parseDouble;
import static ru.demo.investmentapp.util.Manager.*;

public class TransactionEditViewController implements Initializable {
    @FXML
    private Button BtnCancel;

    @FXML
    private Button BtnSave;

    private TransactionService transactionService = new TransactionService();
    private InstrumentService instrumentService = new InstrumentService();
    private AccountService accountService = new AccountService();

    @FXML
    private TextField TextFieldTransactionType;

    @FXML
    private TextField TextFieldAmount;

    @FXML
    private DatePicker DatePickerDate;

    @FXML
    private ComboBox<String> ComboBoxInstrument;

    @FXML
    private ComboBox<String> ComboBoxAccount;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Instrument> instruments = instrumentService.findAll();

        if (instruments == null) {
            MessageBox("Ошибка", "Список инструментов пуст", "Пожалуйста, проверьте данные.", Alert.AlertType.ERROR);
            return;
        }

        ObservableList<String> instrument = FXCollections.observableArrayList(
                instruments.stream()
                        .map(Instrument::getInstrumentType)
                        .distinct()
                        .collect(Collectors.toList())
        );

        ComboBoxInstrument.setItems(instrument);


        List<Account> accounts = accountService.findAll();

        if (accounts == null) {
            MessageBox("Ошибка", "Список аккаунтов пуст", "Пожалуйста, проверьте данные.", Alert.AlertType.ERROR);
            return;
        }

        ObservableList<String> account = FXCollections.observableArrayList(
                accounts.stream()
                        .map(Account::getAccountType)
                        .distinct()
                        .collect(Collectors.toList())
        );
        ComboBoxAccount.setItems(account);

        if (currentTransaction != null) {
            TextFieldTransactionType.setText(currentTransaction.getTransactionType());
            TextFieldAmount.setText(String.valueOf(currentTransaction.getAmount()));
            DatePickerDate.setValue(currentTransaction.getTransactionDate().toLocalDate());
            if (currentTransaction.getInstrument() != null) { // проверка на null
                ComboBoxInstrument.setValue(currentTransaction.getInstrument().getInstrumentType());
            }
            if (currentTransaction.getAccount() != null) { // проверка на null
                ComboBoxAccount.setValue(currentTransaction.getAccount().getAccountType());
            }
        } else {
            currentTransaction = new Transaction();
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
        currentTransaction.setTransactionDate(Date.valueOf(DatePickerDate.getValue()));
        currentTransaction.setTransactionType(TextFieldTransactionType.getText());
        currentTransaction.setAmount(parseDouble(TextFieldAmount.getText()));

        Instrument instrument = findInstrument(ComboBoxInstrument.getValue());
        if (instrument == null){
            MessageBox("Ошибка", "Инструмент не найдена", "Проверьте выбор инструмента.", Alert.AlertType.ERROR);
            return;
        }
        currentTransaction.setInstrument(instrument);

        Account account = findAccount(ComboBoxAccount.getValue());
        if (account == null){
            MessageBox("Ошибка", "Инструмент не найдена", "Проверьте выбор инструмента.", Alert.AlertType.ERROR);
            return;
        }
        currentTransaction.setAccount(account);

        if (currentTransaction.getTransactionId() == null) {
            transactionService.save(currentTransaction);
            MessageBox("Информация", "", "Данные сохранены успешно", Alert.AlertType.INFORMATION);
        } else {
            transactionService.update(currentTransaction);
            MessageBox("Информация", "", "Данные обновлены успешно", Alert.AlertType.INFORMATION);
        }
    }

    private Instrument findInstrument(String instrumentType) {
        List<Instrument> instruments = instrumentService.findAll();
        for (Instrument instrument : instruments) {
            if (instrument.getInstrumentType().equals(instrumentType)) {
                return instrument;
            }
        }
        return null;
    }

    private Account findAccount(String accountType) {
        List<Account> accounts = accountService.findAll();
        for (Account account : accounts) {
            if (account.getAccountType().equals(accountType)) {
                return account;
            }
        }
        return null;
    }

    StringBuilder checkFields() {
        StringBuilder error = new StringBuilder();
        if (DatePickerDate.getValue() == null) {
            error.append("Укажите дату\n");
        }
        String amountText = TextFieldAmount.getText();
        if (amountText.isEmpty()) {
            error.append("Укажите стоимость\n");
        } else {
            try {
                Double.parseDouble(amountText);
            } catch (NumberFormatException e) {
                error.append("Стоимость должна быть числом\n");
            }
        }
        if (TextFieldTransactionType.getText().isEmpty()) {
            error.append("Укажите тип операции\n");
        }
        if (ComboBoxAccount.getValue() == null) {
            error.append("Выберите аккаунт\n");
        }
        if (ComboBoxInstrument.getValue() == null) {
            error.append("Выберите тип инструмента\n");
        }
        return error;
    }
}
