package ru.demo.investmentapp.controller.PortfolioStrategy;

import com.itextpdf.text.DocumentException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import ru.demo.investmentapp.model.PortfolioStrategy;
import ru.demo.investmentapp.service.PortfolioStrategyService;
import ru.demo.investmentapp.util.Manager;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static ru.demo.investmentapp.util.Manager.*;

public class PortfolioStrategyTableViewController implements Initializable {

    private int itemsCount;
    private PortfolioStrategyService portfolioStrategyService = new PortfolioStrategyService();

    @FXML
    private Button BtnBack;

    @FXML
    private TableColumn<PortfolioStrategy, String> TableColumnPortfolio;

    @FXML
    private TableColumn<PortfolioStrategy, String> TableColumnStrategy;

    @FXML
    private Label LabelInfo;
    @FXML
    private Label LabelUser;
    @FXML
    private TextField TextFieldSearch;

    @FXML
    private TableView<PortfolioStrategy> TableViewPortfolioStrategy;

    @FXML
    void TextFieldSearchAction(ActionEvent event) {
        filterData();
    }

    @FXML
    void MenuItemAddAction(ActionEvent event) {
        currentPortfolioStrategy = null;
        ShowEditWindow("PortfolioStrategy/portfolioStrategy-edit-view.fxml");
        filterData();
    }

    @FXML
    void MenuItemBackAction(ActionEvent event) {
        Manager.LoadSecondStageScene("main-view.fxml", "Aurora Growth");
    }

    @FXML
    void MenuItemDeleteAction(ActionEvent event) {
        PortfolioStrategy portfolioStrategy = TableViewPortfolioStrategy.getSelectionModel().getSelectedItem();

        Optional<ButtonType> result = ShowConfirmPopup();
        if (result.get() == ButtonType.OK) {
            portfolioStrategyService.delete(portfolioStrategy);
            filterData();
        }
    }

    @FXML
    void MenuItemUpdateAction(ActionEvent event) {
        PortfolioStrategy portfolioStrategy = TableViewPortfolioStrategy.getSelectionModel().getSelectedItem();
        currentPortfolioStrategy = portfolioStrategy;
        ShowEditWindow("PortfolioStrategy/portfolioStrategy-edit-view.fxml");
        filterData();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initController();
    }

    public void initController() {
        LabelUser.setText("Вы вошли как " + currentUser.getClient().getFirstName() + " " +
                Manager.currentUser.getClient().getLastName());

        List<PortfolioStrategy> portfolioStrategyList = portfolioStrategyService.findAll();
        ObservableList<PortfolioStrategy> portfolioStrategies = FXCollections.observableArrayList(portfolioStrategyList);
        TableViewPortfolioStrategy.setItems(portfolioStrategies);
        setCellValueFactories();

        filterData();
    }

    void filterData() {
        List<PortfolioStrategy> portfolioStrategies = portfolioStrategyService.findAll();
        itemsCount = portfolioStrategies.size();

        String searchText = TextFieldSearch.getText();
        if (!searchText.isEmpty()) {
            portfolioStrategies = portfolioStrategies.stream().filter(portfolioStrategy ->
                    portfolioStrategy.getPortfolio().getPortfolioName().toLowerCase().contains(searchText.toLowerCase())
            ).collect(Collectors.toList());
        }
        TableViewPortfolioStrategy.getItems().clear();
        for (PortfolioStrategy portfolioStrategy : portfolioStrategies) {
            TableViewPortfolioStrategy.getItems().add(portfolioStrategy);
        }
        int filteredItemsCount = portfolioStrategies.size();
        LabelInfo.setText("Всего записей " + filteredItemsCount + " из " + itemsCount);
        TableViewPortfolioStrategy.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

    }

    private void setCellValueFactories() {
        TableColumnPortfolio.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPortfolio().getPortfolioName()));
        TableColumnStrategy.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStrategy().getStrategyName()));
    }
}
