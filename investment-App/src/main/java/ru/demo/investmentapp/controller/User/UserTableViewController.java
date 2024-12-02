package ru.demo.investmentapp.controller.User;

import com.itextpdf.text.DocumentException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import ru.demo.investmentapp.model.User;
import ru.demo.investmentapp.service.UserService;
import ru.demo.investmentapp.util.Manager;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static ru.demo.investmentapp.util.Manager.*;

public class UserTableViewController implements Initializable {

    private int itemsCount;
    private UserService userService = new UserService();

    @FXML
    private Button BtnBack;

    @FXML
    private TableColumn<User, String> TableColumnUserName;

    @FXML
    private TableColumn<User, String> TableColumnRole;

    @FXML
    private TableColumn<User, String> TableColumnClient;

    @FXML
    private TableColumn<User, String> TableColumnPassword;

    @FXML
    private Label LabelInfo;

    @FXML
    private Label LabelUser;

    @FXML
    private TextField TextFieldSearch;

    @FXML
    private TableView<User> TableViewUsers;

    @FXML
    void TextFieldSearchAction(ActionEvent event) {
        filterData();
    }

    @FXML
    void MenuItemAddAction(ActionEvent event) {
        currentEditUser = null;
        ShowEditWindow("User/user-edit-view.fxml");
        filterData();
    }

    @FXML
    void MenuItemRoleAction(ActionEvent event) {
        Manager.LoadSecondStageScene("Role/role-table-view.fxml", "Роли");
    }

    @FXML
    void MenuItemAccountAction(ActionEvent event) {
        Manager.LoadSecondStageScene("Account/account-table-view.fxml", "Аккаунты");
    }

    @FXML
    void MenuItemClientAction(ActionEvent event) {
        Manager.LoadSecondStageScene("Client/client-table-view.fxml", "Клиенты");
    }

    @FXML
    void MenuItemBackAction(ActionEvent event) {
        Manager.LoadSecondStageScene("main-view.fxml", "Aurora Growth");
    }

    @FXML
    void MenuItemDeleteAction(ActionEvent event) {
        User user = TableViewUsers.getSelectionModel().getSelectedItem();

        Optional<ButtonType> result = ShowConfirmPopup();
        if (result.get() == ButtonType.OK) {
            userService.delete(user);
            filterData();
        }
    }


    @FXML
    void MenuItemUpdateAction(ActionEvent event) {
        User user = TableViewUsers.getSelectionModel().getSelectedItem();
        currentEditUser = user;
        ShowEditWindow("user/user-edit-view.fxml");
        filterData();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initController();
    }

    public void initController() {
        LabelUser.setText("Вы вошли как " + currentUser.getClient().getFirstName() + " " +
                Manager.currentUser.getClient().getLastName());

        List<User> userList = userService.findAll();
        ObservableList<User> users = FXCollections.observableArrayList(userList);
        TableViewUsers.setItems(users);
        setCellValueFactories();

        filterData();
    }

    void filterData() {
        List<User> users = userService.findAll();
        itemsCount = users.size();

        String searchText = TextFieldSearch.getText();
        if (!searchText.isEmpty()) {
            users = users.stream().filter(user -> user.getUserName().toLowerCase().contains(searchText.toLowerCase())
            ).collect(Collectors.toList());
        }
        TableViewUsers.getItems().clear();
        for (User user : users) {
            TableViewUsers.getItems().add(user);
        }
        int filteredItemsCount = users.size();
        LabelInfo.setText("Всего записей " + filteredItemsCount + " из " + itemsCount);
        TableViewUsers.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

    }

    private void setCellValueFactories() {
        TableColumnUserName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUserName()));
        TableColumnPassword.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPassword()));
        TableColumnRole.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRole().toString()));
        TableColumnClient.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getClient().getFullName().toString()));
    }
}
