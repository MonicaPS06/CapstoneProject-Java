package com.energy.sources;

import java.io.Serializable;
import com.energy.logging.SystemLogger;

public class SolarPanel extends EnergySource implements Serializable {
    private static final long serialVersionUID = 1L; // Recommended for Serializable classes

    private double generationRate; // Energy generated per hour (kWh)

    // Constructor to match the expected parameters
    public SolarPanel(String name, double generationRate) {
        super(name, Double.MAX_VALUE); // Assuming solar panels have no storage limit
        if (generationRate <= 0) {
            throw new IllegalArgumentException("Generation rate must be greater than zero.");
        }
        this.generationRate = generationRate;
        
     // Logging solar panel initialization
        SystemLogger.logEvent("SolarPanel \"" + name + "\" initialized with generation rate: " + generationRate + " kWh/hour.");
    }

    @Override
    public double generateEnergy(double hours) {
    	if (hours < 0) {
            throw new IllegalArgumentException("Hours must be non-negative.");
        }
        double totalGenerated = 0; // Track total energy generated
        double energyPerSecond = generationRate; // Energy generated per second

        long totalSeconds = (long) (hours * 1); // 1 hour = 1 second for simulation
        for (int i = 0; i < totalSeconds; i++) {
            try {
                Thread.sleep(1000); // Wait for 1 second to mimic real-time simulation
                totalGenerated += energyPerSecond; // Generate energy incrementally
                System.out.println(getName() + " generated " + energyPerSecond + " kWh. Total: " + totalGenerated + " kWh.");
                
                // Logging energy generation for each step
                SystemLogger.logEvent("SolarPanel \"" + getName() + "\" generated " + energyPerSecond + " kWh. Total: " + totalGenerated + " kWh.");
            } catch (InterruptedException e) {
                System.err.println("Simulation interrupted: " + e.getMessage());
             // Logging interruptions during simulation
                SystemLogger.logError("Simulation interrupted for SolarPanel \"" + getName() + "\": " + e.getMessage());
                break;
            }
        }

        return totalGenerated;
    }
    
    public double getGenerationRate() {
        return generationRate;
    }

    @Override
    public String toString() {
        return "SolarPanel{name='" + name + "', generationRate=" + generationRate + " kWh/hour}";
    }
}
