package ru.demo.investmentapp.controller.Portfolio;

import com.itextpdf.text.DocumentException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import ru.demo.investmentapp.model.Portfolio;
import ru.demo.investmentapp.service.PortfolioService;
import ru.demo.investmentapp.util.Manager;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static ru.demo.investmentapp.util.Manager.*;

public class PortfolioTableViewController implements Initializable {

    private int itemsCount;
    private PortfolioService portfolioService = new PortfolioService();

    @FXML
    private Button BtnBack;

    @FXML
    private TableColumn<Portfolio, String> TableColumnId;

    @FXML
    private TableColumn<Portfolio, String> TableColumnPortfolioName;

    @FXML
    private TableColumn<Portfolio, String> TableColumnPortfolioType;

    @FXML
    private TableColumn<Portfolio, String> TableColumnUserName;

    @FXML
    private Label LabelInfo;

    @FXML
    private Label LabelUser;

    @FXML
    private TextField TextFieldSearch;

    @FXML
    private TableView<Portfolio> TableViewPortfolios;

    @FXML
    void TextFieldSearchAction(ActionEvent event) {
        filterData();
    }

    @FXML
    void MenuItemAddAction(ActionEvent event) {
        currentPortfolio = null;
        ShowEditWindow("Portfolio/portfolio-edit-view.fxml");
        filterData();
    }

    @FXML
    void MenuItemBackAction(ActionEvent event) {
        Manager.LoadSecondStageScene("main-view.fxml", "Aurora Growth");
    }

    @FXML
    void MenuItemPrintToPDFAction(ActionEvent event) throws DocumentException, FileNotFoundException {
        Portfolio portfolio = TableViewPortfolios.getSelectionModel().getSelectedItem();

        if (portfolio != null) {
            Manager.PrintPortfolioToPDF(portfolio);
            MessageBox("Информация", "", "Данные сохранены успешно", Alert.AlertType.INFORMATION);return;
        }
    }

    @FXML
    void MenuItemPortfolioStrategyAction(ActionEvent event) {
        Manager.LoadSecondStageScene("PortfolioStrategy/portfolioStrategy-table-view.fxml", "Портфолио стратегий");
    }

    @FXML
    void MenuItemPositionAction(ActionEvent event) {
        Manager.LoadSecondStageScene("PortfolioPosition/portfolioPosition-table-view.fxml", "Позиции портфолио");
    }

    @FXML
    void MenuItemDeleteAction(ActionEvent event) {
        Portfolio portfolio = TableViewPortfolios.getSelectionModel().getSelectedItem();

        Optional<ButtonType> result = ShowConfirmPopup();
        if (result.get() == ButtonType.OK) {
            portfolioService.delete(portfolio);
            filterData();
        }
    }

    @FXML
    void MenuItemUpdateAction(ActionEvent event) {
        Portfolio portfolio = TableViewPortfolios.getSelectionModel().getSelectedItem();
        currentPortfolio = portfolio;
        ShowEditWindow("Portfolio/portfolio-edit-view.fxml");
        filterData();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initController();
    }

    public void initController() {
        LabelUser.setText("Вы вошли как " + currentUser.getClient().getFirstName() + " " +
                Manager.currentUser.getClient().getLastName());

        List<Portfolio> portfolioList = portfolioService.findAll();
        ObservableList<Portfolio> portfolios = FXCollections.observableArrayList(portfolioList);
        TableViewPortfolios.setItems(portfolios);
        setCellValueFactories();

        filterData();
    }

    void filterData() {
        List<Portfolio> portfolios = portfolioService.findAll();
        itemsCount = portfolios.size();

        String searchText = TextFieldSearch.getText();
        if (!searchText.isEmpty()) {
            portfolios = portfolios.stream().filter(portfolio -> portfolio.getPortfolioName().toLowerCase().contains(searchText.toLowerCase())
            ).collect(Collectors.toList());
        }
        TableViewPortfolios.getItems().clear();
        for (Portfolio portfolio: portfolios) {
            TableViewPortfolios.getItems().add(portfolio);
        }
        int filteredItemsCount = portfolios.size();
        LabelInfo.setText("Всего записей " + filteredItemsCount + " из " + itemsCount);
        TableViewPortfolios.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

    }

    private void setCellValueFactories() {
        TableColumnPortfolioName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPortfolioName()));
        TableColumnPortfolioType.setCellValueFactory(cellData -> new SimpleStringProperty((cellData.getValue().getPortfolioType())));
        TableColumnId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPortfolioId().toString()));
        TableColumnUserName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUser().getUserName()));
    }
}
