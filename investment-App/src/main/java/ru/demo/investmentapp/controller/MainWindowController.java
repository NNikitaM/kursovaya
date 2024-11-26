package ru.demo.investmentapp.controller;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import jakarta.persistence.criteria.CriteriaBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import ru.demo.investmentapp.controller.Instrument.InstrumentCell;
import ru.demo.investmentapp.model.Instrument;
import ru.demo.investmentapp.model.MarketData;
import ru.demo.investmentapp.service.InstrumentService;
import ru.demo.investmentapp.service.MarketDataService;
import ru.demo.investmentapp.util.Manager;

import static ru.demo.investmentapp.util.Manager.*;

public class MainWindowController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private BorderPane BorderPaneMainFrame;

    @FXML
    private ComboBox<String> ComboBoxInstrument;

    @FXML
    private ComboBox<String> ComboBoxSort;

    @FXML
    private Label LabelInfo;

    @FXML
    private Label LabelUser;

    @FXML
    private ListView<Instrument> ListViewInstrument;

    @FXML
    private MenuBar MenuBarFile;

    @FXML
    private MenuItem MenuItemMarketData;

    @FXML
    private TextField TextFieldSearch;

    private int itemsCount;
    private MarketDataService marketDataService = new MarketDataService();
    private InstrumentService instrumentService = new InstrumentService();


    @FXML
    void ComboBoxInstrumentAction(ActionEvent event) {
        filterData();
    }

    @FXML
    void ComboBoxSortAction(ActionEvent event) {
        filterData();
    }

    @FXML
    void MenuItemMarketDataAction(ActionEvent event) {
        Manager.LoadSecondStageScene("MarketData/marketData-table-view.fxml", "Рыночные данные");
    }

    @FXML
    void MenuItemStrategyAction(ActionEvent event) {
        Manager.LoadSecondStageScene("InvestmentStrategy/investmentStrategy-table-view.fxml", "Инвестиционные стратегии");
    }

    @FXML
    void MenuItemCompanyInfoAction(ActionEvent event) {
        Manager.LoadSecondStageScene("CompanyInfo/companyInfo-table-view.fxml", "Информация о компаниях");
    }

    @FXML
    void MenuItemPortfolioAction(ActionEvent event) {
        Manager.LoadSecondStageScene("Portfolio/portfolio-table-view.fxml", "Портфолио");
    }

    @FXML
    void MenuItemReportAction(ActionEvent event) {
        Manager.LoadSecondStageScene("Report/report-table-view.fxml", "Отчеты");
    }

    @FXML
    void MenuItemUserAction(ActionEvent event) {
        Manager.LoadSecondStageScene("User/user-table-view.fxml", "Учетные данные");
    }

    @FXML
    void MenuItemTransactionAction(ActionEvent event) {
        Manager.LoadSecondStageScene("Transaction/transaction-table-view.fxml", "Операции");
    }

    @FXML
    void TextFieldSearchAction(ActionEvent event) {
        filterData();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainWindowController = this;
        LabelUser.setText("Вы вошли как " + currentUser.getClient().getFirstName() + " " +
                Manager.currentUser.getClient().getLastName());

        List<Instrument> instrumentList = instrumentService.findAll();
        instrumentList.add(0, new Instrument(0L, "Все", "", ""));

        Set<String> instrumentTypes = instrumentList.stream()
                .map(Instrument::getInstrumentType)
                .collect(Collectors.toSet());

        ObservableList<String> instruments = FXCollections.observableArrayList(instrumentTypes);

        ComboBoxInstrument.setItems(instruments);

        ObservableList<String> instrument = FXCollections.observableArrayList("по возрастанию цены", "по убыванию цены");
        ComboBoxSort.setItems(instrument);

        loadRole();
        filterData();
    }

    void filterData() {
        List<MarketData> marketDatas = marketDataService.findAll();
        itemsCount = marketDatas.size();

        // Фильтр по типу
        if (!ComboBoxInstrument.getSelectionModel().isEmpty()) {
            String instrumentType = ComboBoxInstrument.getValue();

            // Проверка на "Все"
            if (!instrumentType.equals("Все")) {
                marketDatas = marketDatas.stream()
                        .filter(marketData -> marketData.getInstrument().getInstrumentType().equals(instrumentType))
                        .collect(Collectors.toList());
            }
        }

        // Сортировка по цене
        if (!ComboBoxSort.getSelectionModel().isEmpty()) {
            String price = ComboBoxSort.getValue();
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
                    .filter(marketData -> marketData.getInstrument().getName().toLowerCase().contains(searchText.toLowerCase()))
                    .collect(Collectors.toList());
        }


        // Обновление ListViewInstrument
        ListViewInstrument.getItems().clear();
        ListViewInstrument.getItems().addAll(marketDatas.stream()
                .map(MarketData::getInstrument)
                .distinct().collect(Collectors.toList()));
        ListViewInstrument.setCellFactory(lv -> new InstrumentCell());


        // Обновление LabelInfo
        int filteredItemsCount = marketDatas.size();
        LabelInfo.setText("Всего записей " + filteredItemsCount + " из " + itemsCount);
    }


    public void loadRole() {
        MenuBarFile.setVisible(false);
        // вход как админ
        if (currentUser.getRole().getRoleId() == 1) {
            MenuBarFile.setVisible(true);
        }
        // вход как пользователь
        if (currentUser.getRole().getRoleId() == 2) {
            MenuBarFile.setVisible(true);
            MenuItemMarketData.setVisible(false);
        }
    }
}