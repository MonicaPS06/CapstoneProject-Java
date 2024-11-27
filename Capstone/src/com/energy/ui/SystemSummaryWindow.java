package com.energy.ui;

import com.energy.sources.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SystemSummaryWindow extends JFrame {

    private List<Battery> batteries = new ArrayList<>();
    private List<SolarPanel> solarPanels = new ArrayList<>();
    private List<GridConnection> gridConnections = new ArrayList<>();
    private List<SmartObject> smartObjects = new ArrayList<>();
    private List<String> errorLogs = new ArrayList<>(); // To log errors during loading

    public SystemSummaryWindow() {
        setTitle("System Summary");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        try {
        	// Load data from files
        	loadEnergySources();
        	loadSmartObjects();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Critical error initializing system summary: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Create Tabs
        JTabbedPane tabbedPane = new JTabbedPane();

        // Energy Sources Tab
        JPanel energySourcesPanel = createEnergySourcesPanel();
        tabbedPane.addTab("Energy Sources", energySourcesPanel);

        // Smart Objects Tab
        JPanel smartObjectsPanel = createSmartObjectsPanel();
        tabbedPane.addTab("Smart Objects", smartObjectsPanel);
        
        // Errors Tab (if any errors occurred)
        if (!errorLogs.isEmpty()) {
            JPanel errorPanel = createErrorPanel();
            tabbedPane.addTab("Loading Errors", errorPanel);
        }

        // Add Tabs to Frame
        add(tabbedPane, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel createEnergySourcesPanel() {
        String[] columns = {"Type", "Name", "Details"};
        Object[][] data = Stream.concat(
            batteries.stream().map(b -> new Object[]{"Battery", b.getName(), b.getMaxCapacity() + " kWh"}),
            Stream.concat(
                solarPanels.stream().map(s -> new Object[]{"Solar Panel", s.getName(), s.getGenerationRate() + " kWh"}),
                gridConnections.stream().map(g -> new Object[]{"Grid Connection", g.getName(), "Always Available"})
            )
        ).toArray(Object[][]::new);

        JTable table = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(table);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createSmartObjectsPanel() {
        String[] columns = {"Type", "Name", "Energy Demand"};
        Object[][] data = smartObjects.stream()
            .map(o -> {
                if (o instanceof SmartLight) {
                    return new Object[]{"Smart Light", o.getName(), ((SmartLight) o).getEnergyDemand() + " kWh"};
                } else if (o instanceof SmartThermostat) {
                    return new Object[]{"Smart Thermostat", o.getName(), ((SmartThermostat) o).getEnergyDemand() + " kWh"};
                }
                return new Object[]{"Unknown", o.getName(), "N/A"};
            })
            .toArray(Object[][]::new);

        JTable table = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(table);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createErrorPanel() {
        JTextArea errorArea = new JTextArea(String.join("\n", errorLogs));
        errorArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(errorArea);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void loadEnergySources() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("energy_sources.dat"))) {
            List<Object> sources = (List<Object>) ois.readObject();
            for (Object source : sources) {
                if (source instanceof Battery) {
                    batteries.add((Battery) source);
                } else if (source instanceof SolarPanel) {
                    solarPanels.add((SolarPanel) source);
                } else if (source instanceof GridConnection) {
                    gridConnections.add((GridConnection) source);
                }
            }
        } catch (FileNotFoundException e) {
            String message = "Energy sources file not found.";
            errorLogs.add(message);
            System.err.println(message);
        } catch (ClassCastException | ClassNotFoundException e) {
            String message = "Invalid format in energy sources file: " + e.getMessage();
            errorLogs.add(message);
            System.err.println(message);
        } catch (EOFException e) {
            String message = "Energy sources file is empty or incomplete.";
            errorLogs.add(message);
            System.err.println(message);
        } catch (IOException e) {
            String message = "Error reading energy sources file: " + e.getMessage();
            errorLogs.add(message);
            System.err.println(message);
        }
    }

    private void loadSmartObjects() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("smart_objects.dat"))) {
            smartObjects = (List<SmartObject>) ois.readObject();
        } catch (FileNotFoundException e) {
            String message = "Smart objects file not found.";
            errorLogs.add(message);
            System.err.println(message);
        } catch (ClassCastException | ClassNotFoundException e) {
            String message = "Invalid format in smart objects file: " + e.getMessage();
            errorLogs.add(message);
            System.err.println(message);
        } catch (EOFException e) {
            String message = "Smart objects file is empty or incomplete.";
            errorLogs.add(message);
            System.err.println(message);
        } catch (IOException e) {
            String message = "Error reading smart objects file: " + e.getMessage();
            errorLogs.add(message);
            System.err.println(message);
        }
    }
}
