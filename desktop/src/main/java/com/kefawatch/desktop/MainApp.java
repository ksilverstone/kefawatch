package com.kefawatch.desktop;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showWelcomeView();
        primaryStage.show();
    }

    private void showWelcomeView() {
        com.kefawatch.desktop.ui.WelcomeView view = new com.kefawatch.desktop.ui.WelcomeView(
                this::showLoginView,
                this::showRegisterView
        );
        primaryStage.setTitle("Kefawatch - Hoş Geldiniz");
        primaryStage.setScene(new Scene(view, 900, 700));
    }

    private void showLoginView() {
        com.kefawatch.desktop.ui.LoginView view = new com.kefawatch.desktop.ui.LoginView(
                this::showMainCatalog,
                this::showWelcomeView
        );
        primaryStage.setTitle("Kefawatch - Giriş Yap");
        primaryStage.getScene().setRoot(view);
    }

    private void showRegisterView() {
        com.kefawatch.desktop.ui.RegisterView view = new com.kefawatch.desktop.ui.RegisterView(
                this::showMainCatalog,
                this::showWelcomeView
        );
        primaryStage.setTitle("Kefawatch - Kayıt Ol");
        primaryStage.getScene().setRoot(view);
    }

    private void showMainCatalog() {
        com.kefawatch.desktop.ui.MainView mainView = new com.kefawatch.desktop.ui.MainView(
                this::showTitleDetail,
                this::showAddTitleView,
                this::showWelcomeView
        );
        primaryStage.setTitle("Kefawatch Masaüstü - Katalog");
        primaryStage.getScene().setRoot(mainView);
    }

    private void showTitleDetail(long id) {
        com.kefawatch.desktop.ui.TitleDetailView detailView = new com.kefawatch.desktop.ui.TitleDetailView(id, this::showMainCatalog);
        primaryStage.setTitle("Kefawatch Masaüstü - Detay");
        primaryStage.getScene().setRoot(detailView);
    }

    private void showAddTitleView() {
        com.kefawatch.desktop.ui.AddTitleView view = new com.kefawatch.desktop.ui.AddTitleView(
                this::showMainCatalog,
                this::showMainCatalog
        );
        primaryStage.setTitle("Kefawatch Masaüstü - Film Ekle");
        primaryStage.getScene().setRoot(view);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
