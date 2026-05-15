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
        com.kefawatch.desktop.ui.MainView mainView = new com.kefawatch.desktop.ui.MainView(id -> {
            showTitleDetail(id);
        });

        primaryStage.setTitle("Kefawatch Masaüstü - Katalog");
        primaryStage.getScene().setRoot(mainView);
    }

    private void showTitleDetail(long id) {
        com.kefawatch.desktop.ui.TitleDetailView detailView = new com.kefawatch.desktop.ui.TitleDetailView(id, this::showMainCatalog);
        primaryStage.setTitle("Kefawatch Masaüstü - Detay");
        primaryStage.getScene().setRoot(detailView);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
