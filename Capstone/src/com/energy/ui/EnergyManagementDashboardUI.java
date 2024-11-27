package com.energy.ui;

import javax.swing.*;
import java.awt.*;

public class EnergyManagementDashboardUI extends JFrame {

    private EnergySourceUI energySourceUI; // Reference to EnergySourceUI instance

    // Constructor accepting an EnergySourceUI parameter
    public EnergyManagementDashboardUI(EnergySourceUI energySourceUI) {
    	if (energySourceUI == null) {
            throw new RuntimeException("EnergySourceUI dependency cannot be null");
        }
        this.energySourceUI = energySourceUI; // Assign the instance

        try {
        // Set up the JFrame
        setTitle("Smart Home: Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Add a title label
        JLabel titleLabel = new JLabel("Energy Management Dashboard", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Create a panel for the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 2, 10, 10));

        // Add buttons for different functionalities
        JButton systemSummaryButton = new JButton("Show System Summary");
        JButton manageSmartObjectsButton = new JButton("Manage Smart Objects");
        JButton manageEnergySourcesButton = new JButton("Manage Energy Sources");
        JButton exitButton = new JButton("Exit");

        // Add action listeners to buttons
        /*
        systemSummaryButton.addActionListener(e -> showSystemSummaryWindow());
        manageSmartObjectsButton.addActionListener(e -> showSmartObjectUI());
        manageEnergySourcesButton.addActionListener(e -> showEnergySourceUI());
        simulateConsumptionButton.addActionListener(e -> simulateConsumption());
        exitButton.addActionListener(e -> System.exit(0));
        */
        systemSummaryButton.addActionListener(e -> {
            try {
                showSystemSummaryWindow();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: Unable to load System Summary. " + ex.getMessage(), 
                                              "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        manageSmartObjectsButton.addActionListener(e -> {
            try {
                showSmartObjectUI();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: Unable to manage Smart Objects. " + ex.getMessage(), 
                                              "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        manageEnergySourcesButton.addActionListener(e -> {
            try {
                showEnergySourceUI();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: Unable to manage Energy Sources. " + ex.getMessage(), 
                                              "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        exitButton.addActionListener(e -> System.exit(0)); // Exit button doesn't require exception handling

        // Add buttons to the panel
        buttonPanel.add(systemSummaryButton);
        buttonPanel.add(manageSmartObjectsButton);
        buttonPanel.add(manageEnergySourcesButton);
        buttonPanel.add(exitButton);

        // Add the button panel to the center of the frame
        add(buttonPanel, BorderLayout.CENTER);
        
    } catch (Exception e) {
        // Handle exceptions during initialization
        JOptionPane.showMessageDialog(this, "Critical Error: Unable to initialize the dashboard. " + e.getMessage(),
                                      "Initialization Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}

    // Show Smart Object Management UI
    public void showSmartObjectUI() {
    	try {
    		SmartObjectUI smartObjectUI = new SmartObjectUI(energySourceUI);
    		smartObjectUI.showGUI(); // Opens the SmartObjectUI GUI
    	} catch (Exception e) {
            throw new RuntimeException("Failed to load Smart Object UI.", e);
        }
    }

    // Show Energy Source Management UI
    public void showEnergySourceUI() {
    	try {
    		energySourceUI.manageEnergySourcesGUI(); // Opens the EnergySourceUI GUI
    } catch (Exception e) {
        throw new RuntimeException("Failed to load Energy Source UI.", e);
    }
}
    
    // Show System Summary Window
    public void showSystemSummaryWindow() {
    	try {
    		new SystemSummaryWindow(); // Opens the SystemSummaryWindow
    } catch (Exception e) {
        throw new RuntimeException("Failed to load System Summary Window.", e);
    }
}

    // Simulate consumption logic (placeholder)
    private void simulateConsumption() {
        JOptionPane.showMessageDialog(this, "Simulate Consumption feature coming soon.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
        	try {
        		EnergySourceUI energySourceUI = new EnergySourceUI();
        		EnergyManagementDashboardUI dashboard = new EnergyManagementDashboardUI(energySourceUI);
        		dashboard.setVisible(true); // Make the dashboard visible
        	} catch (Exception e) {
                System.err.println("Error starting the application: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
