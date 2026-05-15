package com.kefawatch.desktop;

import com.kefawatch.desktop.ui.LoginView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showLoginScreen();
    }

    private void showLoginScreen() {
        LoginView loginView = new LoginView(this::showMainCatalog);
        Scene scene = new Scene(loginView, 1024, 768);
        primaryStage.setTitle("Kefawatch Masaüstü - Giriş");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showMainCatalog() {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #141414;");
        Label label = new Label("Giriş Başarılı! Katalog yapım aşamasında...");
        label.setStyle("-fx-text-fill: white; -fx-font-size: 20px;");
        root.getChildren().add(label);

        primaryStage.setTitle("Kefawatch Masaüstü - Katalog");
        primaryStage.getScene().setRoot(root);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
