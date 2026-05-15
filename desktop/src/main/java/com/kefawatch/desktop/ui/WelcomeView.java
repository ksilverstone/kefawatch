package com.kefawatch.desktop.ui;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class WelcomeView extends VBox {
    public WelcomeView(Runnable onLoginClick, Runnable onRegisterClick) {
        setStyle("-fx-background-color: #141414; -fx-padding: 50;");
        setAlignment(Pos.CENTER);
        setSpacing(30);

        Label titleLabel = new Label("KEFAWATCH");
        titleLabel.setStyle("-fx-font-size: 56px; -fx-font-weight: bold; -fx-text-fill: #E50914;");

        Label subLabel = new Label("Sınırsız film ve dizi takibi. İstediğin yerde, istediğin zaman.");
        subLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");

        Button loginBtn = new Button("Giriş Yap");
        loginBtn.setStyle("-fx-background-color: #E50914; -fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 15 50; -fx-background-radius: 5; -fx-cursor: hand;");
        loginBtn.setOnAction(e -> onLoginClick.run());

        Button registerBtn = new Button("Kayıt Ol");
        registerBtn.setStyle("-fx-background-color: transparent; -fx-border-color: white; -fx-border-width: 2; -fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 13 50; -fx-border-radius: 5; -fx-cursor: hand;");
        registerBtn.setOnAction(e -> onRegisterClick.run());

        getChildren().addAll(titleLabel, subLabel, loginBtn, registerBtn);
    }
}
