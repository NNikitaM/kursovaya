package ru.demo.investmentapp.controller.CompanyInfo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.demo.investmentapp.model.*;
import ru.demo.investmentapp.service.CompanyInfoService;
import ru.demo.investmentapp.service.InstrumentService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static java.lang.Double.parseDouble;
import static ru.demo.investmentapp.util.Manager.*;

public class CompanyInfoEditViewController implements Initializable {
    @FXML
    private Button BtnCancel;

    @FXML
    private Button BtnSave;

    private CompanyInfoService companyInfoService = new CompanyInfoService();
    private InstrumentService instrumentService = new InstrumentService();

    @FXML
    private TextField TextFieldCompanyInfoId;

    @FXML
    private TextField TextFieldCompanyName;

    @FXML
    private TextField TextFieldMarketCap;

    @FXML
    private TextField TextFieldIndustry;

    @FXML
    private ComboBox<String> ComboBoxInstrument;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Instrument> instruments = instrumentService.findAll();

        if (instruments == null) {
            MessageBox("Ошибка", "Список инструментов пуст", "Пожалуйста, проверьте данные.", Alert.AlertType.ERROR);
            return;
        }

        ObservableList<String> instrumentType = FXCollections.observableArrayList(
                instruments.stream()
                        .map(Instrument::getInstrumentType)
                        .distinct()
                        .collect(Collectors.toList())
        );

        ComboBoxInstrument.setItems(instrumentType);

        if (currentCompanyInfo != null) {
            TextFieldCompanyName.setText(currentCompanyInfo.getCompanyName());
            TextFieldIndustry.setText(currentCompanyInfo.getIndustry());
            TextFieldMarketCap.setText(currentCompanyInfo.getMarketCap().toString());
            if (currentCompanyInfo.getInstrument() != null) { // проверка на null
                ComboBoxInstrument.setValue(currentCompanyInfo.getInstrument().getInstrumentType());
            }
        } else {
            currentCompanyInfo = new CompanyInfo();
        }
    }

    @FXML
    void BtnCancelAction(ActionEvent event) {
        Stage stage = (Stage) BtnCancel.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

    @FXML
    void BtnSaveAction(ActionEvent event) throws IOException {
        String error = checkFields().toString();
        if (!error.isEmpty()) {
            MessageBox("Ошибка", "Заполните поля", error, Alert.AlertType.ERROR);
            return;
        }

        currentCompanyInfo.setCompanyName(TextFieldCompanyName.getText());
        currentCompanyInfo.setIndustry(TextFieldIndustry.getText());
        currentCompanyInfo.setMarketCap(parseDouble(TextFieldMarketCap.getText()));

        Instrument instrument = findInstrument(ComboBoxInstrument.getValue());
        if (instrument == null){
            MessageBox("Ошибка", "Инструмент не найден", "Проверьте выбор инструмента.", Alert.AlertType.ERROR);
            return;
        }
        currentCompanyInfo.setInstrument(instrument);

        if (currentCompanyInfo.getInfoId() == null) {
            companyInfoService.save(currentCompanyInfo);
            MessageBox("Информация", "", "Данные сохранены успешно", Alert.AlertType.INFORMATION);
        } else {
            companyInfoService.update(currentCompanyInfo);
            MessageBox("Информация", "", "Данные обновлены успешно", Alert.AlertType.INFORMATION);
        }
    }

    private Instrument findInstrument(String instrumentType) {
        List<Instrument> instruments = instrumentService.findAll();
        for(Instrument instrument: instruments){
            if(instrument.getInstrumentType().equals(instrumentType)){
                return instrument;
            }
        }
        return null;
    }

    StringBuilder checkFields() {
        StringBuilder error = new StringBuilder();
        if (TextFieldCompanyName.getText().isEmpty()) {
            error.append("Укажите название\n");
        }
        if (TextFieldIndustry.getText().isEmpty()) {
            error.append("Укажите промышленность\n");
        }
        String marketCapText = TextFieldMarketCap.getText();
        if (marketCapText.isEmpty()) {
            error.append("Укажите рыночную капитализацию\n");
        } else {
            try {
                Double.parseDouble(marketCapText);
            } catch (NumberFormatException e) {
                error.append("Рыночная капитализация должна быть числом\n");
            }
        }
        if (ComboBoxInstrument.getValue() == null) {
            error.append("Выберите тип инструмента\n");
        }

        return error;
    }
}
