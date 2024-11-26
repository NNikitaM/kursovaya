package ru.demo.investmentapp.controller.MarketData;

import com.itextpdf.text.DocumentException;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.InputMethodEvent;
import ru.demo.investmentapp.service.MarketDataService;
import ru.demo.investmentapp.util.Manager;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import ru.demo.investmentapp.model.MarketData;

import static ru.demo.investmentapp.util.Manager.*;

public class MarketDataTableViewController implements Initializable {
    private int itemsCount;
    private final MarketDataService marketDataService = new MarketDataService();

    @FXML
    private ComboBox<String> ComboBoxPrice;

    @FXML
    private Label LabelInfo;

    @FXML
    private Label LabelUser;

    @FXML
    private MenuItem MenuItemAdd;

    @FXML
    private MenuItem MenuItemBack;

    @FXML
    private MenuItem MenuItemDelete;

    @FXML
    private MenuItem MenuItemPrintToPDF;

    @FXML
    private MenuItem MenuItemUpdate;

    @FXML
    private TableColumn<MarketData, String> TableColumnDataDate;

    @FXML
    private TableColumn<MarketData, String> TableColumnInstrument;

    @FXML
    private TableColumn<MarketData, Double> TableColumnPrice;

    @FXML
    private TableColumn<MarketData, Integer> TableColumnVolume;

    @FXML
    private TableView<MarketData> TableViewMarketData;

    @FXML
    private TextField TextFieldSearch;

    @FXML
    void ComboBoxPriceAction(ActionEvent event) {
        filterData();
    }

    @FXML
    void MenuItemInstrumentAction(ActionEvent event) {
        Manager.LoadSecondStageScene("Instrument/instrument-table-view.fxml", "Инструменты");
    }

    @FXML
    void MenuItemBackAction(ActionEvent event) {
        Manager.LoadSecondStageScene("main-view.fxml", "ООО Инвестиции");
    }

    @FXML
    void MenuItemAddAction(ActionEvent event) {
        currentInstrument = null;
        ShowEditWindow("MarketData/marketData-edit-view.fxml");
        filterData();
    }

    @FXML
    void MenuItemDeleteAction(ActionEvent event) {
        MarketData marketData = TableViewMarketData.getSelectionModel().getSelectedItem();

        Optional<ButtonType> result = ShowConfirmPopup();
        if (result.get() == ButtonType.OK) {
            marketDataService.delete(marketData);
            filterData();
        }
    }

    @FXML
    void MenuItemPrintToPDFAction(ActionEvent event) throws DocumentException, FileNotFoundException {
        MarketData marketData = TableViewMarketData.getSelectionModel().getSelectedItem();

        if (marketData != null) {
            Manager.PrintMarketDataToPDF(marketData);
            MessageBox("Информация", "", "Данные сохранены успешно", Alert.AlertType.INFORMATION);
            return;
        }
    }

    @FXML
    void MenuItemUpdateAction(ActionEvent event) {
        MarketData marketData = TableViewMarketData.getSelectionModel().getSelectedItem();
        currentMarketData = marketData;
        ShowEditWindow("MarketData/marketData-edit-view.fxml");
        filterData();
    }

    @FXML
    void TextFieldSearchAction(ActionEvent event) {
        filterData();
    }

    @FXML
    void TextFieldTextChanged(InputMethodEvent event) {

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initController();
    }

    public void initController() {
        LabelUser.setText("Вы вошли как " + currentUser.getClient().getFirstName() + " " +
                Manager.currentUser.getClient().getLastName());

        ObservableList<String> marketData = FXCollections.observableArrayList("по возрастанию цены", "по убыванию цены");
        ComboBoxPrice.setItems(marketData);

        filterData();
        setCellValueFactories();
    }

    void filterData() {
        List<MarketData> marketDatas = marketDataService.findAll();
        itemsCount = marketDatas.size();

        // Сортировка по цене
        if (!ComboBoxPrice.getSelectionModel().isEmpty()) {
            String price = ComboBoxPrice.getValue();
            if (price.equals("по возрастанию цены")) {
                marketDatas = marketDatas.stream().sorted(Comparator.comparing(MarketData::getPrice)).collect(Collectors.toList());
            } else if (price.equals("по убыванию цены")) {
                marketDatas = marketDatas.stream().sorted(Comparator.comparing(MarketData::getPrice).reversed()).collect(Collectors.toList());
            }
        }

        // Поиск по имени инструмента
        String searchText = TextFieldSearch.getText();
        if (!searchText.isEmpty()) {
            marketDatas = marketDatas.stream()
                    .filter(marketData -> marketData.getInstrument().getInstrumentType().toLowerCase().contains(searchText.toLowerCase()))
                    .collect(Collectors.toList());
        }

        TableViewMarketData.getItems().clear();
        for (MarketData marketData : marketDatas) {
            TableViewMarketData.getItems().add(marketData);
        }

        int filteredItemsCount = marketDatas.size();
        LabelInfo.setText("Всего записей " + filteredItemsCount + " из " + itemsCount);
        TableViewMarketData.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }

    private void setCellValueFactories() {
        TableColumnInstrument.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getInstrument().getInstrumentType()));
        TableColumnDataDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDataDate().toString()));
        TableColumnPrice.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        TableColumnVolume.setCellValueFactory(cellData -> new SimpleIntegerProperty((int) cellData.getValue().getVolume()).asObject());
    }
}
