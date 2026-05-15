package com.kefawatch.desktop.ui;

import com.fasterxml.jackson.databind.JsonNode;
import com.kefawatch.desktop.api.ApiClient;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

public class MainView extends VBox {

    private final java.util.function.Consumer<Long> onTitleSelected;
    private final Runnable onAddTitleSelected;
    private final Runnable onLogout;
    
    private FlowPane catalogPane;
    private List<JsonNode> allTitles = new ArrayList<>();
    private String currentFilter = "ALL"; 

    public MainView(java.util.function.Consumer<Long> onTitleSelected, Runnable onAddTitleSelected, Runnable onLogout) {
        this.onTitleSelected = onTitleSelected;
        this.onAddTitleSelected = onAddTitleSelected;
        this.onLogout = onLogout;

        setStyle("-fx-background-color: #141414;");

        // --- NAVBAR ---
        HBox navBar = new HBox(20);
        navBar.setAlignment(Pos.CENTER_LEFT);
        navBar.setPadding(new Insets(15, 40, 15, 40));
        navBar.setStyle("-fx-background-color: linear-gradient(to bottom, rgba(0,0,0,0.9), rgba(0,0,0,0));");

        Label logo = new Label("KEFAWATCH");
        logo.setStyle("-fx-text-fill: #E50914; -fx-font-size: 28px; -fx-font-weight: bold; -fx-cursor: hand;");
        logo.setOnMouseClicked(e -> applyFilter("ALL"));

        Button btnHome = createNavBtn("Ana Sayfa");
        btnHome.setOnAction(e -> applyFilter("ALL"));

        Button btnSeries = createNavBtn("Diziler");
        btnSeries.setOnAction(e -> applyFilter("SERIES"));

        Button btnMovies = createNavBtn("Filmler");
        btnMovies.setOnAction(e -> applyFilter("MOVIE"));

        Button btnWatchlist = createNavBtn("Listem");
        btnWatchlist.setOnAction(e -> applyFilter("WATCHLIST"));

        Button btnWatched = createNavBtn("İzlediklerim");
        btnWatched.setOnAction(e -> applyFilter("WATCHED"));
        
        Button addBtn = new Button("+ Yeni İçerik");
        addBtn.setStyle("-fx-background-color: rgba(255,255,255,0.1); -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-background-radius: 5; -fx-padding: 8 15;");
        addBtn.setOnAction(e -> onAddTitleSelected.run());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        TextField searchField = new TextField();
        searchField.setPromptText("Film, dizi, tür ara...");
        searchField.setPrefWidth(250);
        searchField.setStyle("-fx-background-color: rgba(0,0,0,0.6); -fx-text-fill: white; -fx-border-color: #555; -fx-border-width: 1; -fx-border-radius: 20; -fx-padding: 8 15; -fx-font-size: 14px;");
        searchField.textProperty().addListener((obs, oldV, newV) -> performSearch(newV));

        // Modern User Profile Avatar
        MenuButton userMenu = new MenuButton();
        userMenu.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        
        Circle avatarClip = new Circle(18);
        ImageView avatarView = new ImageView(new Image("https://upload.wikimedia.org/wikipedia/commons/0/0b/Netflix-avatar.png", true));
        avatarView.setFitWidth(36);
        avatarView.setFitHeight(36);
        avatarView.setClip(avatarClip);
        userMenu.setGraphic(avatarView);
        
        MenuItem logoutItem = new MenuItem("Çıkış Yap");
        logoutItem.setStyle("-fx-font-weight: bold; -fx-text-fill: black;");
        logoutItem.setOnAction(e -> onLogout.run());
        userMenu.getItems().add(logoutItem);

        navBar.getChildren().addAll(logo, btnHome, btnSeries, btnMovies, btnWatchlist, btnWatched, addBtn, spacer, searchField, userMenu);

        // --- HERO BANNER ---
        VBox heroBanner = new VBox(15);
        heroBanner.setAlignment(Pos.BOTTOM_LEFT);
        heroBanner.setPadding(new Insets(50, 50, 50, 50));
        heroBanner.setPrefHeight(500);
        heroBanner.setStyle("-fx-background-image: url('https://image.tmdb.org/t/p/original/8ZTVqvKDQ8emSGUEMjsS4yHAwrp.jpg'); -fx-background-size: cover; -fx-background-position: center top;");

        VBox heroGradient = new VBox(15);
        heroGradient.setAlignment(Pos.BOTTOM_LEFT);
        heroGradient.setPadding(new Insets(50, 50, 50, 50));
        heroGradient.setPrefHeight(500);
        heroGradient.setStyle("-fx-background-color: linear-gradient(to top, #141414 10%, transparent 60%);");

        Label heroTitle = new Label("INCEPTION");
        heroTitle.setStyle("-fx-text-fill: white; -fx-font-size: 72px; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.9), 15, 0, 0, 0);");
        
        Label heroDesc = new Label("Rüyalar içinde rüyalar. Bir hırsız, kurbanının bilinçaltına girerek\nbir fikri yerleştirmekle görevlendirilir.");
        heroDesc.setStyle("-fx-text-fill: white; -fx-font-size: 22px; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.9), 10, 0, 0, 0);");

        HBox heroButtons = new HBox(15);
        Button watchedBtn = new Button("✔ İzlendi");
        watchedBtn.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 18px; -fx-padding: 12 40; -fx-background-radius: 4; -fx-cursor: hand;");
        
        Button watchlistBtn = new Button("➕ Listeme Ekle");
        watchlistBtn.setStyle("-fx-background-color: rgba(109, 109, 110, 0.7); -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18px; -fx-padding: 12 40; -fx-background-radius: 4; -fx-cursor: hand;");

        Label heroStatusLabel = new Label();
        heroStatusLabel.setStyle("-fx-text-fill: #4CAF50; -fx-font-size: 16px; -fx-font-weight: bold;");

        watchedBtn.setOnAction(e -> {
            long inceptionId = findTitleId("Inception");
            if (inceptionId != -1) {
                if (ApiClient.markAsWatched(inceptionId)) {
                    heroStatusLabel.setText("İzlendi olarak işaretlendi.");
                } else {
                    heroStatusLabel.setText("Kayıt başarısız.");
                    heroStatusLabel.setStyle("-fx-text-fill: #E50914; -fx-font-size: 16px; -fx-font-weight: bold;");
                }
            }
        });

        watchlistBtn.setOnAction(e -> {
            long inceptionId = findTitleId("Inception");
            if (inceptionId != -1) {
                if (ApiClient.addToWatchlist(inceptionId)) {
                    heroStatusLabel.setText("Listeye eklendi.");
                } else {
                    heroStatusLabel.setText("Ekleme başarısız.");
                    heroStatusLabel.setStyle("-fx-text-fill: #E50914; -fx-font-size: 16px; -fx-font-weight: bold;");
                }
            }
        });

        heroButtons.getChildren().addAll(watchedBtn, watchlistBtn, heroStatusLabel);
        heroButtons.setAlignment(Pos.CENTER_LEFT);
        heroGradient.getChildren().addAll(heroTitle, heroDesc, heroButtons);
        
        StackPane heroStack = new StackPane(heroBanner, heroGradient);

        // --- CATALOG PANE ---
        catalogPane = new FlowPane();
        catalogPane.setHgap(20);
        catalogPane.setVgap(30);
        catalogPane.setAlignment(Pos.TOP_LEFT);
        catalogPane.setPadding(new Insets(20, 50, 50, 50));

        ScrollPane scrollPane = new ScrollPane();
        VBox scrollContent = new VBox();
        scrollContent.getChildren().addAll(heroStack, catalogPane);
        scrollContent.setStyle("-fx-background-color: #141414;");
        
        scrollPane.setContent(scrollContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #141414; -fx-border-color: transparent;");

        getChildren().addAll(navBar, scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        loadTitlesFromServer();
    }

    private Button createNavBtn(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #e5e5e5; -fx-font-size: 16px; -fx-font-weight: bold; -fx-cursor: hand;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #b3b3b3; -fx-font-size: 16px; -fx-font-weight: bold; -fx-cursor: hand;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #e5e5e5; -fx-font-size: 16px; -fx-font-weight: bold; -fx-cursor: hand;"));
        return btn;
    }

    private void loadTitlesFromServer() {
        JsonNode items = ApiClient.getTitles();
        if (items != null && items.isArray()) {
            allTitles.clear();
            items.forEach(allTitles::add);
        }
        applyFilter(currentFilter);
    }

    private long findTitleId(String name) {
        for (JsonNode item : allTitles) {
            if (item.path("name").asText("").equalsIgnoreCase(name)) {
                return item.path("id").asLong();
            }
        }
        return -1;
    }

    private void applyFilter(String type) {
        this.currentFilter = type;
        renderCatalog(null);
    }
    
    private void performSearch(String query) {
        renderCatalog(query);
    }

    private void renderCatalog(String query) {
        catalogPane.getChildren().clear();
        boolean found = false;

        List<Long> watchListIds = new ArrayList<>();
        List<Long> watchedIds = new ArrayList<>();

        if (currentFilter.equals("WATCHLIST")) {
            watchListIds = ApiClient.getWatchlist();
        } else if (currentFilter.equals("WATCHED")) {
            watchedIds = ApiClient.getWatched();
        }

        for (JsonNode item : allTitles) {
            String itemType = item.path("type").asText("");
            String itemName = item.path("name").asText("").toLowerCase();
            long itemId = item.path("id").asLong();

            if (currentFilter.equals("MOVIE") && !itemType.equalsIgnoreCase("MOVIE")) continue;
            if (currentFilter.equals("SERIES") && !itemType.equalsIgnoreCase("SERIES")) continue;
            if (currentFilter.equals("WATCHLIST") && !watchListIds.contains(itemId)) continue;
            if (currentFilter.equals("WATCHED") && !watchedIds.contains(itemId)) continue;

            if (query != null && !query.isEmpty() && !itemName.contains(query.toLowerCase())) {
                continue;
            }

            catalogPane.getChildren().add(createTitleCard(item));
            found = true;
        }

        if (!found) {
            Label errorLabel = new Label("Bu kategori veya aramada içerik bulunamadı.");
            errorLabel.setStyle("-fx-text-fill: #aaa; -fx-font-size: 18px;");
            catalogPane.getChildren().add(errorLabel);
        }
    }

    private VBox createTitleCard(JsonNode item) {
        VBox card = new VBox(8);
        card.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        card.setPrefWidth(220);
        card.setAlignment(Pos.TOP_LEFT);

        String posterUrl = item.path("posterUrl").asText("");
        ImageView imageView = new ImageView();
        imageView.setFitWidth(220);
        imageView.setFitHeight(330);
        imageView.setPreserveRatio(false);

        if (!posterUrl.isEmpty() && !posterUrl.equals("null")) {
            try {
                Image img = new Image(posterUrl, true);
                imageView.setImage(img);
            } catch (Exception e) {}
        } else {
            imageView.setStyle("-fx-background-color: #333;"); 
        }

        Label nameLabel = new Label(item.path("name").asText("İsimsiz"));
        nameLabel.setStyle("-fx-text-fill: #e5e5e5; -fx-font-size: 16px; -fx-font-weight: bold;");
        nameLabel.setMaxWidth(220);
        
        card.getChildren().addAll(imageView, nameLabel);

        card.setOnMouseEntered(e -> {
            imageView.setScaleX(1.05);
            imageView.setScaleY(1.05);
            nameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        });
        card.setOnMouseExited(e -> {
            imageView.setScaleX(1.0);
            imageView.setScaleY(1.0);
            nameLabel.setStyle("-fx-text-fill: #e5e5e5; -fx-font-size: 16px; -fx-font-weight: bold;");
        });

        card.setOnMouseClicked(e -> {
            long id = item.path("id").asLong();
            onTitleSelected.accept(id);
        });

        return card;
    }
}
