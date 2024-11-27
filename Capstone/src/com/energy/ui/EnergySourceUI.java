package com.energy.ui;

import com.energy.sources.Battery;
import com.energy.sources.SolarPanel;
import com.energy.sources.GridConnection;
import com.energy.sources.EnergySource;
import com.energy.logging.SystemLogger;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EnergySourceUI {
    private List<EnergySource> energySources; // SolarPanel, GridConnection
    private List<Battery> batteries; // Separate list for batteries
    private List<GridConnection> gridConnections; // Separate list for grid connections

    // Constructor
    public EnergySourceUI() {
        energySources = new ArrayList<>();
        batteries = new ArrayList<>();
        gridConnections = new ArrayList<>(); // Initialize gridConnections
        
        try {
        	loadEnergySources(); // Load saved data when the application starts
        } catch (Exception e) {
            SystemLogger.logError("Failed to load energy sources: " + e.getMessage());
        }
        
     // Ensure a default grid connection exists
        if (!energySources.stream().anyMatch(source -> source instanceof GridConnection)) {
            GridConnection defaultGrid = new GridConnection("Default Grid");
            gridConnections.add(defaultGrid);
            energySources.add(defaultGrid);
            SystemLogger.logEvent("Default Grid added automatically.");
        }
    }
    
    public List<Battery> getBatteries() {
    	System.out.println("Batteries retrieved: " + batteries);
        if (batteries == null) {
            batteries = new ArrayList<>();
        }
        return batteries;
    }


    // GUI-based UI for managing energy sources
    public void manageEnergySourcesGUI() {
    	try {
    		JFrame frame = new JFrame("Energy Source Management");
    		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    		frame.setSize(600, 400);
    		frame.setLayout(new BorderLayout());

    		JPanel buttonPanel = new JPanel();
    		buttonPanel.setLayout(new GridLayout(3, 2, 10, 10)); // Use GridLayout for button arrangement

    		// Add buttons with GUI functionality
    		addGUIButtons(buttonPanel, frame);

    		// Add the panel to the frame
    		frame.add(buttonPanel, BorderLayout.CENTER);

    		// Make the frame visible
    		frame.setVisible(true);
    } catch (Exception e) {
        SystemLogger.logError("Error initializing Energy Source Management GUI: " + e.getMessage());
    }
}

    private void addGUIButtons(JPanel buttonPanel, JFrame frame) {
        JButton addSolarButton = new JButton("Add Solar Panel");
        addSolarButton.addActionListener(e -> {
            try {
                addSolarPanelGUI(frame);
            } catch (Exception ex) {
                SystemLogger.logError("Error adding Solar Panel: " + ex.getMessage());
                JOptionPane.showMessageDialog(frame, "Failed to add Solar Panel: " + ex.getMessage());
            }
        });

        JButton addBatteryButton = new JButton("Add Battery");
        addBatteryButton.addActionListener(e -> {
            try {
                addBatteryGUI(frame);
            } catch (Exception ex) {
                SystemLogger.logError("Error adding Battery: " + ex.getMessage());
                JOptionPane.showMessageDialog(frame, "Failed to add Battery: " + ex.getMessage());
            }
        });

        JButton addGridButton = new JButton("Add Grid Connection");
        addGridButton.addActionListener(e -> addGridConnectionGUI(frame));

        JButton listSourcesButton = new JButton("List Energy Sources");
        listSourcesButton.addActionListener(e -> {
            try {
                listEnergySourcesGUI(frame);
            } catch (Exception ex) {
                SystemLogger.logError("Error listing energy sources: " + ex.getMessage());
                JOptionPane.showMessageDialog(frame, "Failed to list energy sources: " + ex.getMessage());
            }
        });

        JButton simulateButton = new JButton("Simulate Energy Generation");
        simulateButton.addActionListener(e -> {
            try {
                simulateEnergyGenerationGUI(frame);
            } catch (Exception ex) {
                SystemLogger.logError("Error simulating energy generation: " + ex.getMessage());
                JOptionPane.showMessageDialog(frame, "Failed to simulate energy generation: " + ex.getMessage());
            }
        });
        
        JButton removeSolarButton = new JButton("Remove Solar Panel");
        removeSolarButton.addActionListener(e -> {
            try {
                removeSolarPanelGUI(frame);
            } catch (Exception ex) {
                SystemLogger.logError("Error removing Solar Panel: " + ex.getMessage());
                JOptionPane.showMessageDialog(frame, "Failed to remove Solar Panel: " + ex.getMessage());
            }
        });
        
        JButton removeBatteryButton = new JButton("Remove Battery");
        removeBatteryButton.addActionListener(e -> {
            try {
                removeBatteryGUI(frame);
            } catch (Exception ex) {
                SystemLogger.logError("Error removing Battery: " + ex.getMessage());
                JOptionPane.showMessageDialog(frame, "Failed to remove Battery: " + ex.getMessage());
            }
        });

        buttonPanel.add(addSolarButton);
        buttonPanel.add(addBatteryButton);
        buttonPanel.add(addGridButton);
        buttonPanel.add(listSourcesButton);
        buttonPanel.add(simulateButton);
        buttonPanel.add(removeSolarButton);
        buttonPanel.add(removeBatteryButton);
    }

    private void addSolarPanelGUI(JFrame frame) {
        try {
            String name = JOptionPane.showInputDialog(frame, "Enter Solar Panel name:");
            if (name == null || name.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Solar Panel name cannot be empty.");
                return;
            }

            String generationRateStr = JOptionPane.showInputDialog(frame, "Enter generation rate (kWh):");
            if (generationRateStr == null || generationRateStr.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Generation rate cannot be empty.");
                return;
            }

            double generationRate = Double.parseDouble(generationRateStr);

            // Check for duplicates
            if (energySources.stream().anyMatch(source -> source.getName().equalsIgnoreCase(name))) {
                JOptionPane.showMessageDialog(frame, "Solar Panel with this name already exists!");
                return;
            }

            SolarPanel solarPanel = new SolarPanel(name, generationRate);
            energySources.add(solarPanel);
            saveEnergySources();
            JOptionPane.showMessageDialog(frame, "Solar Panel added successfully!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid input. Please enter numeric values.");
        }
    }

    private void addBatteryGUI(JFrame frame) {
        try {
            String name = JOptionPane.showInputDialog(frame, "Enter Battery name:");
            if (name == null || name.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Battery name cannot be empty.");
                return;
            }

            String capacityStr = JOptionPane.showInputDialog(frame, "Enter max capacity (kWh):");
            if (capacityStr == null || capacityStr.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Capacity cannot be empty.");
                return;
            }

            double capacity = Double.parseDouble(capacityStr);

            // Check for duplicates
            if (batteries.stream().anyMatch(battery -> battery.getName().equalsIgnoreCase(name))) {
                JOptionPane.showMessageDialog(frame, "Battery with this name already exists!");
                return;
            }

            Battery battery = new Battery(name, capacity);
            batteries.add(battery);
            saveEnergySources();
            JOptionPane.showMessageDialog(frame, "Battery added successfully!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid input. Please enter numeric values.");
        }
    }

    private void addGridConnectionGUI(JFrame frame) {
        JOptionPane.showMessageDialog(frame, "A grid connection is already available and cannot be added again.");
    }


    private void listEnergySourcesGUI(JFrame frame) {
        if (energySources.isEmpty() && batteries.isEmpty() && gridConnections.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No energy sources, batteries, or grid connections available.");
        } else {
            StringBuilder sourceList = new StringBuilder("Energy Sources:\n");
            energySources.forEach(source -> sourceList.append(source.toString()).append("\n"));
            sourceList.append("Batteries:\n");
            batteries.forEach(battery -> sourceList.append(battery.toString()).append("\n"));
            sourceList.append("Grid Connections:\n");
            gridConnections.forEach(grid -> sourceList.append(grid.toString()).append("\n"));
            JOptionPane.showMessageDialog(frame, sourceList.toString());
        }
    }

    private void simulateEnergyGenerationGUI(JFrame frame) {
        try {
            // Get simulation hours from the user
            String hoursStr = JOptionPane.showInputDialog(frame, "Enter hours of simulation:");
            if (hoursStr == null || hoursStr.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Simulation hours cannot be empty.");
                return;
            }

            double hours = Double.parseDouble(hoursStr);
            StringBuilder results = new StringBuilder("Simulation Results:\n");

            // Create threads for each energy source
            List<Thread> threads = new ArrayList<>();

            for (EnergySource source : energySources) {
                Thread thread = new Thread(() -> {
                    double generated = source.generateEnergy(hours); // Generate energy concurrently
                    synchronized (results) {
                        results.append(source.getName()).append(" generated ").append(generated).append(" kWh.\n");

                        if (source instanceof SolarPanel) {
                            double remainingEnergy = generated;
                            for (Battery battery : batteries) {
                                synchronized (battery) { // Synchronize battery storage
                                    remainingEnergy = battery.storeEnergy(remainingEnergy);
                                }
                                if (remainingEnergy == 0) break;
                            }

                            if (remainingEnergy > 0) {
                                results.append("Overflow: ").append(remainingEnergy).append(" kWh sent to grid.\n");
                            }
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

            // Save energy sources and show the results
            saveEnergySources();
            JOptionPane.showMessageDialog(frame, results.toString());

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid input. Please enter numeric values.");
        } catch (InterruptedException ex) {
            JOptionPane.showMessageDialog(frame, "Simulation interrupted: " + ex.getMessage());
        }
    }
    
    //remove solar panel
    private void removeSolarPanelGUI(JFrame frame) {
        try {
            // Prompt for the name of the solar panel to remove
            String name = JOptionPane.showInputDialog(frame, "Enter the name of the Solar Panel to remove:");
            if (name == null || name.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Solar Panel name cannot be empty.");
                return;
            }

            // Attempt to remove the solar panel by name
            boolean removed = energySources.removeIf(source -> source.getName().equalsIgnoreCase(name));

            if (removed) {
                // Save the updated list to the file
                saveEnergySources();
                JOptionPane.showMessageDialog(frame, "Solar Panel '" + name + "' removed successfully.");
            } else {
                // Notify if no matching solar panel was found
                JOptionPane.showMessageDialog(frame, "No Solar Panel found with the name '" + name + "'.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "An error occurred: " + ex.getMessage());
        }
    }
    
    //remove solar panel
    private void removeBatteryGUI(JFrame frame) {
        try {
            // Prompt for the name of the battery to remove
            String name = JOptionPane.showInputDialog(frame, "Enter the name of the Battery to remove:");
            if (name == null || name.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Battery name cannot be empty.");
                return;
            }

            // Check for the battery and remove it
            boolean removed = batteries.removeIf(battery -> battery.getName().equalsIgnoreCase(name));

            if (removed) {
                // Save the updated list to the file
                saveEnergySources();
                JOptionPane.showMessageDialog(frame, "Battery '" + name + "' removed successfully.");
            } else {
                // Notify if no matching battery was found
                JOptionPane.showMessageDialog(frame, "No Battery found with the name '" + name + "'.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "An error occurred: " + ex.getMessage());
        }
    }




    // Save energy sources to file
    public void saveEnergySources() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("energy_sources.dat"))) {
            List<Object> allSources = new ArrayList<>();
            if (energySources != null) allSources.addAll(energySources);
            if (batteries != null) allSources.addAll(batteries);
            if (gridConnections != null) allSources.addAll(gridConnections);

            oos.writeObject(allSources);
            SystemLogger.logEvent("Energy sources saved successfully.");
        } catch (IOException e) {
            SystemLogger.logError("Error saving energy sources: " + e.getMessage());
        }
    }


    // Load energy sources from file
    private void loadEnergySources() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("energy_sources.dat"))) {
            List<Object> allSources = (List<Object>) ois.readObject();
            energySources = new ArrayList<>();
            batteries = new ArrayList<>();
            boolean gridFound = false;

            for (Object source : allSources) {
                if (source instanceof GridConnection) {
                    if (!gridFound) {
                        energySources.add((EnergySource) source);
                        gridFound = true; // Only allow one grid connection
                    }
                } else if (source instanceof EnergySource) {
                    energySources.add((EnergySource) source);
                } else if (source instanceof Battery) {
                    batteries.add((Battery) source);
                }
            }

            // If no grid was found, create one
            if (!gridFound) {
                GridConnection defaultGrid = new GridConnection("Default Grid");
                energySources.add(defaultGrid);
                SystemLogger.logEvent("No grid connection found in file. Default Grid created.");
            }
        } catch (FileNotFoundException e) {
            SystemLogger.logEvent("No existing energy sources file found. Starting with an empty list.");
        } catch (IOException | ClassNotFoundException e) {
            SystemLogger.logError("Error loading energy sources: " + e.getMessage());
        }
    }


    // Get the first available battery
    public Battery getBattery() {
        if (!batteries.isEmpty()) {
            return batteries.get(0);
        }
        return null; // No battery available
    }

    //Always Available
    public GridConnection getGridConnection() {
        return energySources.stream()
                .filter(source -> source instanceof GridConnection)
                .map(source -> (GridConnection) source)
                .findFirst()
                .orElse(null); // This will never be null because the grid is added by default
    }

}
