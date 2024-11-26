package ru.demo.investmentapp.controller.PortfolioStrategy;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import ru.demo.investmentapp.model.*;
import ru.demo.investmentapp.service.*;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static ru.demo.investmentapp.util.Manager.*;

public class PortfolioStrategyEditViewController implements Initializable {
    @FXML
    private Button BtnCancel;

    @FXML
    private Button BtnSave;

    private PortfolioService portfolioService = new PortfolioService();
    private InvestmentStrategyService investmentStrategyService = new InvestmentStrategyService();
    private PortfolioStrategyService portfolioStrategyService = new PortfolioStrategyService();

    @FXML
    private ComboBox<String> ComboBoxPortfolio;

    @FXML
    private ComboBox<String> ComboBoxStrategy;

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

        List<InvestmentStrategy> investmentStrategies = investmentStrategyService.findAll();

        if (investmentStrategies == null) {
            MessageBox("Ошибка", "Список стратегий пуст", "Пожалуйста, проверьте данные.", Alert.AlertType.ERROR);
            return;
        }

        ObservableList<String> strategyName = FXCollections.observableArrayList(
                investmentStrategies.stream()
                        .map(InvestmentStrategy::getStrategyName)
                        .distinct()
                        .collect(Collectors.toList())
        );

        ComboBoxStrategy.setItems(strategyName);

        if (currentPortfolioStrategy != null) {
            if (currentPortfolioStrategy.getStrategy() != null) { // проверка на null
                ComboBoxStrategy.setValue(currentPortfolioStrategy.getStrategy().getStrategyName());
            }
            if (currentPortfolioStrategy.getPortfolio() != null) { // проверка на null
                ComboBoxPortfolio.setValue(currentPortfolioStrategy.getPortfolio().getPortfolioName());
            }
        } else {
            currentPortfolioStrategy = new PortfolioStrategy();
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

        Portfolio portfolio = findPortfolio(ComboBoxPortfolio.getValue());
        if (portfolio == null){
            MessageBox("Ошибка", "Портфолио не найден", "Проверьте выбор портфолио.", Alert.AlertType.ERROR);
            return;
        }
        currentPortfolioStrategy.setPortfolio(portfolio);

        InvestmentStrategy investmentStrategy = findStrategy(ComboBoxStrategy.getValue());
        if (investmentStrategy == null){
            MessageBox("Ошибка", "Стратегия не найдена", "Проверьте выбор стратегии.", Alert.AlertType.ERROR);
            return;
        }
        currentPortfolioStrategy.setStrategy(investmentStrategy);

        if (currentPortfolioStrategy.getPortfolioStrategyId() == null) {
            portfolioStrategyService.save(currentPortfolioStrategy);
            MessageBox("Информация", "", "Данные сохранены успешно", Alert.AlertType.INFORMATION);
        } else {
            portfolioStrategyService.update(currentPortfolioStrategy);
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

    private InvestmentStrategy findStrategy(String strategyName) {
        List<InvestmentStrategy> investmentStrategies = investmentStrategyService.findAll();
        for (InvestmentStrategy investmentStrategy : investmentStrategies) {
            if (investmentStrategy.getStrategyName().equals(strategyName)) {
                return investmentStrategy;
            }
        }
        return null;
    }

    StringBuilder checkFields() {
        StringBuilder error = new StringBuilder();
        if (ComboBoxStrategy.getValue() == null) {
            error.append("Выберите стратегию\n");
        }
        if (ComboBoxPortfolio.getValue() == null) {
            error.append("Выберите название\n");
        }

        return error;
    }
}
