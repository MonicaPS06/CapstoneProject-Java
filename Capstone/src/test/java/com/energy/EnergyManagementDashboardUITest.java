package test.java.com.energy;

import org.junit.jupiter.api.*;

import com.energy.ui.EnergyManagementDashboardUI;
import com.energy.ui.EnergySourceUI;

import static org.junit.jupiter.api.Assertions.*;

class EnergyManagementDashboardUITest {

    private EnergyManagementDashboardUI dashboardUI;
    private EnergySourceUI mockEnergySourceUI;

    @BeforeEach
    void setUp() {
        // Create a mock or dummy EnergySourceUI instance
        mockEnergySourceUI = new EnergySourceUI();
        dashboardUI = new EnergyManagementDashboardUI(mockEnergySourceUI);
    }

    @Test
    void testDashboardInitialization() {
        assertDoesNotThrow(() -> dashboardUI.setVisible(true));
    }

    @Test
    void testShowSmartObjectUI() {
        // Simulate clicking "Manage Smart Objects"
        assertDoesNotThrow(() -> {
            dashboardUI.showSmartObjectUI();
        });
    }

    @Test
    void testShowEnergySourceUI() {
        // Simulate clicking "Manage Energy Sources"
        assertDoesNotThrow(() -> {
            dashboardUI.showEnergySourceUI();
        });
    }

    @Test
    void testShowSystemSummaryWindow() {
        // Simulate clicking "Show System Summary"
        assertDoesNotThrow(() -> {
            dashboardUI.showSystemSummaryWindow();
        });
    }

    @Test
    void testHandleCriticalErrorDuringInitialization() {
        // Use a lambda-friendly approach
        assertThrows(RuntimeException.class, () -> {
            EnergyManagementDashboardUI faultyDashboard = new EnergyManagementDashboardUI(null); // Pass null dependency
            faultyDashboard.setVisible(true); // Simulate visibility with a faulty dashboard
        });
    }

}
