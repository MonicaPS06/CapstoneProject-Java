package com.energy.main;

import com.energy.ui.EnergySourceUI;
import com.energy.logging.SystemLogger;
import com.energy.ui.EnergyManagementDashboardUI;

public class Main {
    public static void main(String[] args) {
        try {
            // Initialize UI components
            EnergySourceUI energySourceUI = new EnergySourceUI();
            EnergyManagementDashboardUI dashboard = new EnergyManagementDashboardUI(energySourceUI);

            // Log system start
            SystemLogger.logEvent("System started.");

            // Display dashboardUI as the main entry point
            dashboard.setVisible(true);

        } catch (NullPointerException e) {
            SystemLogger.logError("A critical object was not initialized: " + e.getMessage());
            e.printStackTrace();

        } catch (RuntimeException e) {
            SystemLogger.logError("Runtime error during startup: " + e.getMessage());
            e.printStackTrace();

        } catch (Exception e) {
            // Catch-all for any other unexpected exceptions
            SystemLogger.logError("An unexpected error occurred during startup: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Ensure the system logger logs shutdown events
            SystemLogger.logEvent("System shutting down.");
        }
    }

}
