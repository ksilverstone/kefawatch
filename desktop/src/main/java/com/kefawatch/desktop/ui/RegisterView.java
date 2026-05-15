package com.kefawatch.desktop.ui;
import com.kefawatch.desktop.api.ApiClient;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;

public class RegisterView extends StackPane {
    public RegisterView(Runnable onSuccess, Runnable onBack) {
        setStyle("-fx-background-color: radial-gradient(center 50% 50%, radius 100%, #333333, #000000);");
        
        javafx.scene.image.ImageView bg = new javafx.scene.image.ImageView();
        try {
            javafx.scene.image.Image img = new javafx.scene.image.Image("https://image.tmdb.org/t/p/original/hkBaDkMWbLaf8B1lsWsKX7Ew3Xq.jpg", true);
            bg.setImage(img);
        } catch (Exception e) {}
        
        bg.setPreserveRatio(false);
        bg.fitWidthProperty().bind(this.widthProperty());
        bg.fitHeightProperty().bind(this.heightProperty());

        VBox overlay = new VBox();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.6);");

        VBox form = new VBox();
        form.setStyle("-fx-background-color: rgba(0,0,0,0.8); -fx-padding: 40 60; -fx-background-radius: 8; -fx-border-color: #333; -fx-border-width: 1; -fx-border-radius: 8;");
        form.setAlignment(Pos.CENTER);
        form.setSpacing(15);
        form.setMaxWidth(450);

        Label title = new Label("Kayıt Ol");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: white; -fx-alignment: center-left; -fx-pref-width: 300px;");

        TextField nameField = new TextField(); nameField.setPromptText("Adınız"); nameField.setMaxWidth(300); nameField.setStyle("-fx-background-color: #333; -fx-text-fill: white; -fx-padding: 15; -fx-background-radius: 4; -fx-prompt-text-fill: #aaa;");
        TextField surnameField = new TextField(); surnameField.setPromptText("Soyadınız"); surnameField.setMaxWidth(300); surnameField.setStyle("-fx-background-color: #333; -fx-text-fill: white; -fx-padding: 15; -fx-background-radius: 4; -fx-prompt-text-fill: #aaa;");
        TextField emailField = new TextField(); emailField.setPromptText("Email Adresi"); emailField.setMaxWidth(300); emailField.setStyle("-fx-background-color: #333; -fx-text-fill: white; -fx-padding: 15; -fx-background-radius: 4; -fx-prompt-text-fill: #aaa;");
        TextField userField = new TextField(); userField.setPromptText("Kullanıcı Adı"); userField.setMaxWidth(300); userField.setStyle("-fx-background-color: #333; -fx-text-fill: white; -fx-padding: 15; -fx-background-radius: 4; -fx-prompt-text-fill: #aaa;");
        PasswordField passField = new PasswordField(); passField.setPromptText("Şifre (En az 6 karakter)"); passField.setMaxWidth(300); passField.setStyle("-fx-background-color: #333; -fx-text-fill: white; -fx-padding: 15; -fx-background-radius: 4; -fx-prompt-text-fill: #aaa;");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #E50914; -fx-font-weight: bold;");

        Button regBtn = new Button("Üye Ol");
        regBtn.setStyle("-fx-background-color: #E50914; -fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 15 20; -fx-background-radius: 4; -fx-cursor: hand;");
        regBtn.setMaxWidth(300);
        regBtn.setOnAction(e -> {
            String err = ApiClient.register(userField.getText(), emailField.getText(), nameField.getText(), surnameField.getText(), passField.getText());
            if (err == null) {
                onSuccess.run();
            } else {
                errorLabel.setText(err);
            }
        });

        Button backBtn = new Button("İlk Sayfaya Dön");
        backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #737373; -fx-font-size: 16px; -fx-cursor: hand;");
        backBtn.setOnAction(e -> onBack.run());

        form.getChildren().addAll(title, nameField, surnameField, emailField, userField, passField, regBtn, errorLabel, backBtn);
        
        getChildren().addAll(bg, overlay, form);
    }
}
