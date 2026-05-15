package com.kefawatch.desktop.ui;

import com.kefawatch.desktop.api.ApiClient;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class LoginView extends VBox {

    private final Runnable onLoginSuccess;

    public LoginView(Runnable onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;

        setAlignment(Pos.CENTER);
        setSpacing(15);
        setPadding(new Insets(40));
        setStyle("-fx-background-color: #141414;");

        Label title = new Label("Kefawatch");
        title.setStyle("-fx-text-fill: #E50914; -fx-font-size: 32px; -fx-font-weight: bold;");

        TextField userField = new TextField();
        userField.setPromptText("Kullanıcı Adı");
        userField.setMaxWidth(300);
        userField.setStyle("-fx-background-color: #333333; -fx-text-fill: white; -fx-prompt-text-fill: gray; -fx-padding: 10px;");

        PasswordField passField = new PasswordField();
        passField.setPromptText("Şifre");
        passField.setMaxWidth(300);
        passField.setStyle("-fx-background-color: #333333; -fx-text-fill: white; -fx-prompt-text-fill: gray; -fx-padding: 10px;");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        Button loginBtn = new Button("Giriş Yap");
        loginBtn.setMaxWidth(300);
        loginBtn.setStyle("-fx-background-color: #E50914; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px; -fx-cursor: hand;");
        
        Button registerBtn = new Button("Kayıt Ol");
        registerBtn.setMaxWidth(300);
        registerBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #E50914; -fx-border-color: #E50914; -fx-border-width: 2px; -fx-border-radius: 4px; -fx-font-weight: bold; -fx-padding: 8px; -fx-cursor: hand;");

        loginBtn.setOnAction(e -> {
            boolean success = ApiClient.login(userField.getText(), passField.getText());
            if (success) {
                onLoginSuccess.run();
            } else {
                errorLabel.setText("Giriş başarısız! Bilgileri kontrol edin.");
            }
        });

        registerBtn.setOnAction(e -> {
            boolean success = ApiClient.register(userField.getText(), passField.getText());
            if (success) {
                onLoginSuccess.run();
            } else {
                errorLabel.setText("Kayıt başarısız! Kullanıcı adı alınmış olabilir.");
            }
        });

        getChildren().addAll(title, userField, passField, errorLabel, loginBtn, registerBtn);
    }
}
