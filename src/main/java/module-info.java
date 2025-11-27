module com.jsontoll {

    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;

    requires java.desktop;

    requires json.schema.validator; // ← ВАЖНО!
    requires org.fxmisc.richtext; // RichTextFX
    requires reactfx; // RichTextFX dependency

    opens com.jsontoll to javafx.fxml;
    opens com.jsontoll.ui to javafx.fxml;

    exports com.jsontoll;
    exports com.jsontoll.ui;
}