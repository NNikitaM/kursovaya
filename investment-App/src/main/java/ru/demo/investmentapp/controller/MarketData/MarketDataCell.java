package ru.demo.investmentapp.controller.MarketData;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import ru.demo.investmentapp.InvestmentApp;
import ru.demo.investmentapp.model.MarketData;

import java.io.IOException;

public class MarketDataCell extends ListCell<MarketData> {

    private final Parent root;
    private ListCellController controller;

    public MarketDataCell() {
        try {
            FXMLLoader loader = new FXMLLoader(InvestmentApp.class.getResource("MarketData/marketDataCell-view.fxml"));
            root = loader.load();
            root.getStylesheets().add("base-styles.css");
            controller = loader.getController();
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    @Override
    protected void updateItem(MarketData marketData, boolean empty) {
        super.updateItem(marketData, empty);
        if (empty || marketData == null) {
            setGraphic(null);
        } else {
            try {
                controller.setMarketData(marketData);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            setGraphic(root);
        }
    }
}