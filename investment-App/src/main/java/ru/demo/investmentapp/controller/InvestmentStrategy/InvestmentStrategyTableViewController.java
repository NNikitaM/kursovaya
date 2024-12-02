package ru.demo.investmentapp.controller.InvestmentStrategy;

import com.itextpdf.text.DocumentException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import ru.demo.investmentapp.model.InvestmentStrategy;
import ru.demo.investmentapp.service.InvestmentStrategyService;
import ru.demo.investmentapp.util.Manager;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static ru.demo.investmentapp.util.Manager.*;

public class InvestmentStrategyTableViewController implements Initializable {

    private int itemsCount;
    private InvestmentStrategyService investmentStrategyService = new InvestmentStrategyService();

    @FXML
    private Button BtnBack;

    @FXML
    private TableColumn<InvestmentStrategy, String> TableColumnStrategyId;

    @FXML
    private TableColumn<InvestmentStrategy, String> TableColumnStrategyName;

    @FXML
    private TableColumn<InvestmentStrategy, String> TableColumnRiskLevel;

    @FXML
    private TableColumn<InvestmentStrategy, String> TableColumnAssetAllocation;

    @FXML
    private TableColumn<InvestmentStrategy, String> TableColumnDescription;

    @FXML
    private TableColumn<InvestmentStrategy, String> TableColumnReturnTarget;

    @FXML
    private Label LabelInfo;

    @FXML
    private Label LabelUser;

    @FXML
    private TextField TextFieldSearch;

    @FXML
    private TableView<InvestmentStrategy> TableViewStrategies;

    @FXML
    void TextFieldSearchAction(ActionEvent event) {
        filterData();
    }

    @FXML
    void MenuItemAddAction(ActionEvent event) {
        currentInvestmentStrategy = null;
        ShowEditWindow("InvestmentStrategy/investmentStrategy-edit-view.fxml");
        filterData();
    }

    @FXML
    void MenuItemBackAction(ActionEvent event) {
        Manager.LoadSecondStageScene("main-view.fxml", "Aurora Growth");
    }

    @FXML
    void MenuItemDeleteAction(ActionEvent event) {
        InvestmentStrategy investmentStrategy = TableViewStrategies.getSelectionModel().getSelectedItem();

        Optional<ButtonType> result = ShowConfirmPopup();
        if (result.get() == ButtonType.OK) {
            investmentStrategyService.delete(investmentStrategy);
            filterData();
        }
    }

    @FXML
    void MenuItemUpdateAction(ActionEvent event) {
        InvestmentStrategy investmentStrategy = TableViewStrategies.getSelectionModel().getSelectedItem();
        currentInvestmentStrategy = investmentStrategy;
        ShowEditWindow("InvestmentStrategy/investmentStrategy-edit-view.fxml");
        filterData();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initController();
    }

    public void initController() {
        LabelUser.setText("Вы вошли как " + currentUser.getClient().getFirstName() + " " +
                Manager.currentUser.getClient().getLastName());

        List<InvestmentStrategy> investmentStrategyList = investmentStrategyService.findAll();
        ObservableList<InvestmentStrategy> investmentStrategies = FXCollections.observableArrayList(investmentStrategyList);
        TableViewStrategies.setItems(investmentStrategies);
        setCellValueFactories();

        filterData();

    }

    void filterData() {
        List<InvestmentStrategy> investmentStrategies = investmentStrategyService.findAll();
        itemsCount = investmentStrategies.size();

        String searchText = TextFieldSearch.getText();
        if (!searchText.isEmpty()) {
            investmentStrategies = investmentStrategies.stream().filter(investmentStrategy -> investmentStrategy.getStrategyName().toLowerCase().contains(searchText.toLowerCase())
            ).collect(Collectors.toList());
        }
        TableViewStrategies.getItems().clear();
        for (InvestmentStrategy investmentStrategy : investmentStrategies) {
            TableViewStrategies.getItems().add(investmentStrategy);
        }
        int filteredItemsCount = investmentStrategies.size();
        LabelInfo.setText("Всего записей " + filteredItemsCount + " из " + itemsCount);
        TableViewStrategies.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

    }

    private void setCellValueFactories() {
        TableColumnStrategyName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStrategyName()));
        TableColumnDescription.setCellValueFactory(cellData -> new SimpleStringProperty((cellData.getValue().getDescription())));
        TableColumnStrategyId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStrategyId().toString()));
        TableColumnReturnTarget.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getReturnTarget().toString()));
        TableColumnRiskLevel.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRiskLevel()));
        TableColumnAssetAllocation.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAssetAllocation()));
    }
}
