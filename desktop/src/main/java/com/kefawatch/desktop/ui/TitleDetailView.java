package com.kefawatch.desktop.ui;

import com.fasterxml.jackson.databind.JsonNode;
import com.kefawatch.desktop.api.ApiClient;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TitleDetailView extends VBox {

    public TitleDetailView(long titleId, Runnable onBack) {
        setPadding(new Insets(0));
        setStyle("-fx-background-color: #141414;");

        JsonNode data = ApiClient.getTitleDetails(titleId);
        
        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(20));
        topBar.setStyle("-fx-background-color: transparent;");
        Button backBtn = new Button("← Geri Dön");
        backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-cursor: hand; -fx-font-size: 16px; -fx-font-weight: bold;");
        backBtn.setOnAction(e -> onBack.run());
        topBar.getChildren().add(backBtn);

        if (data == null) {
            getChildren().addAll(topBar, new Label("Detaylar yüklenemedi."));
            return;
        }

        HBox contentBox = new HBox(50);
        contentBox.setPadding(new Insets(30, 50, 50, 50));

        String posterUrl = data.path("posterUrl").asText("");
        ImageView imageView = new ImageView();
        imageView.setFitWidth(350);
        imageView.setFitHeight(525);
        imageView.setPreserveRatio(false);

        if (!posterUrl.isEmpty() && !posterUrl.equals("null")) {
            try {
                Image img = new Image(posterUrl, true);
                imageView.setImage(img);
            } catch (Exception e) {}
        }

        VBox detailsBox = new VBox(20);
        detailsBox.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label(data.path("name").asText("İsimsiz"));
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 48px; -fx-font-weight: bold;");
        titleLabel.setWrapText(true);

        Label descLabel = new Label(data.path("description").asText("Açıklama bulunmuyor."));
        descLabel.setStyle("-fx-text-fill: #E0E0E0; -fx-font-size: 18px;");
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(600);

        Label typeLabel = new Label(data.path("type").asText("").equals("MOVIE") ? "Sinema Filmi" : "Dizi");
        typeLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 16px;");

        Button watchlistBtn = new Button("➕ Listeme Ekle");
        watchlistBtn.setStyle("-fx-background-color: rgba(255,255,255,0.1); -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18px; -fx-padding: 10px 30px; -fx-background-radius: 4px; -fx-cursor: hand;");
        
        Button markWatchedBtn = new Button("✔ İzlendi");
        markWatchedBtn.setStyle("-fx-background-color: #E50914; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18px; -fx-padding: 10px 30px; -fx-background-radius: 4px; -fx-cursor: hand;");

        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        watchlistBtn.setOnAction(e -> {
            if (ApiClient.addToWatchlist(titleId)) {
                statusLabel.setStyle("-fx-text-fill: #4CAF50;");
                statusLabel.setText("Listeye başarıyla eklendi.");
            } else {
                statusLabel.setStyle("-fx-text-fill: #E50914;");
                statusLabel.setText("Ekleme işlemi başarısız (Zaten listede olabilir).");
            }
        });

        markWatchedBtn.setOnAction(e -> {
            if (ApiClient.markAsWatched(titleId)) {
                statusLabel.setStyle("-fx-text-fill: #4CAF50;");
                statusLabel.setText("İzlendi olarak kaydedildi!");
            } else {
                statusLabel.setStyle("-fx-text-fill: #E50914;");
                statusLabel.setText("Kayıt işlemi başarısız.");
            }
        });

        HBox actions = new HBox(15, markWatchedBtn, watchlistBtn);
        actions.setAlignment(Pos.CENTER_LEFT);

        detailsBox.getChildren().addAll(titleLabel, typeLabel, descLabel, actions, statusLabel);
        contentBox.getChildren().addAll(imageView, detailsBox);

        getChildren().addAll(topBar, contentBox);
    }
}
