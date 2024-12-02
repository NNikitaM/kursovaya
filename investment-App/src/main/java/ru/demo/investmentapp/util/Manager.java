package ru.demo.investmentapp.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import ru.demo.investmentapp.InvestmentApp;
import ru.demo.investmentapp.controller.MainWindowController;
import ru.demo.investmentapp.model.*;
import ru.demo.investmentapp.service.*;

import java.io.*;
import java.util.List;
import java.util.Optional;

public class Manager {
    public static Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
    public static UserService userService = new UserService();
    public static ClientService clientService = new ClientService();
    public static MarketDataService marketDataService = new MarketDataService();
    public static ReportService reportService = new ReportService();
    public static PortfolioService portfolioService = new PortfolioService();


    public static MainWindowController mainWindowController;
    public static User currentUser = null;
    public static MarketData currentMarketData;
    public static Stage mainStage;
    public static Stage secondStage;
    public static Stage currentStage;
    public static Role currentRole;
    public static User currentEditUser;
    public static Instrument currentInstrument;
    public static Report currentReport;
    public static Portfolio currentPortfolio;
    public static PortfolioStrategy currentPortfolioStrategy;
    public static PortfolioPosition currentPortfolioPosition;
    public static Account currentAccount;
    public static Client currentClient;
    public static Commission currentCommission;
    public static CompanyInfo currentCompanyInfo;
    public static InvestmentStrategy currentInvestmentStrategy;
    public static Transaction currentTransaction;

    public static void ShowPopup() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Закрыть приложение");
        alert.setHeaderText("Вы хотите выйти из приложения?");
        alert.setContentText("Все несохраненные данные, будут утеряны");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Platform.exit();
        }
    }

    public static void ShowErrorMessageBox(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    public static void MessageBox(String title, String header, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static Optional<ButtonType> ShowConfirmPopup() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Удаление");
        alert.setHeaderText("Вы действительно хотите удалить запись?");
        alert.setContentText("Также будут удалены все зависимые от этой записи данные");
        Optional<ButtonType> result = alert.showAndWait();
        return result;
    }

    public static void LoadSecondStageScene(String fxmlFileName, String title)
    {
        FXMLLoader fxmlLoader = new FXMLLoader(InvestmentApp.class.getResource(fxmlFileName));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), screenSize.getWidth(), screenSize.getHeight()- 50);
            scene.getStylesheets().add("base-styles.css");
            Manager.secondStage.setScene(scene);
            Manager.secondStage.setMaximized(true);
            Manager.secondStage.setTitle(title);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void ShowEditWindow(String fxmlFileName) {
        Stage newWindow = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(InvestmentApp.class.getResource(fxmlFileName));

        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add("base-styles.css");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        newWindow.setTitle("Изменить данные");
        newWindow.initOwner(Manager.secondStage);
        newWindow.setResizable(false);
        newWindow.initModality(Modality.WINDOW_MODAL);
        newWindow.setScene(scene);
        Manager.currentStage = newWindow;
        newWindow.showAndWait();
        Manager.currentStage = null;
    }

    public static void PrintClientToPDF(Client client) throws FileNotFoundException, DocumentException {
        String FONT = "src/main/resources/fonts/arial.ttf";
        List<Client> clients = clientService.findAll();

        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.PDF)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(mainStage);

        if (file != null) {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));
            Font font = FontFactory.getFont(FONT, "cp1251", BaseFont.EMBEDDED, 10);
            document.open();
            document.add(new Paragraph("Пользователь: " + clients.getFirst().getFullName(), font));

            document.add(Chunk.NEWLINE);
            PdfPTable table = new PdfPTable(new float[]{10, 10, 10, 10, 10, 10, 10, 10, 10, 10 });
            PdfPCell header = new PdfPCell();
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header.setBorderWidth(2);
            header.setPhrase(new Phrase("№", font));
            table.addCell(header);
            header.setPhrase(new Phrase("Имя", font));
            table.addCell(header);
            header.setPhrase(new Phrase("Фамилия", font));
            table.addCell(header);
            header.setPhrase(new Phrase("Отчество", font));
            table.addCell(header);
            header.setPhrase(new Phrase("Адрес", font));
            table.addCell(header);
            header.setPhrase(new Phrase("Email", font));
            table.addCell(header);
            header.setPhrase(new Phrase("Телефон", font));
            table.addCell(header);
            header.setPhrase(new Phrase("Тип клиента", font));
            table.addCell(header);
            header.setPhrase(new Phrase("Дата рождения", font));
            table.addCell(header);
            header.setPhrase(new Phrase("Паспорт", font));
            table.addCell(header);
            table.setWidthPercentage(100);

            int k = 1;
            for (Client item : clients) {
                table.addCell(String.valueOf(k));
                PdfPCell title = new PdfPCell();
                title.setPhrase(new Phrase(item.getFirstName(), font));
                table.addCell(title);

                title.setPhrase(new Phrase(item.getLastName(), font));
                table.addCell(title);

                title.setPhrase(new Phrase(item.getMiddleName() , font));
                table.addCell(title);

                title.setPhrase(new Phrase(item.getAddress(), font));
                table.addCell(title);

                title.setPhrase(new Phrase(item.getEmail(), font));
                table.addCell(title);

                title.setPhrase(new Phrase(item.getPhone(), font));
                table.addCell(title);

                title.setPhrase(new Phrase(item.getClientType(), font));
                table.addCell(title);

                title.setPhrase(new Phrase(String.valueOf(item.getDateOfBirth()), font));
                table.addCell(title);

                title.setPhrase(new Phrase(item.getPassportNumber(), font));
                table.addCell(title);
                k++;
            }

            document.add(table);
            document.close();
        }
    }

    public static void PrintMarketDataToPDF(MarketData marketData) throws FileNotFoundException, DocumentException {
        String FONT = "src/main/resources/fonts/arial.ttf";
        List<MarketData> marketDatas = marketDataService.findAll();

        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.PDF)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(mainStage);

        if (file != null) {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));
            Font font = FontFactory.getFont(FONT, "cp1251", BaseFont.EMBEDDED, 10);
            document.open();

            document.add(Chunk.NEWLINE);
            PdfPTable table = new PdfPTable(new float[]{10, 18, 18, 18, 18});
            PdfPCell header = new PdfPCell();
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header.setBorderWidth(2);
            header.setPhrase(new Phrase("№", font));
            table.addCell(header);
            header.setPhrase(new Phrase("Инструмент", font));
            table.addCell(header);
            header.setPhrase(new Phrase("Дата", font));
            table.addCell(header);
            header.setPhrase(new Phrase("Цена", font));
            table.addCell(header);
            header.setPhrase(new Phrase("Количество", font));
            table.addCell(header);
            table.setWidthPercentage(100);

            int k = 1;
            for (MarketData item : marketDatas) {
                table.addCell(String.valueOf(k));
                PdfPCell title = new PdfPCell();
                title.setPhrase(new Phrase(item.getInstrument().getInstrumentType(), font));
                table.addCell(title);

                title.setPhrase(new Phrase(String.valueOf(item.getDataDate()), font));
                table.addCell(title);

                title.setPhrase(new Phrase(String.valueOf(item.getPrice()), font));
                table.addCell(title);

                title.setPhrase(new Phrase(String.valueOf(item.getVolume()), font));
                table.addCell(title);

                k++;
            }

            document.add(table);
            document.close();
        }
    }

    public static void PrintReportToPDF(Report report) throws FileNotFoundException, DocumentException {
        String FONT = "src/main/resources/fonts/arial.ttf";
        List<Report> reports = reportService.findAll();

        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.PDF)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(mainStage);

        if (file != null) {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));
            Font font = FontFactory.getFont(FONT, "cp1251", BaseFont.EMBEDDED, 10);
            document.open();
            document.add(new Paragraph("Тип отчета: " + reports.getFirst().getReportType(), font));

            document.add(Chunk.NEWLINE);
            PdfPTable table = new PdfPTable(new float[]{12, 22, 22, 22, 22});
            PdfPCell header = new PdfPCell();
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header.setBorderWidth(2);
            header.setPhrase(new Phrase("№", font));
            table.addCell(header);
            header.setPhrase(new Phrase("Тип отчета", font));
            table.addCell(header);
            header.setPhrase(new Phrase("Дата", font));
            table.addCell(header);
            header.setPhrase(new Phrase("Содержание", font));
            table.addCell(header);
            header.setPhrase(new Phrase("Логин пользователя", font));
            table.addCell(header);
            table.setWidthPercentage(100);

            int k = 1;
            for (Report item : reports) {
                table.addCell(String.valueOf(k));
                PdfPCell title = new PdfPCell();
                title.setPhrase(new Phrase(item.getReportType(), font));
                table.addCell(title);

                title.setPhrase(new Phrase(String.valueOf(item.getReportDate()), font));
                table.addCell(title);

                title.setPhrase(new Phrase(item.getReportContent(), font));
                table.addCell(title);

                title.setPhrase(new Phrase(item.getUser().getUserName(), font));
                table.addCell(title);

                k++;
            }

            document.add(table);
            document.close();
        }
    }

    public static void PrintPortfolioToPDF(Portfolio portfolio) throws FileNotFoundException, DocumentException {
        String FONT = "src/main/resources/fonts/arial.ttf";
        List<Portfolio> portfolios = portfolioService.findAll();

        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.PDF)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(mainStage);

        if (file != null) {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));
            Font font = FontFactory.getFont(FONT, "cp1251", BaseFont.EMBEDDED, 10);
            document.open();
            document.add(new Paragraph("Название портфолио: " + portfolios.getFirst().getPortfolioName(), font));

            document.add(Chunk.NEWLINE);
            PdfPTable table = new PdfPTable(new float[]{10, 30, 30, 30});
            PdfPCell header = new PdfPCell();
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header.setBorderWidth(2);
            header.setPhrase(new Phrase("№", font));
            table.addCell(header);
            header.setPhrase(new Phrase("Логин пользователя", font));
            table.addCell(header);
            header.setPhrase(new Phrase("Тип портфолио", font));
            table.addCell(header);
            header.setPhrase(new Phrase("Название", font));
            table.addCell(header);
            table.setWidthPercentage(100);

            int k = 1;
            for (Portfolio item : portfolios) {
                table.addCell(String.valueOf(k));
                PdfPCell title = new PdfPCell();
                title.setPhrase(new Phrase(item.getUser().getUserName(), font));
                table.addCell(title);

                title.setPhrase(new Phrase(item.getPortfolioType(), font));
                table.addCell(title);

                title.setPhrase(new Phrase(item.getPortfolioName(), font));
                table.addCell(title);

                k++;
            }

            document.add(table);
            document.close();
        }
    }
}