package com.library.model.helpers;

import javafx.scene.text.Text;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

public class MessageUtilTest extends ApplicationTest {

    @Test
    public void testShowMessage() {
        Text messageText = new Text();
        String message = "Test message";
        String color = "red";

        // Call the method
        MessageUtil.showMessage(messageText, message, color);

        // Verify the text and style
        assertEquals(message, messageText.getText(), "Message text should match");
        assertTrue(messageText.getStyle().contains("-fx-fill: red;"), "Style should contain correct color");

        // Wait for the timeline (3 seconds) to complete
        try {
            Thread.sleep(3500); // Slightly more than 3 seconds to ensure it clears
        } catch (InterruptedException e) {
            fail("Thread sleep interrupted");
        }

        // Verify the text is cleared
        assertEquals("", messageText.getText(), "Message text should be cleared after timeline");
    }

    @Test
    public void testShowAlert() {
        String alertTypeString = "information";
        String title = "Test Alert";
        String message = "This is a test alert message.";

        // Simulate the alert display
        MessageUtil.showAlert(alertTypeString, title, message);

        // Since Alert#showAndWait is blocking and GUI verification is not straightforward in headless tests,
        // focus on ensuring the code does not throw exceptions or fail.
        assertDoesNotThrow(() -> MessageUtil.showAlert(alertTypeString, title, message), "Alert should be displayed without exceptions");
    }

    @Test
    public void testShowAlertInvalidType() {
        String alertTypeString = "invalid";
        String title = "Test Alert";
        String message = "This is a test alert message.";

        // Test with an invalid alert type
        assertDoesNotThrow(() -> MessageUtil.showAlert(alertTypeString, title, message), "Alert with invalid type should not throw exceptions");
    }
}

