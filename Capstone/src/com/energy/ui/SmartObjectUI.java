package com.energy.ui;

import com.energy.sources.*;
import com.energy.logging.SystemLogger;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.*;

public class SmartObjectUI {
    private List<SmartObject> smartObjects;
    private EnergySourceUI energySourceUI; // Reference to EnergySourceUI instance

    // Constructor
    public SmartObjectUI(EnergySourceUI energySourceUI) {
        this.smartObjects = new ArrayList<>();
        this.energySourceUI = energySourceUI; // Pass EnergySourceUI instance
        
        try {
        	loadSmartObjects(); // Load smart objects from file at startup
        } catch (Exception e) {
            SystemLogger.logError("Failed to load smart objects: " + e.getMessage());
        }
    }
    
    public List<Battery> getBatteries() {
    	try {
    		return energySourceUI.getBatteries(); // Delegate the call to EnergySourceUI
    	} catch (Exception e) {
            SystemLogger.logError("Error retrieving batteries: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // GUI-based method for managing smart objects
    public void showGUI() {
    		JFrame frame = new JFrame("Smart Object Management");
    		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    		frame.setSize(600, 400);
    		frame.setLayout(new GridLayout(3, 2, 10, 10));

    		JButton addLightButton = new JButton("Add Smart Light");
    		addLightButton.addActionListener(e -> addSmartLight(frame));

    		JButton addThermostatButton = new JButton("Add Smart Thermostat");
    		addThermostatButton.addActionListener(e -> addSmartThermostat(frame));

    		JButton listDevicesButton = new JButton("List All Devices");
    		listDevicesButton.addActionListener(e -> listAllDevices(frame));

    		JButton activateButton = new JButton("Activate Devices");
    		activateButton.addActionListener(e -> activateDevices(frame));

    		JButton deactivateButton = new JButton("Deactivate Devices");
    		deactivateButton.addActionListener(e -> deactivateDevices(frame));

    		JButton simulateButton = new JButton("Simulate Energy Consumption");
    		simulateButton.addActionListener(e -> simulateEnergyConsumptionGUI(frame));
        
    		JButton removeLightButton = new JButton("Remove Smart Light");
    		removeLightButton.addActionListener(e -> removeSmartLight(frame));
        
    		JButton removeThermostatButton = new JButton("Remove Smart Thermostat");
    		removeThermostatButton.addActionListener(e -> removeSmartThermostat(frame));
        
    		JButton removeAllSmartObjectsButton = new JButton("Remove All Smart Objects");
    		removeAllSmartObjectsButton.addActionListener(e -> removeAllSmartObjects(frame));

    		frame.add(addLightButton);
    		frame.add(addThermostatButton);
    		frame.add(listDevicesButton);
    		frame.add(activateButton);
    		frame.add(deactivateButton);
    		frame.add(simulateButton);
    		frame.add(removeLightButton);
    		frame.add(removeThermostatButton);
    		frame.add(removeAllSmartObjectsButton);

    		frame.setVisible(true);
    }
    
    // Add multiple SmartObjects at once
    public void addSmartObjects(List<SmartObject> objects) {
        if (objects != null && !objects.isEmpty()) {
            smartObjects.addAll(objects);
            SystemLogger.logEvent(objects.size() + " Smart Objects added.");
        } else {
            SystemLogger.logEvent("No Smart Objects were added (list is null or empty).");
        }
    }
    
    private void addSmartObject(SmartObject smartObject) {
        if (smartObject == null) {
            SystemLogger.logError("Attempted to add a null SmartObject.");
            return;
        }
        smartObjects.add(smartObject);
        saveSmartObjects(); // Save the updated list of SmartObjects to the file
        SystemLogger.logEvent("SmartObject added: " + smartObject.getName());
    }


    private void addSmartLight(JFrame frame) {
        try {
            String name = JOptionPane.showInputDialog(frame, "Enter Smart Light name:");
            String energyDemandStr = JOptionPane.showInputDialog(frame, "Enter energy demand (kWh):");
            double energyDemand = Double.parseDouble(energyDemandStr);

            // Retrieve grid connection from EnergySourceUI
            GridConnection gridConnection = energySourceUI.getGridConnection();

            // Create and add SmartLight
            SmartLight light = new SmartLight(name, energyDemand, energySourceUI.getBatteries(), gridConnection);
            addSmartObject(light);

            JOptionPane.showMessageDialog(frame, "Smart Light added successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Failed to add Smart Light: " + e.getMessage());
        }
    }

    private void addSmartThermostat(JFrame frame) {
        try {
            String name = JOptionPane.showInputDialog(frame, "Enter Smart Thermostat name:");
            String energyDemandStr = JOptionPane.showInputDialog(frame, "Enter energy demand (kWh):");
            double energyDemand = Double.parseDouble(energyDemandStr);

            // Retrieve grid connection from EnergySourceUI
            GridConnection gridConnection = energySourceUI.getGridConnection();

            // Create and add SmartThermostat
            SmartThermostat thermostat = new SmartThermostat(name, energyDemand, energySourceUI.getBatteries(), gridConnection);
            addSmartObject(thermostat);

            JOptionPane.showMessageDialog(frame, "Smart Thermostat added successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Failed to add Smart Thermostat: " + e.getMessage());
        }
    }

    private void listAllDevices(JFrame frame) {
        if (smartObjects.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No devices available.");
        } else {
            StringBuilder deviceList = new StringBuilder();
            for (SmartObject obj : smartObjects) {
                deviceList.append("Name: ").append(obj.getName())
                        .append(", Active: ").append(obj.isActive())
                        .append(", Energy Demand: ").append(obj.getEnergyDemand()).append(" kWh")
                        .append("\n");
            }
            JOptionPane.showMessageDialog(frame, deviceList.toString());
        }
    }

    /*
    private void activateDevices(JFrame frame) {
        for (SmartObject obj : smartObjects) {
            obj.activate();
        }
        JOptionPane.showMessageDialog(frame, "All devices activated.");
    }
    */
    
    private void activateDevices(JFrame frame) {
        try {
            // Prompt the user to choose between activating all or one device
            String[] options = {"Activate All", "Activate One"};
            int choice = JOptionPane.showOptionDialog(frame, 
                "Do you want to activate all devices or a specific one?", 
                "Activate Devices", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE, 
                null, 
                options, 
                options[0]);

            if (choice == JOptionPane.YES_OPTION) {
                // Activate all devices
                for (SmartObject obj : smartObjects) {
                    obj.activate();
                }
                JOptionPane.showMessageDialog(frame, "All devices activated.");
            } else if (choice == JOptionPane.NO_OPTION) {
                // Activate a specific device
                String name = JOptionPane.showInputDialog(frame, "Enter the name of the device to activate:");
                if (name == null || name.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Device name cannot be empty.");
                    return;
                }

                // Search for the device and activate it
                boolean activated = false;
                for (SmartObject obj : smartObjects) {
                    if (obj.getName().equalsIgnoreCase(name)) {
                        obj.activate();
                        activated = true;
                        JOptionPane.showMessageDialog(frame, "Device '" + name + "' activated.");
                        break;
                    }
                }

                if (!activated) {
                    JOptionPane.showMessageDialog(frame, "No device found with the name '" + name + "'.");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "No action selected.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "An error occurred: " + ex.getMessage());
        }
    }


    /*
    private void deactivateAllDevices(JFrame frame) {
        for (SmartObject obj : smartObjects) {
            obj.deactivate();
        }
        JOptionPane.showMessageDialog(frame, "All devices deactivated.");
    }
    */
    
    private void deactivateDevices(JFrame frame) {
        try {
            // Prompt the user to choose between deactivating all or one device
            String[] options = {"Deactivate All", "Deactivate One"};
            int choice = JOptionPane.showOptionDialog(frame, 
                "Do you want to deactivate all devices or a specific one?", 
                "Deactivate Devices", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE, 
                null, 
                options, 
                options[0]);

            if (choice == JOptionPane.YES_OPTION) {
                // Deactivate all devices
                for (SmartObject obj : smartObjects) {
                    obj.deactivate();
                }
                JOptionPane.showMessageDialog(frame, "All devices deactivated.");
            } else if (choice == JOptionPane.NO_OPTION) {
                // Deactivate a specific device
                String name = JOptionPane.showInputDialog(frame, "Enter the name of the device to deactivate:");
                if (name == null || name.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Device name cannot be empty.");
                    return;
                }

                // Search for the device and deactivate it
                boolean deactivated = false;
                for (SmartObject obj : smartObjects) {
                    if (obj.getName().equalsIgnoreCase(name)) {
                        obj.deactivate();
                        deactivated = true;
                        JOptionPane.showMessageDialog(frame, "Device '" + name + "' deactivated.");
                        break;
                    }
                }

                if (!deactivated) {
                    JOptionPane.showMessageDialog(frame, "No device found with the name '" + name + "'.");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "No action selected.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "An error occurred: " + ex.getMessage());
        }
    }

    
    private void simulateEnergyConsumptionGUI(JFrame frame) {
        try {
            // Get simulation hours from the user
            String hoursStr = JOptionPane.showInputDialog(frame, "Enter hours of simulation:");
            if (hoursStr == null || hoursStr.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Simulation hours cannot be empty.");
                return;
            }

            double hours = Double.parseDouble(hoursStr); // Parse input
            StringBuilder results = new StringBuilder("Smart Object Energy Consumption Results:\n");

            // Fetch batteries from EnergySourceUI
            List<Battery> batteries = getBatteries();
            if (batteries == null || batteries.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No batteries available! Falling back to the grid.");
            }
            
            // Create threads for each smart object
            List<Thread> threads = new ArrayList<>();

            for (SmartObject obj : smartObjects) {
                Thread thread = new Thread(() -> {
                    if (obj.isActive()) {
                        double totalConsumed = obj.consumeEnergy(hours, batteries); // Call the consumeEnergy method

                        synchronized (results) {
                            results.append(obj.getName())
                                    .append(": Consumed ")
                                    .append(totalConsumed)
                                    .append(" kWh over ")
                                    .append(hours)
                                    .append(" hours.\n");
                        }
                    } else {
                        synchronized (results) {
                            results.append(obj.getName())
                                    .append(" is inactive. No energy consumed.\n");
                        }
                    }
                });

                threads.add(thread); // Add thread to the list
                thread.start(); // Start the thread
            }

            
            // Wait for all threads to complete
            for (Thread thread : threads) {
                thread.join();
            }
            
            saveSmartObjects();
            energySourceUI.saveEnergySources();

            // Display results
            JOptionPane.showMessageDialog(frame, results.toString(), "Energy Consumption Results", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Simulation failed: " + e.getMessage());
        }
    }
    
    private void removeSmartLight(JFrame frame) {
        try {
            // Prompt for the name of the smart light to remove
            String name = JOptionPane.showInputDialog(frame, "Enter the name of the Smart Light to remove:");
            if (name == null || name.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Smart Light name cannot be empty.");
                return;
            }

            // Search for the smart light and remove it
            boolean removed = smartObjects.removeIf(object -> object instanceof SmartLight && object.getName().equalsIgnoreCase(name));

            if (removed) {
                // Save the updated list of smart objects
                saveSmartObjects();
                JOptionPane.showMessageDialog(frame, "Smart Light '" + name + "' removed successfully.");
            } else {
                // Notify if no matching smart light was found
                JOptionPane.showMessageDialog(frame, "No Smart Light found with the name '" + name + "'.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Failed to remove Smart Light: " + e.getMessage());
        }
    }
    
    private void removeSmartThermostat(JFrame frame) {
        try {
            // Prompt for the name of the smart thermostat to remove
            String name = JOptionPane.showInputDialog(frame, "Enter the name of the Smart Thermostat to remove:");
            if (name == null || name.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Smart Thermostat name cannot be empty.");
                return;
            }

            // Search for the smart thermostat and remove it
            boolean removed = smartObjects.removeIf(object -> object instanceof SmartThermostat && object.getName().equalsIgnoreCase(name));

            if (removed) {
                // Save the updated list of smart objects
                saveSmartObjects();
                JOptionPane.showMessageDialog(frame, "Smart Thermostat '" + name + "' removed successfully.");
            } else {
                // Notify if no matching smart thermostat was found
                JOptionPane.showMessageDialog(frame, "No Smart Thermostat found with the name '" + name + "'.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Failed to remove Smart Thermostat: " + e.getMessage());
        }
    }
    
    private void removeAllSmartObjects(JFrame frame) {
        int confirm = JOptionPane.showConfirmDialog(frame, 
            "Are you sure you want to remove all Smart Objects?", 
            "Confirm Remove All", 
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Clear the list of smart objects
                smartObjects.clear();

                // Save changes to the file
                saveSmartObjects();

                JOptionPane.showMessageDialog(frame, "All Smart Objects have been removed successfully.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "An error occurred: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Operation canceled.");
        }
    }

    // Save SmartObjects to file
    private void saveSmartObjects() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("smart_objects.dat"))) {
            oos.writeObject(smartObjects);
        } catch (IOException e) {
            SystemLogger.logError("Failed to save Smart Objects: " + e.getMessage());
        }
    }

    // Load SmartObjects from file
    private void loadSmartObjects() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("smart_objects.dat"))) {
            smartObjects = (List<SmartObject>) ois.readObject();
        } catch (FileNotFoundException e) {
            SystemLogger.logEvent("No saved devices found. Starting with an empty list.");
        } catch (Exception e) {
            SystemLogger.logError("Failed to load Smart Objects: " + e.getMessage());
        }
    }

}
