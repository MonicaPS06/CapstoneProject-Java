package test.java.com.energy;

import com.energy.logging.SystemLogger;
import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

class SystemLoggerTest {

    private static final String LOG_FILE = "system.log";

    @BeforeEach
    void setUp() throws IOException {
        // Ensure the log file is clean before each test
        Files.deleteIfExists(Paths.get(LOG_FILE));
        Files.createFile(Paths.get(LOG_FILE));
    }

    @AfterEach
    void tearDown() throws IOException {
        // Clean up the log file after each test
        Files.deleteIfExists(Paths.get(LOG_FILE));
    }

    @Test
    void testLogFileCreation() {
        // Verify that the log file is created during initialization
        assertTrue(Files.exists(Paths.get(LOG_FILE)), "Log file should exist after initialization");
    }

    @Test
    void testLogMessage() throws IOException {
        // Log a message and verify it is written to the file
        SystemLogger.log("Test log message");
        String logContent = Files.readString(Paths.get(LOG_FILE));
        assertTrue(logContent.contains("LOG: Test log message"), "Log file should contain the logged message");
    }

    @Test
    void testLogEvent() throws IOException {
        // Log an event and verify it is written to the file
        SystemLogger.logEvent("Test event message");
        String logContent = Files.readString(Paths.get(LOG_FILE));
        assertTrue(logContent.contains("EVENT: Test event message"), "Log file should contain the event message");
    }

    @Test
    void testLogError() throws IOException {
        // Log an error and verify it is written to the file
        SystemLogger.logError("Test error message");
        String logContent = Files.readString(Paths.get(LOG_FILE));
        assertTrue(logContent.contains("ERROR: Test error message"), "Log file should contain the error message");
    }


    @Test
    void testLogDeviceStatus() throws IOException {
        // Log a device status and verify it is written to the file
        SystemLogger.logDeviceStatus("Device1", "Light", true, 10.5);
        String logContent = Files.readString(Paths.get(LOG_FILE));
        assertTrue(logContent.contains("STATUS: Device ID: Device1, Type: Light, Active: true, Consumption Rate: 10.50 W"),
                "Log file should contain the device status");
    }
}
