package ru.demo.investmentapp.controller.Role;


import com.itextpdf.text.DocumentException;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import ru.demo.investmentapp.model.Role;
import ru.demo.investmentapp.service.RoleService;
import ru.demo.investmentapp.util.Manager;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static ru.demo.investmentapp.util.Manager.*;

public class RoleTableViewController implements Initializable {

    @FXML
    private MenuItem MenuItemAdd;

    @FXML
    private MenuItem MenuItemBack;

    @FXML
    private MenuItem MenuItemDelete;


    @FXML
    private MenuItem MenuItemUpdate;

    @FXML
    private Label LabelInfo;

    @FXML
    private Label LabelUser;

    @FXML
    private TableColumn<Role, String> TableColumnId;

    @FXML
    private TableColumn<Role, String> TableColumnTitle;

    @FXML
    private TableView<Role> TableViewRoles;

    @FXML
    private TextField TextFieldSearch;
    private int itemsCount;
    private RoleService roleService = new RoleService();


    @FXML
    void TextFieldSearchAction(ActionEvent event) {
        filterData();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initController();
    }

    public void initController() {
        LabelUser.setText("Вы вошли как " + currentUser.getClient().getFirstName() + " " +
                Manager.currentUser.getClient().getLastName());

        setCellValueFactories();

        filterData();
    }

    void filterData() {
        List<Role> items = roleService.findAll();
        itemsCount = items.size();

        String searchText = TextFieldSearch.getText();
        if (!searchText.isEmpty()) {
            items = items.stream().filter(item -> item.getTitle().toLowerCase().contains(searchText.toLowerCase())).collect(Collectors.toList());
        }
        TableViewRoles.getItems().clear();
        for (Role role : items) {
            TableViewRoles.getItems().add(role);
        }

        int filteredItemsCount = items.size();
        LabelInfo.setText("Всего записей " + filteredItemsCount + " из " + itemsCount);
        TableViewRoles.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    private void setCellValueFactories() {

        TableColumnId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRoleId().toString()));
        TableColumnTitle.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
    }

    @FXML
    void MenuItemAddAction(ActionEvent event) {
        currentRole = null;
        ShowEditWindow("Role/role-edit-view.fxml");
        filterData();
    }

    @FXML
    void MenuItemBackAction(ActionEvent event) {
        Manager.LoadSecondStageScene("main-view.fxml", "ООО Инвестиции");
    }

    @FXML
    void MenuItemDeleteAction(ActionEvent event) {
        Role role = TableViewRoles.getSelectionModel().getSelectedItem();
        if (!role.getUsers().isEmpty()) {
            ShowErrorMessageBox("Ошибка целостности данных, у данного роли есть зависимые пользователи");
            return;
        }

        Optional<ButtonType> result = ShowConfirmPopup();
        if (result.get() == ButtonType.OK) {
            roleService.delete(role);
            filterData();
        }
    }

    @FXML
    void MenuItemUpdateAction(ActionEvent event) {
        Role role = TableViewRoles.getSelectionModel().getSelectedItem();
        currentRole = role;
        ShowEditWindow("Role/role-edit-view.fxml");
        filterData();
    }
}
