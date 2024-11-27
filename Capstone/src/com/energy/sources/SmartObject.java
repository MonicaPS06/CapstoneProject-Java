package com.energy.sources;

import java.io.Serializable;
import java.util.List;
import com.energy.logging.SystemLogger;

public abstract class SmartObject implements Serializable {
    private static final long serialVersionUID = 1L; // Recommended for Serializable classes

    protected String name;
    protected double energyDemand; // Energy required for operation (kWh)
    protected boolean active; // Indicates whether the object is active
    protected Battery battery; // Reference to the battery
    protected GridConnection gridConnection; // Reference to the grid connection

    // Constructor
    public SmartObject(String name, double energyDemand, Battery battery, GridConnection gridConnection) {
    	if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Smart object name cannot be null or empty.");
        }
        if (energyDemand <= 0) {
            throw new IllegalArgumentException("Energy demand must be greater than zero.");
        }
    	this.name = name;
        this.energyDemand = energyDemand;
        this.active = false; // Default state is inactive
        this.battery = battery;
        this.gridConnection = gridConnection;
        
     // Log initialization
        SystemLogger.logEvent("SmartObject \"" + name + "\" initialized with energy demand: " + energyDemand + " kWh.");
    }

    // Method to consume energy
    public abstract double consumeEnergy(double demand, List<Battery> batteries);

    // Method to retrieve consumption details
    public String getConsumptionDetails(double energyConsumed) {
    	String details = "SmartObject: " + name + ", Energy Consumed: " + energyConsumed + " kWh";
        // Log consumption details
        SystemLogger.logEvent("Consumption details for \"" + name + "\": " + details);
        return details;
    }

    // Activate the object
    public void activate() {
        this.active = true;
     // Log activation
        SystemLogger.logEvent("SmartObject \"" + name + "\" has been activated.");
        System.out.println(name + " has been activated.");
    }

    // Deactivate the object
    public void deactivate() {
        this.active = false;
     // Log deactivation
        SystemLogger.logEvent("SmartObject \"" + name + "\" has been deactivated.");
        System.out.println(name + " has been deactivated.");
    }

    // Check if the object is active
    public boolean isActive() {
        return active;
    }

    // Get the name of the object
    public String getName() {
        return name;
    }

    // Get the energy demand of the object
    public double getEnergyDemand() {
        return energyDemand;
    }

    @Override
    public String toString() {
        return "SmartObject{name='" + name + "', energyDemand=" + energyDemand 
            + ", active=" + active + '}';
    }
}
