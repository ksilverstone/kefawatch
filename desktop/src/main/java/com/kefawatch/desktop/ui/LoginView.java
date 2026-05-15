package com.kefawatch.desktop.ui;
import com.kefawatch.desktop.api.ApiClient;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;

public class LoginView extends StackPane {
    public LoginView(Runnable onSuccess, Runnable onBack) {
        setStyle("-fx-background-color: radial-gradient(center 50% 50%, radius 100%, #333333, #000000);");
        
        javafx.scene.image.ImageView bg = new javafx.scene.image.ImageView();
        try {
            javafx.scene.image.Image img = new javafx.scene.image.Image("https://image.tmdb.org/t/p/original/rAiYTfKGqDCRIIqo664sY9XZIvQ.jpg", true);
            bg.setImage(img);
        } catch (Exception e) {}
        
        bg.setPreserveRatio(false);
        bg.fitWidthProperty().bind(this.widthProperty());
        bg.fitHeightProperty().bind(this.heightProperty());

        VBox overlay = new VBox();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.6);");

        VBox form = new VBox();
        form.setStyle("-fx-background-color: rgba(0,0,0,0.8); -fx-padding: 60; -fx-background-radius: 8; -fx-border-color: #333; -fx-border-width: 1; -fx-border-radius: 8;");
        form.setAlignment(Pos.CENTER);
        form.setSpacing(25);
        form.setMaxWidth(450);
        form.setMaxHeight(550);

        Label title = new Label("Oturum Aç");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white; -fx-alignment: center-left; -fx-pref-width: 300px;");

        TextField userField = new TextField();
        userField.setPromptText("Kullanıcı Adı");
        userField.setMaxWidth(300);
        userField.setStyle("-fx-background-color: #333; -fx-text-fill: white; -fx-padding: 15; -fx-background-radius: 4; -fx-font-size: 16px; -fx-prompt-text-fill: #aaa;");

        PasswordField passField = new PasswordField();
        passField.setPromptText("Şifre");
        passField.setMaxWidth(300);
        passField.setStyle("-fx-background-color: #333; -fx-text-fill: white; -fx-padding: 15; -fx-background-radius: 4; -fx-font-size: 16px; -fx-prompt-text-fill: #aaa;");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #E50914; -fx-font-weight: bold; -fx-font-size: 14px;");

        Button loginBtn = new Button("Oturum Aç");
        loginBtn.setStyle("-fx-background-color: #E50914; -fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 15 20; -fx-background-radius: 4; -fx-cursor: hand;");
        loginBtn.setMaxWidth(300);
        loginBtn.setOnAction(e -> {
            String err = ApiClient.login(userField.getText(), passField.getText());
            if (err == null) {
                onSuccess.run();
            } else {
                errorLabel.setText(err);
            }
        });

        Button backBtn = new Button("İlk Sayfaya Dön");
        backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #737373; -fx-font-size: 16px; -fx-cursor: hand;");
        backBtn.setOnAction(e -> onBack.run());

        form.getChildren().addAll(title, userField, passField, loginBtn, errorLabel, backBtn);
        
        getChildren().addAll(bg, overlay, form);
    }
}
