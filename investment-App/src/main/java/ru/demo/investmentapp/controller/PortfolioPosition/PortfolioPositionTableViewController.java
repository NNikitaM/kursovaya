package ru.demo.investmentapp.controller.PortfolioPosition;

import com.itextpdf.text.DocumentException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import ru.demo.investmentapp.model.PortfolioPosition;
import ru.demo.investmentapp.service.PortfolioPositionService;
import ru.demo.investmentapp.util.Manager;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static ru.demo.investmentapp.util.Manager.*;

public class PortfolioPositionTableViewController implements Initializable {

    private int itemsCount;
    private PortfolioPositionService portfolioPositionService = new PortfolioPositionService();

    @FXML
    private Button BtnBack;

    @FXML
    private TableColumn<PortfolioPosition, String> TableColumnPortfolio;

    @FXML
    private TableColumn<PortfolioPosition, String> TableColumnInstrument;

    @FXML
    private TableColumn<PortfolioPosition, String> TableColumnQuantity;

    @FXML
    private TableColumn<PortfolioPosition, String> TableColumnPurchasePrice;

    @FXML
    private TableColumn<PortfolioPosition, String> TableColumnPurchaseDate;

    @FXML
    private Label LabelInfo;
    @FXML
    private Label LabelUser;
    @FXML
    private TextField TextFieldSearch;

    @FXML
    private TableView<PortfolioPosition> TableViewPositions;

    @FXML
    void TextFieldSearchAction(ActionEvent event) {
        filterData();
    }

    @FXML
    void MenuItemAddAction(ActionEvent event) {
        currentPortfolioPosition = null;
        ShowEditWindow("PortfolioPosition/portfolioPosition-edit-view.fxml");
        filterData();
    }

    @FXML
    void MenuItemBackAction(ActionEvent event) {
        Manager.LoadSecondStageScene("main-view.fxml", "ООО Инвестиции");
    }

    @FXML
    void MenuItemDeleteAction(ActionEvent event) {
        PortfolioPosition portfolioPosition = TableViewPositions.getSelectionModel().getSelectedItem();

        Optional<ButtonType> result = ShowConfirmPopup();
        if (result.get() == ButtonType.OK) {
            portfolioPositionService.delete(portfolioPosition);
            filterData();
        }
    }

    @FXML
    void MenuItemUpdateAction(ActionEvent event) {
        PortfolioPosition portfolioPosition = TableViewPositions.getSelectionModel().getSelectedItem();
        currentPortfolioPosition = portfolioPosition;
        ShowEditWindow("PortfolioPosition/portfolioPosition-edit-view.fxml");
        filterData();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initController();
    }

    public void initController() {
        LabelUser.setText("Вы вошли как " + currentUser.getClient().getFirstName() + " " +
                Manager.currentUser.getClient().getLastName());

        List<PortfolioPosition> portfolioPositionList = portfolioPositionService.findAll();
        ObservableList<PortfolioPosition> portfolioPositions = FXCollections.observableArrayList(portfolioPositionList);
        TableViewPositions.setItems(portfolioPositions);
        setCellValueFactories();

        filterData();
    }

    void filterData() {
        List<PortfolioPosition> portfolioPositions = portfolioPositionService.findAll();
        itemsCount = portfolioPositions.size();

        String searchText = TextFieldSearch.getText();
        if (!searchText.isEmpty()) {
            portfolioPositions = portfolioPositions.stream().filter(portfolioPosition ->
                    portfolioPosition.getPortfolio().getPortfolioName().toLowerCase().contains(searchText.toLowerCase())
            ).collect(Collectors.toList());
        }
        TableViewPositions.getItems().clear();
        for (PortfolioPosition portfolioPosition : portfolioPositions) {
            TableViewPositions.getItems().add(portfolioPosition);
        }
        int filteredItemsCount = portfolioPositions.size();
        LabelInfo.setText("Всего записей " + filteredItemsCount + " из " + itemsCount);
        TableViewPositions.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

    }

    private void setCellValueFactories() {
        TableColumnQuantity.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getQuantity().toString()));
        TableColumnPurchaseDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPurchaseDate().toString()));
        TableColumnPurchasePrice.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPurchasePrice().toString()));
        TableColumnPortfolio.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPortfolio().getPortfolioName()));
        TableColumnInstrument.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getInstrument().getInstrumentType().toString()));
    }
}
