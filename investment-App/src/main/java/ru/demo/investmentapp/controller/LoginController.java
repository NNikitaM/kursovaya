package ru.demo.investmentapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import ru.demo.investmentapp.InvestmentApp;
import ru.demo.investmentapp.model.User;
import ru.demo.investmentapp.util.MakeCaptcha;
import ru.demo.investmentapp.util.Manager;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static ru.demo.investmentapp.util.Manager.*;

public class LoginController implements Initializable {
    boolean isWrongCaptha;
    boolean isShowCaptha;
    String captchaCode;
    int secondsLeft;
    @FXML
    RowConstraints ThirdRow;
    @FXML
    Button BtnRenewCaptcha;
    @FXML
    private Button BtnCancel;
    @FXML
    private Button BtnOk;
    @FXML
    private PasswordField PasswordField;
    @FXML
    private TextField TextFieldUsername;
    @FXML
    private TextField TextFieldCaptcha;
    @FXML
    private ImageView ImageViewCaptcha;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initController();
    }

    @FXML
    void BtnRenewCaptchaAction(ActionEvent event) {
        generateCaptcha();
    }

    @FXML
    void BtnCancelAction(ActionEvent event) {
        Manager.ShowPopup();
    }

    @FXML
    void BtnOkActon(ActionEvent event) {
        List<User> users = userService.findAll();
        Optional<User> person = users.stream().filter(user -> user.getUserName().equals(TextFieldUsername.getText()) &&
                user.getPassword().equals(PasswordField.getText())).findFirst();

        if (person.isEmpty() && isShowCaptha && !TextFieldCaptcha.getText().equals(captchaCode)) {
            System.out.println("Bad error");
            ShowErrorMessageBox("Не верный логин, пароль или текст капчи");
            blockButtons();
            return;
        }
        if (person.isEmpty() && (!isShowCaptha)) {
            System.out.println("Bad error");
            generateCaptcha();
            isShowCaptha = true;
            ThirdRow.setPrefHeight(50);
            ImageViewCaptcha.setVisible(true);
            TextFieldCaptcha.setVisible(true);
            BtnRenewCaptcha.setVisible(true);
            ShowErrorMessageBox("Не верный логин или пароль");
            return;
        }
        if (person.isPresent() && isShowCaptha && !TextFieldCaptcha.getText().equals(captchaCode)) {
            blockButtons();
            ShowErrorMessageBox("Не верный логин, пароль или текст капчи");
            return;
        }

        if (person.isPresent() && isShowCaptha && TextFieldCaptcha.getText().equals(captchaCode)) {
            showMainWindow(person.get());
            return;
        }

        if (person.isPresent() && !isShowCaptha) {
            showMainWindow(person.get());
        }
    }

    public void generateCaptcha() {
        try {
            ImageViewCaptcha.setImage(MakeCaptcha.CreateImage(150, 40, 4));
            captchaCode = MakeCaptcha.captchaCode();
            System.out.println(captchaCode);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void showMainWindow(User person) {
        Manager.currentUser = person;
        System.out.println(Manager.currentUser);
        Manager.mainStage.hide();
        Stage newWindow = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(InvestmentApp.class.getResource("main-view.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), screenSize.getWidth(), screenSize.getHeight());
            scene.getStylesheets().add("base-styles.css");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        newWindow.setTitle("ООО Инвестиции");
        newWindow.setMaximized(true);
        newWindow.setScene(scene);
        newWindow.setOnCloseRequest(e -> {
            Manager.mainStage.show();
        });
        Manager.secondStage = newWindow;

        newWindow.show();
    }

    public void initTimer() {
        TimerTask task = new TimerTask() {
            public void run() {
                System.out.println("Task performed on: " + new Date() + "n" +
                        "Thread's name: " + Thread.currentThread().getName());
                secondsLeft--;
                if (secondsLeft == 0) ;
                {
                    BtnOk.setDisable(false);
                    BtnCancel.setDisable(false);
                    this.cancel();
                }
            }
        };
        Timer timer = new Timer("Timer");

        long delay = 10000L;
        timer.schedule(task, delay);
    }

    public void blockButtons() {
        initTimer();
        secondsLeft = 10;
        BtnOk.setDisable(true);
        BtnCancel.setDisable(true);
    }

    public void initController() {
        ThirdRow.setPrefHeight(0);
        TextFieldUsername.setText("admin");
        PasswordField.setText("1");
        TextFieldCaptcha.setVisible(false);
        BtnRenewCaptcha.setVisible(false);
        ImageViewCaptcha.setVisible(false);
        isWrongCaptha = false;
        isShowCaptha = false;
        captchaCode = "";
        secondsLeft = 0;
    }
}
