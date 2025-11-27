package com.jsontoll.model;

import com.fasterxml.jackson.databind.JsonNode;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class JsonSchemaModel {
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private JsonNode rootNode;
    private final StringProperty rawText = new SimpleStringProperty("");

    public void addListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void setRootNode(JsonNode node) {
        JsonNode old = rootNode;
        rootNode = node;
        pcs.firePropertyChange("rootNode", old, node);
    }

    public JsonNode getRootNode() { return rootNode; }

    public void setRawText(String text) {
        rawText.set(text);
        pcs.firePropertyChange("rawText", null, text);
    }

    public StringProperty rawTextProperty() { return rawText; }
}