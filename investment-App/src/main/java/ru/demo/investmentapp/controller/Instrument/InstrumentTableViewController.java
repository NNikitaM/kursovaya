package ru.demo.investmentapp.controller.Instrument;

import com.itextpdf.text.DocumentException;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import ru.demo.investmentapp.model.Instrument;
import ru.demo.investmentapp.service.InstrumentService;
import ru.demo.investmentapp.util.Manager;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static ru.demo.investmentapp.util.Manager.*;

public class InstrumentTableViewController implements Initializable {

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
    private TableColumn<Instrument, String> TableColumnId;

    @FXML
    private TableColumn<Instrument, String> TableColumnTickerSymbol;

    @FXML
    private TableColumn<Instrument, String> TableColumnName;

    @FXML
    private TableColumn<Instrument, String> TableColumnType;

    @FXML
    private TableView<Instrument> TableViewInstruments;

    @FXML
    private TextField TextFieldSearch;
    private int itemsCount;
    private InstrumentService instrumentService = new InstrumentService();

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
        List<Instrument> items = instrumentService.findAll();
        itemsCount = items.size();

        String searchText = TextFieldSearch.getText();
        if (!searchText.isEmpty()) {
            items = items.stream().filter(item -> item.getInstrumentType().toLowerCase().contains(searchText.toLowerCase())).collect(Collectors.toList());
        }
        TableViewInstruments.getItems().clear();
        for (Instrument instrument : items) {
            TableViewInstruments.getItems().add(instrument);
        }

        int filteredItemsCount = items.size();
        LabelInfo.setText("Всего записей " + filteredItemsCount + " из " + itemsCount);
        TableViewInstruments.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
    }

    private void setCellValueFactories() {

        TableColumnId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getInstrumentId().toString()));
        TableColumnName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        TableColumnTickerSymbol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTickerSymbol()));
        TableColumnType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getInstrumentType()));
    }

    @FXML
    void MenuItemAddAction(ActionEvent event) {
        currentInstrument = null;
        ShowEditWindow("Instrument/instrument-edit-view.fxml");
        filterData();
    }

    @FXML
    void MenuItemBackAction(ActionEvent event) {
        Manager.LoadSecondStageScene("main-view.fxml", "Aurora Growth");
    }

    @FXML
    void MenuItemDeleteAction(ActionEvent event) {
        Instrument instrument = TableViewInstruments.getSelectionModel().getSelectedItem();

        Optional<ButtonType> result = ShowConfirmPopup();
        if (result.get() == ButtonType.OK) {
            instrumentService.delete(instrument);
            filterData();
        }
    }

    @FXML
    void MenuItemUpdateAction(ActionEvent event) {
        Instrument instrument = TableViewInstruments.getSelectionModel().getSelectedItem();
        currentInstrument = instrument;
        ShowEditWindow("Instrument/instrument-edit-view.fxml");
        filterData();
    }
}
