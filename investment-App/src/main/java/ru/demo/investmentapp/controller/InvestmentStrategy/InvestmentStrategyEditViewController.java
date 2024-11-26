package ru.demo.investmentapp.controller.InvestmentStrategy;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.demo.investmentapp.model.*;
import ru.demo.investmentapp.service.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static ru.demo.investmentapp.util.Manager.*;

public class InvestmentStrategyEditViewController implements Initializable {
    @FXML
    private Button BtnCancel;

    @FXML
    private Button BtnSave;

    private InvestmentStrategyService investmentStrategyService = new InvestmentStrategyService();

    @FXML
    private TextField TextFieldStrategyName;

    @FXML
    private TextField TextFieldDescription;

    @FXML
    private TextField TextFieldRickLevel;

    @FXML
    private TextField TextFieldReturnTarget;

    @FXML
    private TextField TextFieldAssetAllocation;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if (currentInvestmentStrategy != null) {
            TextFieldStrategyName.setText(currentInvestmentStrategy.getStrategyName());
            TextFieldDescription.setText(currentInvestmentStrategy.getDescription());
            TextFieldAssetAllocation.setText(currentInvestmentStrategy.getAssetAllocation());
            TextFieldRickLevel.setText(currentInvestmentStrategy.getRiskLevel());
            TextFieldReturnTarget.setText(currentInvestmentStrategy.getReturnTarget().toString());
        } else {
            currentInvestmentStrategy = new InvestmentStrategy();
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

        currentInvestmentStrategy.setStrategyName(TextFieldStrategyName.getText());
        currentInvestmentStrategy.setDescription(TextFieldDescription.getText());
        currentInvestmentStrategy.setAssetAllocation(TextFieldAssetAllocation.getText());
        currentInvestmentStrategy.setReturnTarget(Double.parseDouble(TextFieldReturnTarget.getText()));
        currentInvestmentStrategy.setRiskLevel(TextFieldRickLevel.getText());

        if (currentInvestmentStrategy.getStrategyId() == null) {
            investmentStrategyService.save(currentInvestmentStrategy);
            MessageBox("Информация", "", "Данные сохранены успешно", Alert.AlertType.INFORMATION);
        } else {
            investmentStrategyService.update(currentInvestmentStrategy);
            MessageBox("Информация", "", "Данные обновлены успешно", Alert.AlertType.INFORMATION);
        }
    }

    StringBuilder checkFields() {
        StringBuilder error = new StringBuilder();
        if (TextFieldStrategyName.getText().isEmpty()) {
            error.append("Укажите название\n");
        }
        if (TextFieldRickLevel.getText().isEmpty()) {
            error.append("Укажите уровень риска\n");
        }
        String returnTargetText = TextFieldReturnTarget.getText();
        if (returnTargetText.isEmpty()) {
            error.append("Укажите целевая доходность\n");
        } else {
            try {
                Double.parseDouble(returnTargetText);
            } catch (NumberFormatException e) {
                error.append("Целевая доходность должна быть числом\n");
            }
        }
        if (TextFieldAssetAllocation.getText().isEmpty()) {
            error.append("Укажите распределение активов\n");
        }
        return error;
    }
}
