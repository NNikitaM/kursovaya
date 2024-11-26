package ru.demo.investmentapp.controller.Instrument;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import ru.demo.investmentapp.model.Instrument;
import ru.demo.investmentapp.model.MarketData;
import ru.demo.investmentapp.service.MarketDataService;

import java.io.IOException;

public class ListCellController {

    Instrument currentInstrument;

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

    public void setInstrument(Instrument instrument) throws IOException {
        currentInstrument = instrument;
        LabelInstrument.setText(instrument.getName());
        LabelInstrumentType.setText(instrument.getInstrumentType());
        LabelTickerSymbol.setText(instrument.getTickerSymbol());

        // Получение MarketData по instrumentId
        MarketData marketData = marketDataService.findByInstrumentId(instrument.getInstrumentId());

        if (marketData != null) {
            LabelDate.setText(marketData.getDataDate().toString());
            LabelPrice.setText(String.format("%.2f", marketData.getPrice()) + " руб.");
            LabelVolume.setText(String.valueOf(marketData.getVolume()));
        } else {
            // Обработка ситуации, если marketData не найден
            LabelDate.setText("N/A");
            LabelPrice.setText("N/A");
            LabelVolume.setText("N/A");
        }
    }
}