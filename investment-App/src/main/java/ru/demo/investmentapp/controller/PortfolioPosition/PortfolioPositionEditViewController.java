package ru.demo.investmentapp.controller.PortfolioPosition;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ru.demo.investmentapp.model.*;
import ru.demo.investmentapp.service.*;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static ru.demo.investmentapp.util.Manager.*;

public class PortfolioPositionEditViewController implements Initializable {
    @FXML
    private Button BtnCancel;

    @FXML
    private Button BtnSave;

    private PortfolioService portfolioService = new PortfolioService();
    private InstrumentService instrumentService = new InstrumentService();
    private PortfolioPositionService portfolioPositionService = new PortfolioPositionService();

    @FXML
    private TextField TextFieldQuantity;

    @FXML
    private DatePicker DatePickerPurchaseDate;

    @FXML
    private TextField TextFieldPurchasePrice;

    @FXML
    private ComboBox<String> ComboBoxPortfolio;

    @FXML
    private ComboBox<String> ComboBoxInstrument;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Portfolio> portfolios = portfolioService.findAll();

        if (portfolios == null) {
            MessageBox("Ошибка", "Список портфолио пуст", "Пожалуйста, проверьте данные.", Alert.AlertType.ERROR);
            return;
        }

        ObservableList<String> portfolioName = FXCollections.observableArrayList(
                portfolios.stream()
                        .map(Portfolio::getPortfolioName)
                        .distinct()
                        .collect(Collectors.toList())
        );

        ComboBoxPortfolio.setItems(portfolioName);

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

        if (currentPortfolioPosition != null) {
            TextFieldQuantity.setText(currentPortfolioPosition.getQuantity().toString());
            DatePickerPurchaseDate.setValue(currentPortfolioPosition.getPurchaseDate().toLocalDate());
            TextFieldPurchasePrice.setText(currentPortfolioPosition.getPurchasePrice().toString());
            if (currentPortfolioPosition.getInstrument() != null) { // проверка на null
                ComboBoxInstrument.setValue(currentPortfolioPosition.getInstrument().getInstrumentType());
            }
            if (currentPortfolioPosition.getPortfolio() != null) { // проверка на null
                ComboBoxPortfolio.setValue(currentPortfolioPosition.getPortfolio().getPortfolioName());
            }
        } else {
            currentPortfolioPosition = new PortfolioPosition();
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
        currentPortfolioPosition.setQuantity(Integer.valueOf(TextFieldQuantity.getText()));
        currentPortfolioPosition.setPurchaseDate(Date.valueOf(DatePickerPurchaseDate.getValue()));
        currentPortfolioPosition.setPurchasePrice(Double.valueOf(TextFieldPurchasePrice.getText()));

        Portfolio portfolio = findPortfolio(ComboBoxPortfolio.getValue());
        if (portfolio == null){
            MessageBox("Ошибка", "Портфолио не найден", "Проверьте выбор портфолио.", Alert.AlertType.ERROR);
            return;
        }
        currentPortfolioPosition.setPortfolio(portfolio);

        Instrument instrument = findInstrument(ComboBoxInstrument.getValue());
        if (instrument == null){
            MessageBox("Ошибка", "Инструмент не найден", "Проверьте выбор интсрумента.", Alert.AlertType.ERROR);
            return;
        }
        currentPortfolioPosition.setInstrument(instrument);

        if (currentPortfolioPosition.getPositionId() == null) {
            portfolioPositionService.save(currentPortfolioPosition);
            MessageBox("Информация", "", "Данные сохранены успешно", Alert.AlertType.INFORMATION);
        } else {
            portfolioPositionService.update(currentPortfolioPosition);
            MessageBox("Информация", "", "Данные обновлены успешно", Alert.AlertType.INFORMATION);
        }
    }

    private Portfolio findPortfolio(String portfolioName) {
        List<Portfolio> portfolios = portfolioService.findAll();
        for (Portfolio portfolio : portfolios) {
            if (portfolio.getPortfolioName().equals(portfolioName)) {
                return portfolio;
            }
        }
        return null;
    }

    private Instrument findInstrument(String instrumentType) {
        List<Instrument> instruments = instrumentService.findAll();
        for (Instrument instrument : instruments) {
            if (instrument.getInstrumentType().equals(instrumentType)) {
                return instrument;
            }
        }
        return null;
    }

    StringBuilder checkFields() {
        StringBuilder error = new StringBuilder();
        String quantityText = TextFieldQuantity.getText();
        if (quantityText.isEmpty()) {
            error.append("Укажите количество\n");
        } else {
            try {
                Double.parseDouble(quantityText);
            } catch (NumberFormatException e) {
                error.append("Количество должно быть числом\n");
            }
        }
        String purchasePriceText = TextFieldPurchasePrice.getText();
        if (purchasePriceText.isEmpty()) {
            error.append("Укажите цену\n");
        } else {
            try {
                Double.parseDouble(purchasePriceText);
            } catch (NumberFormatException e) {
                error.append("Цена должна быть числом\n");
            }
        }
        if (DatePickerPurchaseDate.getValue() == null) {
            error.append("Укажите дату\n");
        }
        if (ComboBoxInstrument.getValue() == null) {
            error.append("Выберите тип инструмента\n");
        }
        if (ComboBoxPortfolio.getValue() == null) {
            error.append("Выберите название\n");
        }

        return error;
    }
}
