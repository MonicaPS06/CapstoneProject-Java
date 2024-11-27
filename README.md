Capstone: Energy Management System
Project Description
This capstone project simulates an Energy Management System for smart homes. The system balances energy supply and demand by integrating smart objects (e.g., lights, thermostats) and energy sources (e.g., solar panels, batteries). A dynamic dashboard allows for managing and monitoring the system in real time.
Contributors
Role	Name	Responsibilities
Smart Object Specialist	Daranya	(7222079)Simulating smart objects and their UI
Energy Source Specialist	Arthi (7216696)	Energy source simulation and management UI.
Management Specialist	Monica(7221964)	System management, logging, and exception handling.

Key Features
1.	Smart Object Management:
o	Simulates devices like smart lights and smart thermostats.
o	Activates, deactivates, and adjusts device settings.
2.	Energy Source Management:
o	Models energy sources like solar panels, batteries, and grid connections.
o	Simulates energy generation, storage, and depletion.
3.	System Management Dashboard:
o	Provides a user interface for viewing and managing energy consumption.
o	Tracks energy flow and system-wide performance metrics.
4.	Logging and Exception Handling:
o	Records significant events and handles invalid operations.
5.	Testing and Integration:
o	Includes unit tests to ensure module functionality.

Src//
├── com.energy.logging           # Logging utility
│   └── SystemLogger.java        # Handles system logs
│
├── com.energy.main              # Main application logic
│   └── (Entry point classes)
│
├── com.energy.sources           # Energy source simulations
│   ├── Battery.java             # Simulates battery energy storage
│   ├── EnergySource.java        # Base class for energy sources
│   ├── GridConnection.java      # Simulates grid connection energy
│   ├── SolarPanel.java          # Simulates solar panel energy
│
├── com.energy.ui                # User interface classes
│   ├── EnergySourceUI.java      # UI for managing energy sources
│   ├── SmartObjectUI.java       # UI for managing smart objects
│   ├── SystemSummaryWindow.java # Displays summary of system state
│
└── test.java.com.energy         # Unit tests
    ├── SystemLoggerTest.java    # Test for logging functionality
    ├── SmartObjectsSubmoduleTest.java  # Tests for smart devices
    ├── EnergySourcesSubmoduleTest.java # Tests for energy sources
    ├── EnergyManagementDashboardUITest.java # Tests for the UI

Testing Instructions
1.	Open the test.java.com.energy package in your IDE.
2.	Run the unit tests using JUnit 5.
3.	Verify that all test cases pass successfully.

