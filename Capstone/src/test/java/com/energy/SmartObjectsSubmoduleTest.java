package test.java.com.energy;

import org.junit.jupiter.api.*;

import com.energy.sources.Battery;
import com.energy.sources.GridConnection;
import com.energy.sources.SmartLight;
import com.energy.sources.SmartThermostat;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

class SmartObjectsSubmoduleTest {

    private SmartLight smartLight;
    private SmartThermostat smartThermostat;
    private List<Battery> batteries;

    @BeforeEach
    void setUp() {
        batteries = new ArrayList<>();
        batteries.add(new Battery("Battery1", 100));
        batteries.add(new Battery("Battery2", 200));
        smartLight = new SmartLight("SmartLight", 10, batteries, null); // Demand: 10 kWh/hour
        smartThermostat = new SmartThermostat("Thermostat", 20, batteries, null); // Demand: 20 kWh/hour
    }

    @Test
    void testSmartLightConsumesEnergy() {
        batteries.get(0).storeEnergy(50);
        double consumed = smartLight.consumeEnergy(1, batteries); // 1 hour
        assertEquals(10, consumed); // 10 kWh demand
        assertEquals(40, batteries.get(0).getCurrentCharge());
    }

    @Test
    void testSmartThermostatConsumesEnergy() {
        batteries.get(1).storeEnergy(100);
        double consumed = smartThermostat.consumeEnergy(2, batteries); // 2 hours
        assertEquals(40, consumed); // 20 kWh/hour * 2 hours
        assertEquals(60, batteries.get(1).getCurrentCharge());
    }

    @Test
    void testFallbackToGridWhenBatteriesDepleted() {
        batteries.clear(); // Simulate no batteries available
        GridConnection grid = new GridConnection("MainGrid");
        smartLight = new SmartLight("SmartLight", 10, batteries, grid);

        double consumed = smartLight.consumeEnergy(1, batteries); // 1 hour
        assertEquals(10, consumed); // Consumes directly from the grid
    }

    @Test
    void testIntegrationWithMultipleBatteries() {
        batteries.get(0).storeEnergy(10);
        batteries.get(1).storeEnergy(20);

        double consumed = smartThermostat.consumeEnergy(1, batteries); // 1 hour
        assertEquals(20, consumed); // Thermostat's demand
        assertEquals(10, batteries.get(1).getCurrentCharge());
    }

    @Test
    void testSmartLightOverDemand() {
        batteries.get(0).storeEnergy(5); // Less than demand
        double consumed = smartLight.consumeEnergy(1, batteries); // 1 hour
        assertEquals(5, consumed); // Only consumes available energy
        assertEquals(0, batteries.get(0).getCurrentCharge());
    }
}
