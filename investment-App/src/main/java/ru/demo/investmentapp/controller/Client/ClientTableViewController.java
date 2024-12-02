package ru.demo.investmentapp.controller.Client;


import com.itextpdf.text.DocumentException;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import ru.demo.investmentapp.model.Client;
import ru.demo.investmentapp.service.ClientService;
import ru.demo.investmentapp.util.Manager;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static ru.demo.investmentapp.util.Manager.*;

public class ClientTableViewController implements Initializable {

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
    private TableColumn<Client, String> TableColumnId;

    @FXML
    private TableColumn<Client, String> TableColumnFirstName;

    @FXML
    private TableColumn<Client, String> TableColumnLastName;

    @FXML
    private TableColumn<Client, String> TableColumnMiddleName;

    @FXML
    private TableColumn<Client, String> TableColumnAddress;

    @FXML
    private TableColumn<Client, String> TableColumnEmail;

    @FXML
    private TableColumn<Client, String> TableColumnPhone;

    @FXML
    private TableColumn<Client, String> TableColumnClientType;

    @FXML
    private TableColumn<Client, String> TableColumnBirthday;

    @FXML
    private TableColumn<Client, String> TableColumnPassportNumber;

    @FXML
    private TableView<Client> TableViewClients;

    @FXML
    private TextField TextFieldSearch;
    private int itemsCount;
    private ClientService clientService = new ClientService();


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
        List<Client> items = clientService.findAll();
        itemsCount = items.size();

        String searchText = TextFieldSearch.getText();
        if (!searchText.isEmpty()) {
            items = items.stream().filter(item -> item.getFullName().toLowerCase().contains(searchText.toLowerCase())).collect(Collectors.toList());
        }
        TableViewClients.getItems().clear();
        for (Client client : items) {
            TableViewClients.getItems().add(client);
        }

        int filteredItemsCount = items.size();
        LabelInfo.setText("Всего записей " + filteredItemsCount + " из " + itemsCount);
        TableViewClients.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    private void setCellValueFactories() {

        TableColumnId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getClientId().toString()));
        TableColumnFirstName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        TableColumnLastName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        TableColumnMiddleName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMiddleName()));
        TableColumnAddress.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));
        TableColumnEmail.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        TableColumnPhone.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhone()));
        TableColumnClientType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getClientType()));
        TableColumnBirthday.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateOfBirth().toString()));
        TableColumnPassportNumber.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPassportNumber()));
    }

    @FXML
    void MenuItemAddAction(ActionEvent event) {
        currentClient = null;
        ShowEditWindow("Client/client-edit-view.fxml");
        filterData();
    }

    @FXML
    void MenuItemBackAction(ActionEvent event) {
        Manager.LoadSecondStageScene("main-view.fxml", "Aurora Growth");
    }

    @FXML
    void MenuItemPrintToPDFAction(ActionEvent event) throws DocumentException, FileNotFoundException {
        Client client = TableViewClients.getSelectionModel().getSelectedItem();

        if (client != null) {
            Manager.PrintClientToPDF(client);
            MessageBox("Информация", "", "Данные сохранены успешно", Alert.AlertType.INFORMATION);return;
        }
    }

    @FXML
    void MenuItemDeleteAction(ActionEvent event) {
        Client client = TableViewClients.getSelectionModel().getSelectedItem();

        Optional<ButtonType> result = ShowConfirmPopup();
        if (result.get() == ButtonType.OK) {
            clientService.delete(client);
            filterData();
        }
    }

    @FXML
    void MenuItemUpdateAction(ActionEvent event) {
        Client client = TableViewClients.getSelectionModel().getSelectedItem();
        currentClient = client;
        ShowEditWindow("Client/client-edit-view.fxml");
        filterData();
    }

}
