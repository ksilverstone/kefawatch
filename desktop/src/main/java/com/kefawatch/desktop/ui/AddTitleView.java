package com.kefawatch.desktop.ui;
import com.kefawatch.desktop.api.ApiClient;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;

public class AddTitleView extends StackPane {
    public AddTitleView(Runnable onSuccess, Runnable onBack) {
        setStyle("-fx-background-image: url('https://image.tmdb.org/t/p/original/8ZTVqvKDQ8emSGUEMjsS4yHAwrp.jpg'); -fx-background-size: cover;");

        VBox overlay = new VBox();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.8);");

        VBox form = new VBox();
        form.setStyle("-fx-background-color: #141414; -fx-padding: 40; -fx-background-radius: 8;");
        form.setAlignment(Pos.CENTER);
        form.setSpacing(15);
        form.setMaxWidth(500);

        Label title = new Label("Yeni Film/Dizi Ekle");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white; -fx-alignment: center-left; -fx-pref-width: 400px;");

        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("MOVIE", "SERIES");
        typeBox.setValue("MOVIE");
        typeBox.setStyle("-fx-background-color: #333; -fx-text-fill: white; -fx-font-size: 14px;");
        typeBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: white;");
                }
            }
        });
        typeBox.setMaxWidth(400);

        TextField nameField = createStyledTextField("Başlık (Örn: The Matrix)");
        TextArea descField = new TextArea(); 
        descField.setPromptText("Açıklama"); 
        descField.setMaxWidth(400); 
        descField.setMaxHeight(100); 
        descField.setStyle("-fx-control-inner-background: #333; -fx-text-fill: white; -fx-prompt-text-fill: #aaa; -fx-font-size: 14px;");
        
        TextField posterField = createStyledTextField("Afiş URL (Resim linki)");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #E50914; -fx-font-weight: bold; -fx-font-size: 14px;");

        Button submitBtn = new Button("Kataloğa Ekle");
        submitBtn.setStyle("-fx-background-color: #E50914; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12 20; -fx-font-size: 16px; -fx-cursor: hand; -fx-background-radius: 4;");
        submitBtn.setMaxWidth(400);
        submitBtn.setOnAction(e -> {
            String err = ApiClient.createTitle(typeBox.getValue(), nameField.getText(), descField.getText(), posterField.getText(), null, null);
            if (err == null) {
                onSuccess.run();
            } else {
                errorLabel.setText(err);
            }
        });

        Button backBtn = new Button("İptal Et ve Geri Dön");
        backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #aaa; -fx-font-size: 14px; -fx-cursor: hand;");
        backBtn.setOnAction(e -> onBack.run());

        form.getChildren().addAll(title, typeBox, nameField, descField, posterField, submitBtn, errorLabel, backBtn);
        getChildren().addAll(overlay, form);
    }
    
    private TextField createStyledTextField(String prompt) {
        TextField f = new TextField();
        f.setPromptText(prompt);
        f.setMaxWidth(400);
        f.setStyle("-fx-background-color: #333; -fx-text-fill: white; -fx-prompt-text-fill: #aaa; -fx-padding: 15; -fx-background-radius: 4; -fx-font-size: 14px;");
        return f;
    }
}
