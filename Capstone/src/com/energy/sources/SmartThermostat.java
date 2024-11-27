package com.energy.sources;

import java.io.Serializable;
import java.util.List;
import com.energy.logging.SystemLogger;

public class SmartThermostat extends SmartObject implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Battery> batteries; // Shared list of batteries

    public SmartThermostat(String name, double energyDemand, List<Battery> batteries, GridConnection gridConnection) {
        super(name, energyDemand, null, gridConnection); // Pass null for single battery
        //this.batteries = batteries; // Assign shared battery list
        
        // Log initialization
        SystemLogger.logEvent("SmartThermostat \"" + name + "\" initialized with energy demand: " + energyDemand + " kWh.");
    }

    @Override
    public double consumeEnergy(double hours, List<Battery> batteries) {
    	this.batteries = batteries; 
        double totalConsumed = 0; // Total energy consumed
        double energyPerSecond = getEnergyDemand(); // Energy demand per hour

        long totalSeconds = (long) (hours * 1); // 1 hour = 1 second in simulation
        for (int i = 0; i < totalSeconds; i++) {
            try {
                Thread.sleep(1000); // Simulate 1-second interval

                double demand = energyPerSecond; // Energy demanded in this second
                double consumedFromBattery = 0;

                // Attempt to consume energy from the list of batteries
                synchronized (batteries) {
                    for (Battery battery : batteries) {
                        synchronized (battery) {
                            double consumed = battery.consumeEnergy(demand);
                            consumedFromBattery += consumed;
                            demand -= consumed;

                            if (demand <= 0) break; // Stop if demand is fully met
                        }
                    }
                }
                
             // Log battery consumption
                SystemLogger.logEvent("SmartThermostat \"" + getName() + "\" consumed " + consumedFromBattery + " kWh from batteries.");

                // Fallback to grid if batteries cannot meet the demand
                if (demand > 0 && gridConnection != null) {
                    gridConnection.importEnergy(demand);
                    totalConsumed += demand;
                    System.out.println(getName() + " consumed " + demand + " kWh from the grid.");
                    
                 // Log grid consumption
                    SystemLogger.logEvent("SmartThermostat \"" + getName() + "\" consumed " + demand + " kWh from the grid.");
                } else if (demand > 0) {
                    System.out.println(getName() + ": Unable to meet remaining demand of " + demand + " kWh.");
                 // Log not met demand
                    SystemLogger.logError("SmartThermostat \"" + getName() + "\": Unable to meet remaining demand of " + demand + " kWh.");
                }

                totalConsumed += consumedFromBattery;

                // Log the consumption for this interval
                System.out.println(getName() + " consumed " + consumedFromBattery + " kWh from batteries so far.");
            } catch (InterruptedException e) {
                System.err.println("Simulation interrupted for " + getName() + ": " + e.getMessage());
             // Log interruptions
                SystemLogger.logError("Simulation interrupted for SmartThermostat \"" + getName() + "\": " + e.getMessage());
                break;
            }
        }

        return totalConsumed;
    }
}
