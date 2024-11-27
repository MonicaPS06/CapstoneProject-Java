
package com.energy.sources;

import java.io.Serializable;

public abstract class EnergySource implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String name;
    protected double capacity; // Capacity is meaningful for batteries or finite sources

    public EnergySource(String name, double capacity) {
    	if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Energy source name cannot be null or empty.");
        }
        if (capacity < 0) {
            throw new IllegalArgumentException("Capacity must be non-negative.");
        }
        this.name = name;
        this.capacity = capacity;
    }

    // Abstract method for generating or supplying energy
    public abstract double generateEnergy(double hours);

    public String getName() {
        return name;
    }

    public double getCapacity() {
        return capacity;
    }

    @Override
    public String toString() {
        return name + ": Capacity = " + capacity + " kWh";
    }
    
    public String getStatus() {
        return name + ": Capacity = " + capacity + " kWh";
    }

}
