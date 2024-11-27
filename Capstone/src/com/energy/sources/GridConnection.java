package com.energy.sources;

import java.io.Serializable;
import com.energy.logging.SystemLogger;

public class GridConnection extends EnergySource implements Serializable {
    private static final long serialVersionUID = 1L;

    private double energyBalance; // Tracks net energy imported/exported

    public GridConnection(String name) {
        super(name, Double.MAX_VALUE); // Infinite capacity for grid
        this.energyBalance = 0;
        
     // Log grid connection initialization
        SystemLogger.logEvent("GridConnection \"" + name + "\" initialized with infinite capacity.");
    }

    @Override
    public double generateEnergy(double hours) {
        // Grid doesn't "generate" energy, but it can provide as needed
        System.out.println(name + " is always available to supply energy.");
     // Grid doesn't "generate" energy, but it can provide as needed
        SystemLogger.logEvent("GridConnection \"" + name + "\" is always available to supply energy.");
        return 0; // Return 0 as it doesn't generate energy by itself
    }

    // Import energy from the grid to meet demand
    public double importEnergy(double amount) {
    	if (amount < 0) {
            throw new IllegalArgumentException("Energy import amount cannot be negative.");
        }
        energyBalance += amount;
     // Log energy import
        SystemLogger.logEvent("GridConnection \"" + name + "\" imported " + amount + " kWh. Net balance: " + energyBalance + " kWh.");
        return amount; // Always fulfills the requested amount
    }

    // Export surplus energy to the grid
    public void exportEnergy(double amount) {
    	if (amount < 0) {
            throw new IllegalArgumentException("Energy export amount cannot be negative.");
        }
        energyBalance -= amount; // Decrease the balance to reflect export
        
     // Log energy export
        SystemLogger.logEvent("GridConnection \"" + name + "\" exported " + amount + " kWh. Net balance: " + energyBalance + " kWh.");
    }

    public double getEnergyBalance() {
        return energyBalance;
    }

    @Override
    public String toString() {
        return name + ": Net Energy Balance = " + energyBalance + " kWh";
    }
}
