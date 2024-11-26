package ru.demo.investmentapp.controller.MarketData;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ru.demo.investmentapp.model.Instrument;
import ru.demo.investmentapp.model.MarketData;
import ru.demo.investmentapp.service.InstrumentService;
import ru.demo.investmentapp.service.MarketDataService;
import ru.demo.investmentapp.util.Manager;

import java.net.URL;
import java.sql.Date;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static ru.demo.investmentapp.util.Manager.MessageBox;
import static ru.demo.investmentapp.util.Manager.currentMarketData;

public class MarketDataEditViewController implements Initializable {

    @FXML
    private Button BtnCancel;

    @FXML
    private Button BtnSave;
    private MarketDataService marketDataService = new MarketDataService();

    @FXML
    private ComboBox<String> ComboBoxInstrument;
    private InstrumentService instrumentService = new InstrumentService();

    @FXML
    private DatePicker DatePickerDataDate;

    @FXML
    private TextField TextFieldPrice;

    @FXML
    private TextField TextFieldVolume;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Instrument> instruments = instrumentService.findAll(); // Получаем список инструментов

        if (instruments == null) {
            // Обработка ошибки: список инструментов пуст
            MessageBox("Ошибка", "Список инструментов пуст", "Пожалуйста, проверьте данные.", Alert.AlertType.ERROR);
            return;
        }

        ObservableList<String> instrumentTypes = FXCollections.observableArrayList(
                instruments.stream()
                        .map(Instrument::getInstrumentType)
                        .distinct() // Добавлено для удаления дубликатов
                        .collect(Collectors.toList())
        );

        ComboBoxInstrument.setItems(instrumentTypes); // Используем instrumentTypes

        if (Manager.currentMarketData != null) {
            DatePickerDataDate.setValue(Manager.currentMarketData.getDataDate().toLocalDate());
            TextFieldVolume.setText(Manager.currentMarketData.getVolume().toString());
            TextFieldPrice.setText(String.format("%.2f", Manager.currentMarketData.getPrice()));
            ComboBoxInstrument.setValue(Manager.currentMarketData.getInstrument().getInstrumentType());
        } else {
            Manager.currentMarketData = new MarketData();
        }
    }

    @FXML
    void BtnCancelAction(ActionEvent event) {
        Stage stage = (Stage) BtnCancel.getScene().getWindow();
        // do what you have to do
        stage.close();
    }

    @FXML
    void BtnSaveAction(ActionEvent event) {
        String error = checkFields().toString();
        if (!error.isEmpty()) {
            MessageBox("Ошибка", "Заполните поля", error, Alert.AlertType.ERROR);
            return;
        }
        currentMarketData.setDataDate(Date.valueOf(DatePickerDataDate.getValue()));
        currentMarketData.setPrice(parseDouble(TextFieldPrice.getText()));
        currentMarketData.setVolume(Integer.parseInt(TextFieldVolume.getText()));
        Instrument instrument = findInstrument(ComboBoxInstrument.getValue());
        if (instrument == null){
            MessageBox("Ошибка", "Инструмент не найден", "Проверьте выбор инструмента.", Alert.AlertType.ERROR);
            return;
        }
        currentMarketData.setInstrument(instrument);

        if (currentMarketData.getDataId() == null) {
            marketDataService.save(currentMarketData);
            MessageBox("Информация", "", "Данные сохранены успешно", Alert.AlertType.INFORMATION);
        } else {
            marketDataService.update(currentMarketData);
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

    private double parseDouble(String text) throws NumberFormatException {
        NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());
        try {
            return nf.parse(text).doubleValue();
        } catch (ParseException ex) {
            throw new NumberFormatException(ex.getMessage());
        }
    }

    StringBuilder checkFields() {
        StringBuilder error = new StringBuilder();
        if (DatePickerDataDate.getValue() == null) {
            error.append("Укажите дату\n");
        }
        String volumeText = TextFieldVolume.getText();
        if (volumeText.isEmpty()) {
            error.append("Укажите объем\n");
        } else {
            try {
                Double.parseDouble(volumeText);
            } catch (NumberFormatException e) {
                error.append("Объем должен быть числом\n");
            }
        }
        String priceText = TextFieldPrice.getText();
        if (priceText.isEmpty()) {
            error.append("Укажите цену\n");
        } else {
            try {
                Double.parseDouble(priceText);
            } catch (NumberFormatException e) {
                error.append("Цена должна быть числом\n");
            }
        }
        if (ComboBoxInstrument.getValue() == null) {
            error.append("Выберите инструмент\n");
        }

        return error;
    }
}
