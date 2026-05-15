package com.kefawatch.desktop.ui;

import com.fasterxml.jackson.databind.JsonNode;
import com.kefawatch.desktop.api.ApiClient;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TitleDetailView extends VBox {

    public TitleDetailView(long titleId, Runnable onBack) {
        setSpacing(20);
        setPadding(new Insets(30));
        setStyle("-fx-background-color: #141414;");

        Button backBtn = new Button("← Geri Dön");
        backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-cursor: hand; -fx-font-size: 14px;");
        backBtn.setOnAction(e -> onBack.run());

        JsonNode data = ApiClient.getTitleDetails(titleId);
        if (data == null) {
            getChildren().addAll(backBtn, new Label("Detaylar yüklenemedi."));
            return;
        }

        Label titleLabel = new Label(data.path("name").asText("İsimsiz"));
        titleLabel.setStyle("-fx-text-fill: #E50914; -fx-font-size: 36px; -fx-font-weight: bold;");
        titleLabel.setWrapText(true);

        Label descLabel = new Label(data.path("description").asText("Açıklama bulunmuyor."));
        descLabel.setStyle("-fx-text-fill: #E0E0E0; -fx-font-size: 16px;");
        descLabel.setWrapText(true);

        int epCount = data.path("episodes").size();
        Label epLabel = new Label("Toplam Bölüm: " + epCount);
        epLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 14px;");

        Button watchlistBtn = new Button("İzleme Listesine Ekle");
        watchlistBtn.setStyle("-fx-background-color: #E50914; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px 20px; -fx-cursor: hand;");
        
        Button markWatchedBtn = new Button("İzlendi İşaretle");
        markWatchedBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 1px; -fx-border-radius: 4px; -fx-padding: 10px 20px; -fx-cursor: hand;");

        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-text-fill: green;");

        watchlistBtn.setOnAction(e -> {
            if (ApiClient.addToWatchlist(titleId)) {
                statusLabel.setStyle("-fx-text-fill: green;");
                statusLabel.setText("Listeye eklendi!");
            } else {
                statusLabel.setStyle("-fx-text-fill: red;");
                statusLabel.setText("Ekleme başarısız.");
            }
        });

        markWatchedBtn.setOnAction(e -> {
            if (ApiClient.markAsWatched(titleId)) {
                statusLabel.setStyle("-fx-text-fill: green;");
                statusLabel.setText("İzlendi olarak kaydedildi!");
            } else {
                statusLabel.setStyle("-fx-text-fill: red;");
                statusLabel.setText("İşlem başarısız.");
            }
        });

        HBox actions = new HBox(15, watchlistBtn, markWatchedBtn);
        actions.setAlignment(Pos.CENTER_LEFT);

        getChildren().addAll(backBtn, titleLabel, descLabel, epLabel, actions, statusLabel);
    }
}
