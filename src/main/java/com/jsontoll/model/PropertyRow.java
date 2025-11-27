package com.jsontoll.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PropertyRow {
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty type = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final StringProperty format = new SimpleStringProperty();
    private final StringProperty example = new SimpleStringProperty();

    public PropertyRow(String name, String type, String description, String format, String example) {
        this.name.set(name);
        this.type.set(type);
        this.description.set(description);
        this.format.set(format);
        this.example.set(example != null ? example : "");
    }

    // Обов’язково — гетери з назвою getXXX() + Property()
    public StringProperty nameProperty() { return name; }
    public StringProperty typeProperty() { return type; }
    public StringProperty descriptionProperty() { return description; }
    public StringProperty formatProperty() { return format; }
    public StringProperty exampleProperty() { return example; }

    // Гетери для рефлексії
    public String getName() { return name.get(); }
    public String getType() { return type.get(); }
    public String getDescription() { return description.get(); }
    public String getFormat() { return format.get(); }
    public String getExample() { return example.get(); }
}