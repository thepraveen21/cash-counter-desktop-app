package com.example.cashcounterapp2;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class CashCounterApp extends Application {

    // Denomination values
    private final int[] denominations = {5000, 2000, 1000, 500, 100, 50, 20};

    // Arrays to hold UI components
    private TextField[] quantityFields = new TextField[denominations.length];
    private Label[] totalLabels = new Label[denominations.length];
    private Label grandTotalLabel;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Cash Counter Application");

        // Main container
        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(30));
        mainContainer.setStyle("-fx-background-color: linear-gradient(to bottom, #667eea 0%, #764ba2 100%);");

        // Title
        Label titleLabel = new Label("💰 Cash Counter");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setAlignment(Pos.CENTER);

        // Card container for denominations
        VBox cardContainer = new VBox(15);
        cardContainer.setPadding(new Insets(30));
        cardContainer.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 15;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 20, 0, 0, 5);"
        );

        // Header row
        HBox headerRow = createHeaderRow();
        cardContainer.getChildren().add(headerRow);

        // Separator
        Separator separator = new Separator();
        cardContainer.getChildren().add(separator);

        // Create rows for each denomination
        for (int i = 0; i < denominations.length; i++) {
            HBox row = createDenominationRow(i);
            cardContainer.getChildren().add(row);
        }

        // Another separator
        Separator separator2 = new Separator();
        separator2.setPadding(new Insets(10, 0, 10, 0));
        cardContainer.getChildren().add(separator2);

        // Grand total row
        HBox grandTotalRow = createGrandTotalRow();
        cardContainer.getChildren().add(grandTotalRow);

        // Buttons
        HBox buttonBox = createButtonBox();
        cardContainer.getChildren().add(buttonBox);

        // Add all to main container
        mainContainer.getChildren().addAll(titleLabel, cardContainer);
        mainContainer.setAlignment(Pos.TOP_CENTER);

        // Create scene
        ScrollPane scrollPane = new ScrollPane(mainContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: linear-gradient(to bottom, #667eea 0%, #764ba2 100%);");

        Scene scene = new Scene(scrollPane, 700, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createHeaderRow() {
        HBox headerRow = new HBox(20);
        headerRow.setAlignment(Pos.CENTER);
        headerRow.setPadding(new Insets(0, 0, 10, 0));

        Label noteLabel = new Label("Note Value");
        noteLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        noteLabel.setPrefWidth(150);

        Label quantityLabel = new Label("Quantity");
        quantityLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        quantityLabel.setPrefWidth(150);

        Label totalLabel = new Label("Total Amount");
        totalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        totalLabel.setPrefWidth(180);

        headerRow.getChildren().addAll(noteLabel, quantityLabel, totalLabel);
        return headerRow;
    }

    private HBox createDenominationRow(int index) {
        HBox row = new HBox(20);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(10));
        row.setStyle(
                "-fx-background-color: #f8f9fa;" +
                        "-fx-background-radius: 8;"
        );

        // Note label with currency symbol
        Label noteLabel = new Label("Rs." + denominations[index]);
        noteLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        noteLabel.setPrefWidth(150);
        noteLabel.setStyle("-fx-text-fill: #2c3e50;");

        // Quantity field
        TextField quantityField = new TextField("0");
        quantityField.setPrefWidth(150);
        quantityField.setFont(Font.font("Arial", 16));
        quantityField.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #dfe6e9;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;"
        );

        // Only allow numbers
        quantityField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                quantityField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            calculateTotal(index);
            calculateGrandTotal();
        });

        quantityFields[index] = quantityField;

        // Total label
        Label totalLabel = new Label("Rs.0");
        totalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        totalLabel.setPrefWidth(180);
        totalLabel.setStyle("-fx-text-fill: #27ae60;");
        totalLabels[index] = totalLabel;

        row.getChildren().addAll(noteLabel, quantityField, totalLabel);
        return row;
    }

    private HBox createGrandTotalRow() {
        HBox grandTotalRow = new HBox(20);
        grandTotalRow.setAlignment(Pos.CENTER_RIGHT);
        grandTotalRow.setPadding(new Insets(20, 10, 10, 10));
        grandTotalRow.setStyle(
                "-fx-background-color: #667eea;" +
                        "-fx-background-radius: 8;"
        );

        Label label = new Label("GRAND TOTAL:");
        label.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        label.setTextFill(Color.WHITE);

        grandTotalLabel = new Label("Rs.0");
        grandTotalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        grandTotalLabel.setTextFill(Color.web("#ffeaa7"));

        grandTotalRow.getChildren().addAll(label, grandTotalLabel);
        return grandTotalRow;
    }

    private HBox createButtonBox() {
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));

        Button clearButton = new Button("Clear All");
        clearButton.setPrefWidth(150);
        clearButton.setPrefHeight(40);
        clearButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        clearButton.setStyle(
                "-fx-background-color: #e74c3c;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 20;" +
                        "-fx-cursor: hand;"
        );

        clearButton.setOnMouseEntered(e ->
                clearButton.setStyle(
                        "-fx-background-color: #c0392b;" +
                                "-fx-text-fill: white;" +
                                "-fx-background-radius: 20;" +
                                "-fx-cursor: hand;"
                )
        );

        clearButton.setOnMouseExited(e ->
                clearButton.setStyle(
                        "-fx-background-color: #e74c3c;" +
                                "-fx-text-fill: white;" +
                                "-fx-background-radius: 20;" +
                                "-fx-cursor: hand;"
                )
        );

        clearButton.setOnAction(e -> clearAll());

        Button printButton = new Button("Print Summary");
        printButton.setPrefWidth(150);
        printButton.setPrefHeight(40);
        printButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        printButton.setStyle(
                "-fx-background-color: #3498db;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 20;" +
                        "-fx-cursor: hand;"
        );

        printButton.setOnMouseEntered(e ->
                printButton.setStyle(
                        "-fx-background-color: #2980b9;" +
                                "-fx-text-fill: white;" +
                                "-fx-background-radius: 20;" +
                                "-fx-cursor: hand;"
                )
        );

        printButton.setOnMouseExited(e ->
                printButton.setStyle(
                        "-fx-background-color: #3498db;" +
                                "-fx-text-fill: white;" +
                                "-fx-background-radius: 20;" +
                                "-fx-cursor: hand;"
                )
        );

        printButton.setOnAction(e -> printSummary());

        buttonBox.getChildren().addAll(clearButton, printButton);
        return buttonBox;
    }

    private void calculateTotal(int index) {
        try {
            String quantityText = quantityFields[index].getText();
            int quantity = quantityText.isEmpty() ? 0 : Integer.parseInt(quantityText);
            int total = denominations[index] * quantity;
            totalLabels[index].setText("Rs. " + String.format("%,d", total));
        } catch (NumberFormatException e) {
            totalLabels[index].setText("Rs.0");
        }
    }

    private void calculateGrandTotal() {
        int grandTotal = 0;
        for (int i = 0; i < denominations.length; i++) {
            try {
                String quantityText = quantityFields[i].getText();
                int quantity = quantityText.isEmpty() ? 0 : Integer.parseInt(quantityText);
                grandTotal += denominations[i] * quantity;
            } catch (NumberFormatException e) {
                // Skip invalid entries
            }
        }
        grandTotalLabel.setText("Rs. " + String.format("%,d", grandTotal));
    }

    private void clearAll() {
        for (TextField field : quantityFields) {
            field.setText("0");
        }
        for (Label label : totalLabels) {
            label.setText("Rs.0");
        }
        grandTotalLabel.setText("Rs.0");
    }

    private void printSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("===== CASH COUNTER SUMMARY =====\n\n");

        for (int i = 0; i < denominations.length; i++) {
            String quantityText = quantityFields[i].getText();
            int quantity = quantityText.isEmpty() ? 0 : Integer.parseInt(quantityText);
            if (quantity > 0) {
                int total = denominations[i] * quantity;
                summary.append(String.format("Rs. %,4d × %3d = Rs. %,10d\n",
                        denominations[i], quantity, total));
            }
        }

        summary.append("\n================================\n");
        summary.append(String.format("GRAND TOTAL: %s\n", grandTotalLabel.getText()));
        summary.append("================================");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Summary");
        alert.setHeaderText("Cash Counter Summary");
        alert.setContentText(summary.toString());
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}