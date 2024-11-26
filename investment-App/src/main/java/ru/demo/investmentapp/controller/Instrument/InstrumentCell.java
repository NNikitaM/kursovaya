package ru.demo.investmentapp.controller.Instrument;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import ru.demo.investmentapp.InvestmentApp;
import ru.demo.investmentapp.model.Instrument;

import java.io.IOException;

public class InstrumentCell extends ListCell<Instrument> {

    private final Parent root;
    private ListCellController controller;

    public InstrumentCell() {
        try {
            FXMLLoader loader = new FXMLLoader(InvestmentApp.class.getResource("Instrument/instrumentCell-view.fxml"));
            root = loader.load();
            root.getStylesheets().add("base-styles.css");
            controller = loader.getController();
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    @Override
    protected void updateItem(Instrument instrument, boolean empty) {
        super.updateItem(instrument, empty);
        if (empty || instrument == null) {
            setGraphic(null);
        } else {
            try {
                controller.setInstrument(instrument);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            setGraphic(root);
        }
    }
}
