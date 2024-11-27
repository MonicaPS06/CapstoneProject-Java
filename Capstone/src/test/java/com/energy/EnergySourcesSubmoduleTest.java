package test.java.com.energy;

import org.junit.jupiter.api.*;

import com.energy.sources.Battery;
import com.energy.sources.EnergySource;
import com.energy.sources.GridConnection;
import com.energy.sources.SolarPanel;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

class EnergySourcesSubmoduleTest {

    private List<EnergySource> energySources;
    private Battery battery;

    @BeforeEach
    void setUp() {
        energySources = new ArrayList<>();
        energySources.add(new SolarPanel("Solar1", 50)); // Generates 50 kWh/hour
        energySources.add(new GridConnection("Grid1")); // Backup energy source
        battery = new Battery("MainBattery", 200); // Battery with 200 kWh capacity
    }

    @Test
    void testAddEnergySource() {
        energySources.add(new SolarPanel("Solar2", 30));
        assertEquals(3, energySources.size());
    }

    @Test
    void testGenerateEnergyFromMultipleSources() {
        energySources.add(new SolarPanel("Solar2", 30)); // Another SolarPanel generating 30 kWh/hour

        double totalEnergy = energySources.stream()
                                          .filter(source -> source instanceof SolarPanel) // Only SolarPanels
                                          .mapToDouble(source -> source.generateEnergy(2)) // 2 hours
                                          .sum();

        // Validate the total energy
        assertEquals(160.0, totalEnergy, 0.001); // 50 + 30 kWh/hour * 2 hours = 160 kWh
    }



    @Test
    void testStoreGeneratedEnergyInBattery() {
        double generatedEnergy = energySources.get(0).generateEnergy(2); // SolarPanel for 2 hours
        double overflow = battery.storeEnergy(generatedEnergy);
        assertEquals(0, overflow);
        assertEquals(100, battery.getCurrentCharge());
    }

    @Test
    void testExceedBatteryCapacity() {
        double generatedEnergy = 250; // Simulate excess energy
        double overflow = battery.storeEnergy(generatedEnergy);
        assertEquals(50, overflow); // 250 - 200 = 50
        assertEquals(200, battery.getCurrentCharge());
    }

    @Test
    void testEnergySourceIntegration() {
        energySources.add(new SolarPanel("Solar3", 60));
        double totalGenerated = energySources.stream()
                                             .mapToDouble(source -> source.generateEnergy(1))
                                             .sum();
        double overflow = battery.storeEnergy(totalGenerated);
        assertEquals(0, overflow); // Ensure battery stores energy without overflow
    }
}
