package ru.demo.investmentapp.controller.MarketData;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import ru.demo.investmentapp.model.MarketData;
import ru.demo.investmentapp.service.MarketDataService;

import java.io.IOException;

public class ListCellController {

    MarketData currentMarketData;

    MarketDataService marketDataService = new MarketDataService();

    @FXML
    private Label LabelInstrument;

    @FXML
    private Label LabelInstrumentType;

    @FXML
    private Label LabelTickerSymbol;

    @FXML
    private Label LabelDate;

    @FXML
    private Label LabelPrice;

    @FXML
    private Label LabelVolume;

    @FXML
    private AnchorPane CellAnchorPane;

    public void setMarketData(MarketData marketData) throws IOException {
        currentMarketData = marketData;
        LabelInstrument.setText(marketData.getInstrument().getName());
        LabelInstrumentType.setText(marketData.getInstrument().getInstrumentType().toString());
        LabelTickerSymbol.setText(marketData.getInstrument().getTickerSymbol());
        LabelDate.setText(marketData.getDataDate().toString());
        LabelPrice.setText(String.format("%.2f", marketData.getPrice()) + " руб.");
        LabelVolume.setText(String.valueOf(marketData.getVolume()));
    }
}