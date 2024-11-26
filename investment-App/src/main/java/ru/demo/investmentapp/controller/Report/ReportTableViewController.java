package ru.demo.investmentapp.controller.Report;

import com.itextpdf.text.DocumentException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import ru.demo.investmentapp.model.Report;
import ru.demo.investmentapp.service.ReportService;
import ru.demo.investmentapp.util.Manager;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static ru.demo.investmentapp.util.Manager.*;

public class ReportTableViewController implements Initializable {

    private int itemsCount;
    private ReportService reportService = new ReportService();

    @FXML
    private Button BtnBack;

    @FXML
    private TableColumn<Report, String> TableColumnId;

    @FXML
    private TableColumn<Report, String> TableColumnDate;

    @FXML
    private TableColumn<Report, String> TableColumnContent;

    @FXML
    private TableColumn<Report, String> TableColumnType;

    @FXML
    private TableColumn<Report, String> TableColumnUserName;

    @FXML
    private Label LabelInfo;

    @FXML
    private Label LabelUser;

    @FXML
    private TextField TextFieldSearch;


    @FXML
    private TableView<Report> TableViewReports;

    @FXML
    void TextFieldSearchAction(ActionEvent event) {
        filterData();
    }

    @FXML
    void MenuItemAddAction(ActionEvent event) {
        currentReport = null;
        ShowEditWindow("Report/report-edit-view.fxml");
        filterData();
    }

    @FXML
    void MenuItemBackAction(ActionEvent event) {
        Manager.LoadSecondStageScene("main-view.fxml", "ООО Инвестиции");
    }

    @FXML
    void MenuItemPrintToPDFAction(ActionEvent event) throws DocumentException, FileNotFoundException {
        Report report = TableViewReports.getSelectionModel().getSelectedItem();

        if (report != null) {
            Manager.PrintReportToPDF(report);
            MessageBox("Информация", "", "Данные сохранены успешно", Alert.AlertType.INFORMATION);return;
        }
    }

    @FXML
    void MenuItemDeleteAction(ActionEvent event) {
        Report report = TableViewReports.getSelectionModel().getSelectedItem();

        Optional<ButtonType> result = ShowConfirmPopup();
        if (result.get() == ButtonType.OK) {
            reportService.delete(report);
            filterData();
        }
    }

    @FXML
    void MenuItemUpdateAction(ActionEvent event) {
        Report report = TableViewReports.getSelectionModel().getSelectedItem();
        currentReport = report;
        ShowEditWindow("Report/report-edit-view.fxml");
        filterData();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initController();
    }

    public void initController() {
        LabelUser.setText("Вы вошли как " + currentUser.getClient().getFirstName() + " " +
                Manager.currentUser.getClient().getLastName());

        List<Report> reportList = reportService.findAll();
        ObservableList<Report> reports = FXCollections.observableArrayList(reportList);
        TableViewReports.setItems(reports);
        setCellValueFactories();

        filterData();
    }

    void filterData() {
        List<Report> reports = reportService.findAll();
        itemsCount = reports.size();

        String searchText = TextFieldSearch.getText();
        if (!searchText.isEmpty()) {
            reports = reports.stream().filter(report -> report.getReportType().toLowerCase().contains(searchText.toLowerCase())
            ).collect(Collectors.toList());
        }
        TableViewReports.getItems().clear();
        for (Report report: reports) {
            TableViewReports.getItems().add(report);
        }
        int filteredItemsCount = reports.size();
        LabelInfo.setText("Всего записей " + filteredItemsCount + " из " + itemsCount);
        TableViewReports.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

    }

    private void setCellValueFactories() {
        TableColumnType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getReportType()));
        TableColumnDate.setCellValueFactory(cellData -> new SimpleStringProperty((cellData.getValue().getReportDate().toString())));
        TableColumnContent.setCellValueFactory(cellData -> new SimpleStringProperty((cellData.getValue().getReportContent())));
        TableColumnId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getReportId().toString()));
        TableColumnUserName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUser().getUserName()));
    }
}
