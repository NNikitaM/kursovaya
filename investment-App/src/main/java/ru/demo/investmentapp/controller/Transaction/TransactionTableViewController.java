package ru.demo.investmentapp.controller.Transaction;

import com.itextpdf.text.DocumentException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import ru.demo.investmentapp.model.Transaction;
import ru.demo.investmentapp.service.TransactionService;
import ru.demo.investmentapp.util.Manager;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static ru.demo.investmentapp.util.Manager.*;

public class TransactionTableViewController implements Initializable {

    private int itemsCount;
    private TransactionService transactionService = new TransactionService();

    @FXML
    private Button BtnBack;

    @FXML
    private TableColumn<Transaction, String> TableColumnTransactionId;

    @FXML
    private TableColumn<Transaction, String> TableColumnTransactionType;

    @FXML
    private TableColumn<Transaction, String> TableColumnAmount;

    @FXML
    private TableColumn<Transaction, String> TableColumnDate;

    @FXML
    private TableColumn<Transaction, String> TableColumnInstrument;

    @FXML
    private TableColumn<Transaction, String> TableColumnAccount;

    @FXML
    private TableColumn<Transaction, String> TableColumnAccountClient;

    @FXML
    private Label LabelInfo;
    @FXML
    private Label LabelUser;
    @FXML
    private TextField TextFieldSearch;


    @FXML
    private TableView<Transaction> TableViewTransactions;

    @FXML
    void TextFieldSearchAction(ActionEvent event) {
        filterData();
    }

    @FXML
    void MenuItemCommissionAction(ActionEvent event) {
        Manager.LoadSecondStageScene("Commission/commission-table-view.fxml", "Комиссии");
    }

    @FXML
    void MenuItemAddAction(ActionEvent event) {
        currentTransaction = null;
        ShowEditWindow("Transaction/transaction-edit-view.fxml");
        filterData();
    }

    @FXML
    void MenuItemBackAction(ActionEvent event) {
        Manager.LoadSecondStageScene("main-view.fxml", "Aurora Growth");
    }

    @FXML
    void MenuItemDeleteAction(ActionEvent event) {
        Transaction transaction = TableViewTransactions.getSelectionModel().getSelectedItem();

        Optional<ButtonType> result = ShowConfirmPopup();
        if (result.get() == ButtonType.OK) {
            transactionService.delete(transaction);
            filterData();
        }
    }

    @FXML
    void MenuItemUpdateAction(ActionEvent event) {
        Transaction transaction = TableViewTransactions.getSelectionModel().getSelectedItem();
        currentTransaction = transaction;
        ShowEditWindow("Transaction/transaction-edit-view.fxml");
        filterData();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initController();
    }

    public void initController() {
        LabelUser.setText("Вы вошли как " + currentUser.getClient().getFirstName() + " " +
                Manager.currentUser.getClient().getLastName());

        List<Transaction> transactionList = transactionService.findAll();
        ObservableList<Transaction> transactions = FXCollections.observableArrayList(transactionList);
        TableViewTransactions.setItems(transactions);
        setCellValueFactories();

        filterData();
    }

    void filterData() {
        List<Transaction> transactions = transactionService.findAll();
        itemsCount = transactions.size();

        String searchText = TextFieldSearch.getText();
        if (!searchText.isEmpty()) {
            transactions = transactions.stream().filter(transaction -> transaction.getTransactionType().toLowerCase().contains(searchText.toLowerCase())
            ).collect(Collectors.toList());
        }
        TableViewTransactions.getItems().clear();
        for (Transaction transaction : transactions) {
            TableViewTransactions.getItems().add(transaction);
        }
        int filteredItemsCount = transactions.size();
        LabelInfo.setText("Всего записей " + filteredItemsCount + " из " + itemsCount);
        TableViewTransactions.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

    }

    private void setCellValueFactories() {
        TableColumnAccountClient.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAccount().getClient().getFullName()));
        TableColumnAccount.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAccount().getAccountType()));
        TableColumnInstrument.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getInstrument().getInstrumentType().toString()));
        TableColumnTransactionType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTransactionType()));
        TableColumnAmount.setCellValueFactory(cellData -> new SimpleStringProperty((cellData.getValue().getAmount().toString())));
        TableColumnTransactionId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTransactionId().toString()));
        TableColumnDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTransactionDate().toString()));
    }
}