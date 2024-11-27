package com.energy.logging;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SystemLogger {
    private static final String LOG_FILE = "system.log";
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Ensure the log file exists
    static {
        try {
            if (!Files.exists(Paths.get(LOG_FILE))) {
                Files.createFile(Paths.get(LOG_FILE));
            }
        } catch (IOException e) {
            System.err.println("Error initializing log file: " + e.getMessage());
        }
    }
    
    public static void log(String message) {
        writeLog("LOG: " + message);
    }

    public static void logEvent(String message) {
        writeLog("EVENT: " + message);
    }

    public static void logError(String errorMessage) {
        writeLog("ERROR: " + errorMessage);
    }

    public static void logDetailed(String message, String details) {
        writeLog("DETAIL: " + message + " - " + details);
    }

    public static void logDeviceStatus(String id, String type, boolean isActive, double consumptionRate) {
        String status = String.format("Device ID: %s, Type: %s, Active: %b, Consumption Rate: %.2f W", 
                                      id, type, isActive, consumptionRate);
        writeLog("STATUS: " + status);
    }

    public static void logEnergySourceStatus(String name, double energyLevel, double maxCapacity) {
        String status = String.format("Energy Source: %s, Energy Level: %.2f kWh, Max Capacity: %.2f kWh", 
                                        name, energyLevel, maxCapacity);
        writeLog("STATUS: " + status);
    }

    private static void writeLog(String message) {
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            writer.write(timestamp + " - " + message + System.lineSeparator());
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }
}
