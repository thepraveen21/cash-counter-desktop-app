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
        VBox mainContainer = new VBox(15);
        mainContainer.setPadding(new Insets(20));
        mainContainer.setStyle("-fx-background-color: linear-gradient(to bottom, #667eea 0%, #764ba2 100%);");

        // Title
        Label titleLabel = new Label("💰 Cash Counter");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setAlignment(Pos.CENTER);

        // Card container for denominations
        VBox cardContainer = new VBox(10);
        cardContainer.setPadding(new Insets(20));
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
        separator2.setPadding(new Insets(5, 0, 5, 0));
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

        Scene scene = new Scene(scrollPane, 500, 650);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false); // Fixed size like a calculator
        primaryStage.show();
    }

    private HBox createHeaderRow() {
        HBox headerRow = new HBox(15);
        headerRow.setAlignment(Pos.CENTER);
        headerRow.setPadding(new Insets(0, 0, 5, 0));

        Label noteLabel = new Label("Note Value");
        noteLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        noteLabel.setPrefWidth(110);

        Label quantityLabel = new Label("Quantity");
        quantityLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        quantityLabel.setPrefWidth(100);

        Label totalLabel = new Label("Total Amount");
        totalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        totalLabel.setPrefWidth(140);

        headerRow.getChildren().addAll(noteLabel, quantityLabel, totalLabel);
        return headerRow;
    }

    private HBox createDenominationRow(int index) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(8));
        row.setStyle(
                "-fx-background-color: #f8f9fa;" +
                        "-fx-background-radius: 8;"
        );

        // Note label with currency symbol
        Label noteLabel = new Label("Rs. " + denominations[index]);
        noteLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        noteLabel.setPrefWidth(110);
        noteLabel.setStyle("-fx-text-fill: #2c3e50;");

        // Quantity field
        TextField quantityField = new TextField("");
        quantityField.setPrefWidth(100);
        quantityField.setFont(Font.font("Arial", 14));
        quantityField.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #dfe6e9;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;"
        );

        // Add tooltip
        Tooltip tooltip = new Tooltip("Maximum: 99999");
        tooltip.setStyle("-fx-font-size: 11px;");
        quantityField.setTooltip(tooltip);

        // Only allow numbers and limit to 5 digits (max 99999)
        quantityField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Remove non-numeric characters
            if (!newValue.matches("\\d*")) {
                newValue = newValue.replaceAll("[^\\d]", "");
            }

            // Limit to 5 digits and max value of 99999
            if (newValue.length() > 5) {
                newValue = newValue.substring(0, 5);
            }

            // Check if value exceeds 99999
            if (!newValue.isEmpty()) {
                try {
                    int value = Integer.parseInt(newValue);
                    if (value > 99999) {
                        newValue = "99999";
                    }
                } catch (NumberFormatException e) {
                    newValue = "";
                }
            }

            // Update the field only if value changed
            final String finalValue = newValue;
            if (!finalValue.equals(quantityField.getText())) {
                quantityField.setText(finalValue);
                return; // Prevent recursive call
            }

            calculateTotal(index);
            calculateGrandTotal();
        });

        quantityFields[index] = quantityField;

        // Total label
        Label totalLabel = new Label("Rs. 0");
        totalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        totalLabel.setPrefWidth(140);
        totalLabel.setStyle("-fx-text-fill: #27ae60;");
        totalLabels[index] = totalLabel;

        row.getChildren().addAll(noteLabel, quantityField, totalLabel);
        return row;
    }

    private HBox createGrandTotalRow() {
        HBox grandTotalRow = new HBox(15);
        grandTotalRow.setAlignment(Pos.CENTER_RIGHT);
        grandTotalRow.setPadding(new Insets(12, 10, 12, 10));
        grandTotalRow.setStyle(
                "-fx-background-color: #667eea;" +
                        "-fx-background-radius: 8;"
        );

        Label label = new Label("GRAND TOTAL:");
        label.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        label.setTextFill(Color.WHITE);

        grandTotalLabel = new Label("Rs. 0");
        grandTotalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        grandTotalLabel.setTextFill(Color.web("#ffeaa7"));

        grandTotalRow.getChildren().addAll(label, grandTotalLabel);
        return grandTotalRow;
    }

    private HBox createButtonBox() {
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(15, 0, 0, 0));

        Button clearButton = new Button("Clear All");
        clearButton.setPrefWidth(120);
        clearButton.setPrefHeight(35);
        clearButton.setFont(Font.font("Arial", FontWeight.BOLD, 12));
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
        printButton.setPrefWidth(120);
        printButton.setPrefHeight(35);
        printButton.setFont(Font.font("Arial", FontWeight.BOLD, 12));
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
            totalLabels[index].setText("Rs. 0");
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
            field.setText("");
        }
        for (Label label : totalLabels) {
            label.setText("Rs. 0");
        }
        grandTotalLabel.setText("Rs. 0");
    }

    private void printSummary() {
        StringBuilder summary = new StringBuilder();

        summary.append("===== CASH COUNTER SUMMARY =====\n\n");

        summary.append(String.format(
                "%-10s %5s %18s%n",
                "NOTE", "QTY", "TOTAL"
        ));
        summary.append("---------------------------------------------\n");

        int grandTotal = 0;

        for (int i = 0; i < denominations.length; i++) {
            String qtyText = quantityFields[i].getText();
            int qty = qtyText.isEmpty() ? 0 : Integer.parseInt(qtyText);

            if (qty > 0) {
                int total = denominations[i] * qty;
                grandTotal += total;

                // 🔑 RIGHT-ALIGNED NUMBERS (IMPORTANT PART)
                summary.append(String.format(
                        "Rs. %10s × %5s = Rs. %18s%n",
                        String.format("%,d", denominations[i]),
                        String.format("%d", qty),
                        String.format("%,d", total)
                ));
            }
        }

        summary.append("---------------------------------------------\n");

        summary.append(String.format(
                "%-18s Rs. %18s%n",
                "GRAND TOTAL",
                String.format("%,d", grandTotal)
        ));

        summary.append("=============================================");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Summary");
        alert.setHeaderText("Cash Counter Summary");
        alert.setContentText(summary.toString());

        // ⭐ REQUIRED for PERFECT ALIGNMENT
        alert.getDialogPane().setStyle(
                "-fx-font-family: 'Consolas'; -fx-font-size: 12px;"
        );

        alert.showAndWait();
    }



    public static void main(String[] args) {
        launch(args);
    }
}