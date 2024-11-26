package ru.demo.investmentapp.controller.Instrument;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.demo.investmentapp.model.Instrument;
import ru.demo.investmentapp.service.InstrumentService;
import ru.demo.investmentapp.util.Manager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static ru.demo.investmentapp.util.Manager.MessageBox;

public class InstrumentEditViewController implements Initializable {
    @FXML
    private Button BtnSave;
    @FXML
    private Button BtnCancel;
    private InstrumentService instrumentService = new InstrumentService();

    @FXML
    private TextField TextFieldName;
    @FXML
    private TextField TextFieldTickerSymbol;
    @FXML
    private TextField TextFieldType;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (Manager.currentInstrument != null) {
            TextFieldName.setText(Manager.currentInstrument.getName());
            TextFieldType.setText(Manager.currentInstrument.getInstrumentType());
            TextFieldTickerSymbol.setText(Manager.currentInstrument.getTickerSymbol());
        } else {
            Manager.currentInstrument = new Instrument();
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
        Manager.currentInstrument.setName(TextFieldName.getText());
        Manager.currentInstrument.setTickerSymbol(TextFieldTickerSymbol.getText());
        Manager.currentInstrument.setInstrumentType(TextFieldType.getText());
        if (Manager.currentInstrument.getInstrumentId() == null) {
            Manager.currentInstrument.setName(TextFieldName.getText());
            instrumentService.save(Manager.currentInstrument);
            MessageBox("Информация", "", "Данные сохранены успешно", Alert.AlertType.INFORMATION);
        } else {
            instrumentService.update(Manager.currentInstrument);
            MessageBox("Информация", "", "Данные обновлены успешно", Alert.AlertType.INFORMATION);
        }
    }

    StringBuilder checkFields() {
        StringBuilder error = new StringBuilder();
        if (TextFieldName.getText().isEmpty()) {
            error.append("Укажите название\n");
        }
        if (TextFieldType.getText().isEmpty()) {
            error.append("Укажите тип\n");
        }
        if (TextFieldTickerSymbol.getText().isEmpty()) {
            error.append("Укажите тикер\n");
        }
        return error;
    }
}
