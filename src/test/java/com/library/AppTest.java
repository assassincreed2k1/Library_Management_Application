package com.library;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AppTest {

    private static Stage stage;

    @BeforeAll
    public static void setUpClass() {
        // Initialize JavaFX
        new JFXPanel(); // This initializes JavaFX
        Platform.runLater(() -> {
            try {
                App app = new App();
                stage = new Stage();
                app.start(stage); // Start the app and set the stage
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @AfterAll
    public static void tearDownClass() {
        // Clean up after all tests have run
        Platform.exit(); // Close JavaFX
    }

    @Test
    public void testSceneNotNull() {
        // Check that the scene is not null
        Platform.runLater(() -> {
            Scene scene = stage.getScene();
            assertNotNull(scene, "Scene should not be null");
        });
    }

    @Test
    public void testDatabaseConnection() {
        // Ensure that a database connection can be established
        Platform.runLater(() -> {
            Connection connection = App.getConnection(); // Assuming you implement this method
            assertNotNull(connection, "Database connection should not be null");
        });
    }

    // Additional tests can be added here
}
