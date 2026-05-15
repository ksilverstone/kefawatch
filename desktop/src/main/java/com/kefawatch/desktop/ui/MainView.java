package com.kefawatch.desktop.ui;

import com.fasterxml.jackson.databind.JsonNode;
import com.kefawatch.desktop.api.ApiClient;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class MainView extends VBox {

    private final Runnable onTitleSelected; // for step 3

    public MainView(Runnable onTitleSelected) {
        this.onTitleSelected = onTitleSelected;

        setSpacing(20);
        setPadding(new Insets(20));
        setStyle("-fx-background-color: #141414;");

        Label header = new Label("Katalog");
        header.setStyle("-fx-text-fill: white; -fx-font-size: 28px; -fx-font-weight: bold;");

        FlowPane catalogPane = new FlowPane();
        catalogPane.setHgap(20);
        catalogPane.setVgap(20);
        catalogPane.setAlignment(Pos.TOP_LEFT);

        ScrollPane scrollPane = new ScrollPane(catalogPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: #141414;");

        getChildren().addAll(header, scrollPane);

        loadTitles(catalogPane);
    }

    private void loadTitles(FlowPane catalogPane) {
        JsonNode items = ApiClient.getTitles();
        if (items != null && items.isArray()) {
            for (JsonNode item : items) {
                VBox card = createTitleCard(item);
                catalogPane.getChildren().add(card);
            }
        } else {
            Label errorLabel = new Label("Filmler yüklenemedi.");
            errorLabel.setStyle("-fx-text-fill: red;");
            catalogPane.getChildren().add(errorLabel);
        }
    }

    private VBox createTitleCard(JsonNode item) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: #1F1F1F; -fx-background-radius: 8px; -fx-cursor: hand;");
        card.setPrefWidth(200);

        Label nameLabel = new Label(item.path("name").asText("İsimsiz"));
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        nameLabel.setWrapText(true);

        Label typeLabel = new Label(item.path("type").asText(""));
        typeLabel.setStyle("-fx-text-fill: gray; -fx-font-size: 12px;");

        card.getChildren().addAll(nameLabel, typeLabel);

        // Hover effect for premium feel
        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: #2F2F2F; -fx-background-radius: 8px; -fx-cursor: hand;"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: #1F1F1F; -fx-background-radius: 8px; -fx-cursor: hand;"));

        // Click effect -> trigger step 3 detail view
        card.setOnMouseClicked(e -> {
            long id = item.path("id").asLong();
            System.out.println("Tıklandı ID: " + id);
            // onTitleSelected.run(); -> will pass ID in Step 3
        });

        return card;
    }
}
