package ru.demo.investmentapp.controller.Account;

import com.itextpdf.text.DocumentException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import ru.demo.investmentapp.model.Account;
import ru.demo.investmentapp.service.AccountService;
import ru.demo.investmentapp.util.Manager;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static ru.demo.investmentapp.util.Manager.*;

public class AccountTableViewController implements Initializable {

    private int itemsCount;
    private AccountService accountService = new AccountService();

    @FXML
    private Button BtnBack;

    @FXML
    private TableColumn<Account, String> TableColumnAccountId;

    @FXML
    private TableColumn<Account, String> TableColumnClient;

    @FXML
    private TableColumn<Account, String> TableColumnType;

    @FXML
    private TableColumn<Account, String> TableColumnBalance;

    @FXML
    private Label LabelInfo;
    @FXML
    private Label LabelUser;
    @FXML
    private TextField TextFieldSearch;


    @FXML
    private TableView<Account> TableViewAccounts;

    @FXML
    void TextFieldSearchAction(ActionEvent event) {
        filterData();
    }

    @FXML
    void MenuItemAddAction(ActionEvent event) {
        currentAccount = null;
        ShowEditWindow("Account/account-edit-view.fxml");
        filterData();
    }

    @FXML
    void MenuItemBackAction(ActionEvent event) {
        Manager.LoadSecondStageScene("main-view.fxml", "Aurora Growth");
    }

    @FXML
    void MenuItemDeleteAction(ActionEvent event) {
        Account account = TableViewAccounts.getSelectionModel().getSelectedItem();

        Optional<ButtonType> result = ShowConfirmPopup();
        if (result.get() == ButtonType.OK) {
            accountService.delete(account);
            filterData();
        }
    }

    @FXML
    void MenuItemUpdateAction(ActionEvent event) {
        Account account = TableViewAccounts.getSelectionModel().getSelectedItem();
        currentAccount = account;
        ShowEditWindow("Account/account-edit-view.fxml");
        filterData();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initController();
    }

    public void initController() {
        LabelUser.setText("Вы вошли как " + currentUser.getClient().getFirstName() + " " +
                Manager.currentUser.getClient().getLastName());

        List<Account> accountList = accountService.findAll();
        ObservableList<Account> accounts = FXCollections.observableArrayList(accountList);
        TableViewAccounts.setItems(accounts);
        setCellValueFactories();

        filterData();

    }

    void filterData() {
        List<Account> accounts = accountService.findAll();
        itemsCount = accounts.size();

        String searchText = TextFieldSearch.getText();
        if (!searchText.isEmpty()) {
            accounts = accounts.stream().filter(account -> account.getAccountType().toLowerCase().contains(searchText.toLowerCase())
            ).collect(Collectors.toList());
        }
        TableViewAccounts.getItems().clear();
        for (Account account : accounts) {
            TableViewAccounts.getItems().add(account);
        }
        int filteredItemsCount = accounts.size();
        LabelInfo.setText("Всего записей " + filteredItemsCount + " из " + itemsCount);
        TableViewAccounts.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

    }

    private void setCellValueFactories() {
        TableColumnType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAccountType()));
        TableColumnBalance.setCellValueFactory(cellData -> new SimpleStringProperty((cellData.getValue().getBalance()).toString()));
        TableColumnAccountId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAccountId().toString()));
        TableColumnClient.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getClient().getFullName().toString()));
    }
}
