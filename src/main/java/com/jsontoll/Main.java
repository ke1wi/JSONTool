package com.jsontoll;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/MainWindow.fxml"));
        Scene scene = new Scene(loader.load(), 1500, 900);
        scene.getStylesheets().add(getClass().getResource("/css/dark.css").toExternalForm());
        stage.setTitle("JSON Tool v1.0 — Java 23");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}