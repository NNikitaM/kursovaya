package ru.demo.investmentapp.controller.CompanyInfo;

import com.itextpdf.text.DocumentException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import ru.demo.investmentapp.model.CompanyInfo;
import ru.demo.investmentapp.service.CompanyInfoService;
import ru.demo.investmentapp.util.Manager;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static ru.demo.investmentapp.util.Manager.*;

public class CompanyInfoTableViewController implements Initializable {

    private int itemsCount;
    private CompanyInfoService companyInfoService = new CompanyInfoService();

    @FXML
    private Button BtnBack;

    @FXML
    private TableColumn<CompanyInfo, String> TableColumnCompanyInfoId;

    @FXML
    private TableColumn<CompanyInfo, String> TableColumnInstrument;

    @FXML
    private TableColumn<CompanyInfo, String> TableColumnMarketCap;

    @FXML
    private TableColumn<CompanyInfo, String> TableColumnCompanyName;

    @FXML
    private TableColumn<CompanyInfo, String> TableColumnIndustry;

    @FXML
    private Label LabelInfo;
    @FXML
    private Label LabelUser;
    @FXML
    private TextField TextFieldSearch;


    @FXML
    private TableView<CompanyInfo> TableViewCompanyInfo;

    @FXML
    void TextFieldSearchAction(ActionEvent event) {
        filterData();
    }

    @FXML
    void MenuItemAddAction(ActionEvent event) {
        currentCompanyInfo = null;
        ShowEditWindow("CompanyInfo/companyInfo-edit-view.fxml");
        filterData();
    }

    @FXML
    void MenuItemBackAction(ActionEvent event) {
        Manager.LoadSecondStageScene("main-view.fxml", "ООО Инвестиции");
    }

    @FXML
    void MenuItemDeleteAction(ActionEvent event) {
        CompanyInfo companyInfo = TableViewCompanyInfo.getSelectionModel().getSelectedItem();

        Optional<ButtonType> result = ShowConfirmPopup();
        if (result.get() == ButtonType.OK) {
            companyInfoService.delete(companyInfo);
            filterData();
        }
    }

    @FXML
    void MenuItemUpdateAction(ActionEvent event) {
        CompanyInfo companyInfo = TableViewCompanyInfo.getSelectionModel().getSelectedItem();
        currentCompanyInfo = companyInfo;
        ShowEditWindow("CompanyInfo/companyInfo-edit-view.fxml");
        filterData();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initController();
    }

    public void initController() {
        LabelUser.setText("Вы вошли как " + currentUser.getClient().getFirstName() + " " +
                Manager.currentUser.getClient().getLastName());

        List<CompanyInfo> companyInfoList = companyInfoService.findAll();
        ObservableList<CompanyInfo> companyInfos = FXCollections.observableArrayList(companyInfoList);
        TableViewCompanyInfo.setItems(companyInfos);
        setCellValueFactories();

        filterData();

    }

    void filterData() {
        List<CompanyInfo> companyInfos = companyInfoService.findAll();
        itemsCount = companyInfos.size();

        String searchText = TextFieldSearch.getText();
        if (!searchText.isEmpty()) {
            companyInfos = companyInfos.stream().filter(companyInfo -> companyInfo.getCompanyName().toLowerCase().contains(searchText.toLowerCase())
            ).collect(Collectors.toList());
        }
        TableViewCompanyInfo.getItems().clear();
        for (CompanyInfo companyInfo : companyInfos) {
            TableViewCompanyInfo.getItems().add(companyInfo);
        }
        int filteredItemsCount = companyInfos.size();
        LabelInfo.setText("Всего записей " + filteredItemsCount + " из " + itemsCount);
        TableViewCompanyInfo.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

    }

    private void setCellValueFactories() {
        TableColumnCompanyName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCompanyName()));
        TableColumnIndustry.setCellValueFactory(cellData -> new SimpleStringProperty((cellData.getValue().getIndustry())));
        TableColumnCompanyInfoId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getInfoId().toString()));
        TableColumnMarketCap.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMarketCap().toString()));
        TableColumnInstrument.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getInstrument().getInstrumentType()));
    }
}
