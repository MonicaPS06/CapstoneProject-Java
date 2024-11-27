
package com.energy.sources;

import java.io.Serializable;
import com.energy.logging.SystemLogger;

public class Battery implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private double maxCapacity; // Maximum energy capacity
    private double currentCharge; // Current stored energy

    public Battery(String name, double maxCapacity) {
    	if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Battery name cannot be null or empty.");
        }
        if (maxCapacity <= 0) {
            throw new IllegalArgumentException("Battery max capacity must be greater than zero.");
        }
        this.name = name;
        this.maxCapacity = maxCapacity;
        this.currentCharge = 0; // Initially empty
        
     // Logging battery initialization
        SystemLogger.logEvent("Battery initialized: " + name + " with max capacity: " + maxCapacity + " kWh.");
    }

    // Method to store energy in the battery
    public synchronized double storeEnergy(double energy) {
    	if (energy < 0) {
            throw new IllegalArgumentException("Energy to store cannot be negative.");
        }
    	
        double overflow = 0;
        if (currentCharge + energy > maxCapacity) {
            overflow = (currentCharge + energy) - maxCapacity;
            currentCharge = maxCapacity; // Battery is full
            
         // Logging charge overflow
            SystemLogger.logEvent("Battery \"" + name + "\" overflow: Excess energy of " + overflow + " kWh returned.");
        } else {
            currentCharge += energy;
        }
     // Logging energy storage
        SystemLogger.logEvent("Battery \"" + name + "\" stored " + energy + " kWh. Current charge: " + currentCharge + " kWh.");
        return overflow; // Return the excess energy
    }

    // Method to consume energy from the battery
    public synchronized double consumeEnergy(double demand) {
    	if (demand < 0) {
            throw new IllegalArgumentException("Energy demand cannot be negative.");
        }
        if (currentCharge >= demand) {
            currentCharge -= demand;
            // Logging successful energy consumption
            SystemLogger.logEvent("Battery \"" + name + "\" provided " + demand + " kWh. Remaining charge: " + currentCharge + " kWh.");
            return demand; // Fully meets the demand
        } else {
            double provided = currentCharge;
            currentCharge = 0; // Battery is drained
            
            // Logging partial energy provision
            SystemLogger.logEvent("Battery \"" + name + "\" drained. Provided only " + provided + " kWh. Remaining charge: " + currentCharge + " kWh.");
            return provided; // Provides whatever is available
        }
    }

    public double getCurrentCharge() {
        return currentCharge;
    }

    public double getMaxCapacity() {
        return maxCapacity;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + ": " + currentCharge + "/" + maxCapacity + " kWh";
    }
}
