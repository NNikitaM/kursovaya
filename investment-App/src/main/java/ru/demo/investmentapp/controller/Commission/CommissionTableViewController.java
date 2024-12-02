package ru.demo.investmentapp.controller.Commission;

import com.itextpdf.text.DocumentException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import ru.demo.investmentapp.model.Commission;
import ru.demo.investmentapp.service.CommissionService;
import ru.demo.investmentapp.util.Manager;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static ru.demo.investmentapp.util.Manager.*;

public class CommissionTableViewController implements Initializable {

    private int itemsCount;
    private CommissionService commissionService = new CommissionService();

    @FXML
    private Button BtnBack;

    @FXML
    private TableColumn<Commission, String> TableColumnCommissionId;

    @FXML
    private TableColumn<Commission, String> TableColumnTransaction;

    @FXML
    private TableColumn<Commission, String> TableColumnType;

    @FXML
    private TableColumn<Commission, String> TableColumnAmount;

    @FXML
    private Label LabelInfo;
    @FXML
    private Label LabelUser;
    @FXML
    private TextField TextFieldSearch;


    @FXML
    private TableView<Commission> TableViewCommissions;

    @FXML
    void TextFieldSearchAction(ActionEvent event) {
        filterData();
    }

    @FXML
    void MenuItemAddAction(ActionEvent event) {
        currentCommission = null;
        ShowEditWindow("Commission/commission-edit-view.fxml");
        filterData();
    }

    @FXML
    void MenuItemBackAction(ActionEvent event) {
        Manager.LoadSecondStageScene("main-view.fxml", "Aurora Growth");
    }

    @FXML
    void MenuItemDeleteAction(ActionEvent event) {
        Commission commission = TableViewCommissions.getSelectionModel().getSelectedItem();

        Optional<ButtonType> result = ShowConfirmPopup();
        if (result.get() == ButtonType.OK) {
            commissionService.delete(commission);
            filterData();
        }
    }

    @FXML
    void MenuItemUpdateAction(ActionEvent event) {
        Commission commission = TableViewCommissions.getSelectionModel().getSelectedItem();
        currentCommission = commission;
        ShowEditWindow("Commission/commission-edit-view.fxml");
        filterData();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initController();
    }

    public void initController() {
        LabelUser.setText("Вы вошли как " + currentUser.getClient().getFirstName() + " " +
                Manager.currentUser.getClient().getLastName());

        List<Commission> commissionList = commissionService.findAll();
        ObservableList<Commission> commissions = FXCollections.observableArrayList(commissionList);
        TableViewCommissions.setItems(commissions);
        setCellValueFactories();

        filterData();

    }

    void filterData() {
        List<Commission> commissions = commissionService.findAll();
        itemsCount = commissions.size();

        String searchText = TextFieldSearch.getText();
        if (!searchText.isEmpty()) {
            commissions = commissions.stream().filter(commission -> commission.getCommissionType().toLowerCase().contains(searchText.toLowerCase())
            ).collect(Collectors.toList());
        }
        TableViewCommissions.getItems().clear();
        for (Commission commission : commissions) {
            TableViewCommissions.getItems().add(commission);
        }
        int filteredItemsCount = commissions.size();
        LabelInfo.setText("Всего записей " + filteredItemsCount + " из " + itemsCount);
        TableViewCommissions.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    private void setCellValueFactories() {
        TableColumnType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCommissionType()));
        TableColumnAmount.setCellValueFactory(cellData -> new SimpleStringProperty((cellData.getValue().getAmount()).toString()));
        TableColumnCommissionId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCommissionId().toString()));
        TableColumnTransaction.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTransaction().getTransactionType()));
    }
}
