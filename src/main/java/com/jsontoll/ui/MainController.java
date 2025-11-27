package com.jsontoll.ui;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsontoll.model.JsonSchemaModel;
import com.jsontoll.model.PropertyRow;
import com.jsontoll.validation.Draft202012Strategy;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.io.File;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainController {

    @FXML private CodeArea codeArea;
    @FXML private TableView<PropertyRow> metadataTable;
    @FXML private Label statusLabel;

    private final JsonSchemaModel model = new JsonSchemaModel();
    private final ObjectMapper mapper = new ObjectMapper();
    private final FileChooser fileChooser = new FileChooser();

    private static final Pattern PATTERN = Pattern.compile(
            "(?<STRING>\\\"(?:\\\\.|[^\\\\\"])*\\\")|" +
                    "(?<KEYWORD>\\b(type|properties|required|description|example|format|pattern|title|enum|const|default|items|oneOf|anyOf|allOf)\\b)|" +
                    "(?<NUMBER>-?\\d+(\\.\\d+)?([eE][+-]?\\d+)?)|" +
                    "(?<BOOLEAN>true|false|null)|" +
                    "(?<BRACKET>[{}\\[\\]:,])|" +
                    "(?<COMMENT>//.*|/\\*[\\s\\S]*?\\*/)"
    );

    @FXML
    private void initialize() {
        setupCodeArea();
        model.addListener(evt -> Platform.runLater(this::refreshAll));
        refreshAll(); // ← обов’язково!
        setupAutoSave();
    }

    private void setupCodeArea() {
        codeArea.textProperty().addListener((obs, old, text) -> {
            model.setRawText(text);
            codeArea.setStyleSpans(0, computeHighlighting(text));
        });

        // Початковий шаблон
        codeArea.replaceText("{\n  \"type\": \"object\",\n  \"properties\": {}\n}");
    }

    private StyleSpans<Collection<String>> computeHighlighting(String text) {
        if (text == null || text.isEmpty()) {
            StyleSpansBuilder<Collection<String>> b = new StyleSpansBuilder<>();
            b.add(Collections.emptyList(), 0);
            return b.create();
        }

        Matcher m = PATTERN.matcher(text);
        int last = 0;
        StyleSpansBuilder<Collection<String>> spans = new StyleSpansBuilder<>();

        while (m.find()) {
            if (m.start() > last) spans.add(Collections.emptyList(), m.start() - last);
            String style = m.group("STRING")   != null ? "string" :
                    m.group("KEYWORD")  != null ? "keyword" :
                            m.group("NUMBER")   != null ? "number" :
                                    m.group("BOOLEAN")  != null ? "boolean" :
                                            m.group("BRACKET")  != null ? "bracket" :
                                                    m.group("COMMENT")  != null ? "comment" : "plain";
            spans.add(Collections.singleton(style), m.end() - m.start());
            last = m.end();
        }
        if (last < text.length()) spans.add(Collections.emptyList(), text.length() - last);
        if (last == 0) spans.add(Collections.emptyList(), text.length());
        return spans.create();
    }

    private void refreshAll() {
        try {
            JsonNode node = mapper.readTree(model.rawTextProperty().get());
            model.setRootNode(node);
            buildMetadataTable(node);
            statusLabel.setText("Схема валідна");
        } catch (Exception e) {
            statusLabel.setText("Помилка парсингу JSON");
            metadataTable.setItems(FXCollections.observableArrayList());
        }
    }

    private void buildMetadataTable(JsonNode node) {
        var list = FXCollections.<PropertyRow>observableArrayList();

        JsonNode props = node.at("/properties");
        if (props.isObject()) {
            props.fields().forEachRemaining(entry -> {
                String name = entry.getKey();
                JsonNode p = entry.getValue();
                String type = p.has("type") ? p.get("type").asText() : "?";
                String desc = p.has("description") ? p.get("description").asText("") : "";
                String format = p.has("format") ? p.get("format").asText("") : "";
                list.add(new PropertyRow(name, type, desc, format, ""));
            });
        }
        metadataTable.setItems(list); // ← ЦЕ ГОЛОВНЕ!
    }

    @FXML private void openSchema() {
        File file = fileChooser.showOpenDialog(codeArea.getScene().getWindow());
        if (file != null) {
            try {
                String content = Files.readString(file.toPath());
                codeArea.replaceText(content);
                statusLabel.setText("Завантажено: " + file.getName());
            } catch (Exception ex) {
                statusLabel.setText("Помилка відкриття");
            }
        }
    }

    @FXML private void validateInstance() {
        File file = fileChooser.showOpenDialog(codeArea.getScene().getWindow());
        if (file == null) return;
        try {
            JsonNode instance = mapper.readTree(file);
            var report = Draft202012Strategy.validate(model.getRootNode(), instance);
            statusLabel.setText(report.valid ? "JSON валідний!" : "Помилок: " + report.messages.size());
        } catch (Exception ex) {
            statusLabel.setText("Помилка читання JSON");
        }
    }

    @FXML private void closeApplication() {
        Platform.exit();
    }

    private void setupAutoSave() {
        new Thread(() -> {
            while (true) {
                try { Thread.sleep(12000); } catch (InterruptedException ignored) {}
                Platform.runLater(() -> {
                    try {
                        Files.writeString(
                                java.nio.file.Path.of(System.getProperty("user.home"), ".jsontoll_autosave.json"),
                                codeArea.getText()
                        );
                    } catch (Exception ignored) {}
                });
            }
        }).start();
    }
}