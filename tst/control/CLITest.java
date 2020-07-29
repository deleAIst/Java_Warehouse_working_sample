package control;

import model.businessLogice.BusinessLogic;
import model.businessLogice.BusinessLogiceException;
import model.storageContract.administration.Customer;
import model.storageContract.administration.CustomerException;
import model.storageContract.cargo.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import routing.event.*;
import routing.eventHandler.EventHandler;
import routing.eventListener.*;
import view.*;
import view.Observer;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CLITest {

    BusinessLogic businessLogic;
    CLI cli;
    EventHandler<CargoDeleteEvent> eventHandlerCargoDelete;
    EventHandler<CargoLiquidInsertEvent> eventHandlerCargoLiquid;
    EventHandler<CargoMixedInsertEvent> cargoMixedInsertEventEventHandler;
    EventHandler<CargoUnitisedInsertEvent> cargoUnitisedInsertEventEventHandler;
    EventHandler<CargoTypeDisplayEvent> cargoTypeDisplayEventEventHandler;
    EventHandler<HazardDisplayEvent> hazardDisplayEventEventHandler;
    EventHandler<CustomerDeleteEvent> customerDeleteEventHandler;
    EventHandler<CustomerInsertEvent> customerInsertEventHandler;
    EventHandler<CustomerDisplayEvent> customerDisplayEventEventHandler;
    EventHandler<ConsoleOutputEvent> consoleOutputEventEventHandler;
    EventHandler<DateOfInspectionEvent> dateOfInspectionEventHandler;
    EventHandler<PersistentEvent> persistentEventEventHandler;
    EventHandler<ObserverAddEvent> observerAddEventEventHandler;
    EventHandler<ObserverRemoveEvent> removeEventEventHandler;
    EventHandler<HazardOutputEvent> hazardOutputEventHandler;
    EventHandler<CargoPersistentEvent> cargoPersistensEventHandler;
    private ControlModel controlModel;
    InputStream sysInBackup;
    private ViewCLI view;
    private PrintStream old;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private String mainMenue;

    @BeforeEach
    public void setup() {
        this.sysInBackup = System.in;
        this.businessLogic = new BusinessLogic(20);
        this.controlModel = new ControlModel(this.businessLogic);
        System.setOut(new PrintStream(outContent));
        this.view = new ViewCLI(System.out);

        this.eventHandlerCargoDelete = new EventHandler<>();
        this.eventHandlerCargoLiquid = new EventHandler<>();
        this.cargoMixedInsertEventEventHandler = new EventHandler<>();
        this.cargoUnitisedInsertEventEventHandler = new EventHandler<>();
        this.cargoTypeDisplayEventEventHandler = new EventHandler<>();
        this.hazardDisplayEventEventHandler = new EventHandler<>();
        this.customerDeleteEventHandler = new EventHandler<>();
        this.customerInsertEventHandler = new EventHandler<>();
        this.customerDisplayEventEventHandler = new EventHandler<>();
        this.consoleOutputEventEventHandler = new EventHandler<>();
        this.dateOfInspectionEventHandler = new EventHandler<>();
        this.persistentEventEventHandler = new EventHandler<>();
        this.observerAddEventEventHandler = new EventHandler<>();
        this.removeEventEventHandler = new EventHandler<>();
        this.hazardOutputEventHandler = new EventHandler<>();
        this.cargoPersistensEventHandler = new EventHandler<>();


        HazardOutputEventListener hazardOutputEventListener = new HazardOutputEventListener(view);
        this.hazardOutputEventHandler.add(hazardOutputEventListener);
        CustomerInsertEventListener customerInsertEventListener = new CustomerInsertEventListener(this.controlModel);
        CargoUnitisedInsertEventListener cargoUnitisedInsertEventListener = new CargoUnitisedInsertEventListener(this.controlModel);
        CargoMixedInsertEventListener cargoMixedInsertEventListener = new CargoMixedInsertEventListener(this.controlModel);
        CargoLiquidInsertEventListener cargoLiquidInsertEventListener = new CargoLiquidInsertEventListener(this.controlModel);
        CustomerDisplayEventListener customerDisplayEventListener = new CustomerDisplayEventListener(this.controlModel, view);
        CargoTypeDisplayEventListener cargoTypeDisplayEventListener = new CargoTypeDisplayEventListener(this.controlModel, this.view);
        HazardDisplayEventListener hazardDisplayEventListener = new HazardDisplayEventListener(this.controlModel, hazardOutputEventHandler);
        CargoDeleteEventListener cargoDeleteEventListener = new CargoDeleteEventListener(this.controlModel);
        CustomerDeleteEventListener customerDeleteEventListener = new CustomerDeleteEventListener(this.controlModel);
        DateOfInspectionEventListener dateOfInspectionEventListener = new DateOfInspectionEventListener(this.controlModel);
        ConsoleOutputEventListener consoleOutputEventListener = new ConsoleOutputEventListener(this.view);
        PersistendEventListener persistendEventListener = new PersistendEventListener(this.controlModel);
        ObserverAddEventListener observerAddEventListener = new ObserverAddEventListener(this.controlModel);
        ObserverRemoveEventListener observerRemoveEventListener = new ObserverRemoveEventListener(this.controlModel);
        CargoPersistendEventListener cargoPersistendEventListener = new CargoPersistendEventListener(this.controlModel);

        this.eventHandlerCargoDelete.add(cargoDeleteEventListener);
        this.eventHandlerCargoLiquid.add(cargoLiquidInsertEventListener);
        this.cargoMixedInsertEventEventHandler.add(cargoMixedInsertEventListener);
        this.cargoUnitisedInsertEventEventHandler.add(cargoUnitisedInsertEventListener);
        this.cargoTypeDisplayEventEventHandler.add(cargoTypeDisplayEventListener);
        this.hazardDisplayEventEventHandler.add(hazardDisplayEventListener);
        this.customerDeleteEventHandler.add(customerDeleteEventListener);
        this.customerDisplayEventEventHandler.add(customerDisplayEventListener);
        this.customerInsertEventHandler.add(customerInsertEventListener);
        this.consoleOutputEventEventHandler.add(consoleOutputEventListener);
        this.dateOfInspectionEventHandler.add(dateOfInspectionEventListener);
        this.persistentEventEventHandler.add(persistendEventListener);
        this.observerAddEventEventHandler.add(observerAddEventListener);
        this.removeEventEventHandler.add(observerRemoveEventListener);
        this.cargoPersistensEventHandler.add(cargoPersistendEventListener);
        this.mainMenue = "Sie habe die Auswahl zwischen 7 Modusen.\nEinfügemodus :c\nLöschmodus :d\nAnzeigemodus :r\n" +
                "Änderungsmodus :u\nPersistenzmodus :p\nKonfigurationsmodus :config\nBeenden :exit\nBitte geben sie jetzt ihren Modus ein mit dem Kürzel\n";

    }

    @AfterEach
    public void setSystemInBack() throws IOException {
        Files.deleteIfExists(Paths.get("testfile.txt"));
        Files.deleteIfExists(Paths.get("testfileXML.xml"));
        Files.deleteIfExists(Paths.get("CargoSave.txt"));
        System.setIn(this.sysInBackup);
        System.setOut(originalOut);
    }

    @Test
    public void insortModeCall_good() throws UnsupportedEncodingException, BusinessLogiceException, CustomerException, CargoException {
        //https://stackoverflow.com/questions/6415728/junit-testing-with-simulated-user-input
        ByteArrayInputStream in = new ByteArrayInputStream(":c\nexit".getBytes());

        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue + "Sie sind im Einfügemodus:\nWollen sie einen Customer Eingben dan 1 oder ein Cargo dan 2 oder exit\n" + mainMenue,
                outContent.toString());
    }

    @Test
    public void deleteCall_good() throws UnsupportedEncodingException, BusinessLogiceException, CustomerException, CargoException {
        //https://stackoverflow.com/questions/6415728/junit-testing-with-simulated-user-input
        ByteArrayInputStream in = new ByteArrayInputStream(":d\nexit".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue + "Sie sind im Löschenmodus:\nWollen sie einen Customer Löschen dan 1 oder ein Cargo dan 2\n" + mainMenue,
                outContent.toString());
    }

    @Test
    public void displayModeCall_good() throws UnsupportedEncodingException, BusinessLogiceException, CustomerException, CargoException {
        //https://stackoverflow.com/questions/6415728/junit-testing-with-simulated-user-input
        ByteArrayInputStream in = new ByteArrayInputStream(":r\nexit".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue + "Sie sind im Anzeigemodus:\n" +
                        "Mit der 1 Anzeige der Kunden mit der Anzahl eingelagerter Frachtstücke\n" +
                        "Mit der 2 Anzeige der Frachtstücke\n" +
                        "Mit der 3 Anzeige der vorhandenen bzw. nicht vorhandenen Gefahrenstoffe\n" +
                        "Mit exit kommen sie zurück in das Haupmenü\n" + mainMenue,
                outContent.toString());
    }

    @Test
    public void persistensModeCall_good() throws UnsupportedEncodingException, BusinessLogiceException, CustomerException, CargoException {
        //https://stackoverflow.com/questions/6415728/junit-testing-with-simulated-user-input
        ByteArrayInputStream in = new ByteArrayInputStream(":p\nexit".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Persistenenzmodus:\n" +
                        "Mit der 1 Speichern mit JOS\n" +
                        "Mit der 2 Speichern mit JBP\n" +
                        "Mit der 3 Laden mit JOS\n" +
                        "Mit der 4 Laden mit JBP\n" +
                        "Mit der 5 Speichern eines Cargos\n" +
                        "Mit der 6 Laden eines Cargos\n" +
                        "Mit exit kommen sie zurück in das Haupmenü\n"+
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void configModeCall_good() throws UnsupportedEncodingException, BusinessLogiceException, CustomerException, CargoException {
        //https://stackoverflow.com/questions/6415728/junit-testing-with-simulated-user-input
        ByteArrayInputStream in = new ByteArrayInputStream(":config\nexit".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue + "Sie sind im Configmodus:\n" +
                        "Mit der 1 add von Beobachtern\n" +
                        "Mit der 2 remove von Beobachtern\n" +
                        "Mit exit kommen sie zurück in das Haupmenü\n" + mainMenue,
                outContent.toString());
    }

    @Test
    public void modificationModeCall_good() throws UnsupportedEncodingException, BusinessLogiceException, CustomerException, CargoException {
        //https://stackoverflow.com/questions/6415728/junit-testing-with-simulated-user-input
        ByteArrayInputStream in = new ByteArrayInputStream(":u\nexit".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Änderungsmodus:\n" +
                        "Bitte geben sie den Lagerplatz an wo das Inspektionsdatum auf den aktuellen Zeitpunkt gesetzt werden soll.\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void insortModeCallLowerCase_good() throws UnsupportedEncodingException, BusinessLogiceException, CustomerException, CargoException {
        //https://stackoverflow.com/questions/6415728/junit-testing-with-simulated-user-input
        ByteArrayInputStream in = new ByteArrayInputStream(":C\nexit".getBytes());

        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue + "Sie sind im Einfügemodus:\nWollen sie einen Customer Eingben dan 1 oder ein Cargo dan 2 oder exit\n" + mainMenue,
                outContent.toString());
    }

    @Test
    public void deleteCallLowerCase_good() throws UnsupportedEncodingException, BusinessLogiceException, CustomerException, CargoException {
        //https://stackoverflow.com/questions/6415728/junit-testing-with-simulated-user-input
        ByteArrayInputStream in = new ByteArrayInputStream(":D\nexit".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue + "Sie sind im Löschenmodus:\nWollen sie einen Customer Löschen dan 1 oder ein Cargo dan 2\n" + mainMenue,
                outContent.toString());
    }

    @Test
    public void displayModeCallLowerCase_good() throws UnsupportedEncodingException, BusinessLogiceException, CustomerException, CargoException {
        //https://stackoverflow.com/questions/6415728/junit-testing-with-simulated-user-input
        ByteArrayInputStream in = new ByteArrayInputStream(":R\nexit".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue + "Sie sind im Anzeigemodus:\n" +
                        "Mit der 1 Anzeige der Kunden mit der Anzahl eingelagerter Frachtstücke\n" +
                        "Mit der 2 Anzeige der Frachtstücke\n" +
                        "Mit der 3 Anzeige der vorhandenen bzw. nicht vorhandenen Gefahrenstoffe\n" +
                        "Mit exit kommen sie zurück in das Haupmenü\n" + mainMenue,
                outContent.toString());
    }

    @Test
    public void persistensModeCallLowerCase_good() throws UnsupportedEncodingException, BusinessLogiceException, CustomerException, CargoException {
        //https://stackoverflow.com/questions/6415728/junit-testing-with-simulated-user-input
        ByteArrayInputStream in = new ByteArrayInputStream(":P\nexit".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Persistenenzmodus:\n" +
                        "Mit der 1 Speichern mit JOS\n" +
                        "Mit der 2 Speichern mit JBP\n" +
                        "Mit der 3 Laden mit JOS\n" +
                        "Mit der 4 Laden mit JBP\n" +
                        "Mit der 5 Speichern eines Cargos\n" +
                        "Mit der 6 Laden eines Cargos\n" +
                        "Mit exit kommen sie zurück in das Haupmenü\n"+
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void configModeCallLowerCase_good() throws UnsupportedEncodingException, BusinessLogiceException, CustomerException, CargoException {
        //https://stackoverflow.com/questions/6415728/junit-testing-with-simulated-user-input
        ByteArrayInputStream in = new ByteArrayInputStream(":CONFIG\nexit".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue + "Sie sind im Configmodus:\n" +
                        "Mit der 1 add von Beobachtern\n" +
                        "Mit der 2 remove von Beobachtern\n" +
                        "Mit exit kommen sie zurück in das Haupmenü\n" + mainMenue,
                outContent.toString());
    }

    @Test
    public void modificationModeCallLowerCase_good() throws UnsupportedEncodingException, BusinessLogiceException, CustomerException, CargoException {
        //https://stackoverflow.com/questions/6415728/junit-testing-with-simulated-user-input
        ByteArrayInputStream in = new ByteArrayInputStream(":U\nexit".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Änderungsmodus:\n" +
                        "Bitte geben sie den Lagerplatz an wo das Inspektionsdatum auf den aktuellen Zeitpunkt gesetzt werden soll.\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void modeCall_bad() throws UnsupportedEncodingException, BusinessLogiceException, CustomerException, CargoException {
        //https://stackoverflow.com/questions/6415728/junit-testing-with-simulated-user-input
        ByteArrayInputStream in = new ByteArrayInputStream(":w".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Das eingegeben Kommentar ist Falsch.\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void insortModeCall_bad() throws UnsupportedEncodingException, BusinessLogiceException, CustomerException, CargoException {
        //https://stackoverflow.com/questions/6415728/junit-testing-with-simulated-user-input
        ByteArrayInputStream in = new ByteArrayInputStream("c".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Das eingegeben Kommentar ist Falsch.\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void deleteCall_bad() throws UnsupportedEncodingException, BusinessLogiceException, CustomerException, CargoException {
        //https://stackoverflow.com/questions/6415728/junit-testing-with-simulated-user-input
        ByteArrayInputStream in = new ByteArrayInputStream("d".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Das eingegeben Kommentar ist Falsch.\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void displayModeCall_bad() throws UnsupportedEncodingException, BusinessLogiceException, CustomerException, CargoException {
        //https://stackoverflow.com/questions/6415728/junit-testing-with-simulated-user-input
        ByteArrayInputStream in = new ByteArrayInputStream("r".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Das eingegeben Kommentar ist Falsch.\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void persistensModeCall_bad() throws UnsupportedEncodingException, BusinessLogiceException, CustomerException, CargoException {
        //https://stackoverflow.com/questions/6415728/junit-testing-with-simulated-user-input
        ByteArrayInputStream in = new ByteArrayInputStream("p".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Das eingegeben Kommentar ist Falsch.\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void configModeCall_bad() throws UnsupportedEncodingException, BusinessLogiceException, CustomerException, CargoException {
        //https://stackoverflow.com/questions/6415728/junit-testing-with-simulated-user-input
        ByteArrayInputStream in = new ByteArrayInputStream("config".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Das eingegeben Kommentar ist Falsch.\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void modificationModeCall_bad() throws UnsupportedEncodingException, BusinessLogiceException, CustomerException, CargoException {
        //https://stackoverflow.com/questions/6415728/junit-testing-with-simulated-user-input
        ByteArrayInputStream in = new ByteArrayInputStream("u".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Das eingegeben Kommentar ist Falsch.\n" +
                        mainMenue,
                outContent.toString());
    }

    //=========================================== Insert Mode ====================================================//

    @Test
    public void insertCustomerCall_good() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":c\n1\nDennis".getBytes());

        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Einfügemodus:\nWollen sie einen Customer Eingben dan 1 oder ein Cargo dan 2 oder exit\n" +
                        "Sie wollen einen neuen Customer hinzufügen Bitte geben sie den Name des Customers an.\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void insertCustomerOrCargoCall_bad() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":c\n3".getBytes());

        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Einfügemodus:\nWollen sie einen Customer Eingben dan 1 oder ein Cargo dan 2 oder exit\n" +
                        "Das eingegeben Kommentar ist Falsch.\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void insertCustomerAtBusinessLogic_good() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":c\n1\nDennis".getBytes());

        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals("Dennis", businessLogic.getCustomer("Dennis").getName());
    }

    @Test
    public void insertCustomerAtBusinessLogic_badErrorString() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":c\n1\n\n".getBytes());

        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Einfügemodus:\nWollen sie einen Customer Eingben dan 1 oder ein Cargo dan 2 oder exit\n" +
                        "Sie wollen einen neuen Customer hinzufügen Bitte geben sie den Name des Customers an.\n" +
                        "Es wurde null übergebn das ist nicht erlaubt.\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void insertCustomerAtBusinessLogic_bad() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":c\n1\n".getBytes());

        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(0, businessLogic.getAlleCustomer().size());
    }

    @Test
    public void insertCargoCall_good() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":c\n2\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Einfügemodus:\nWollen sie einen Customer Eingben dan 1 oder ein Cargo dan 2 oder exit\n" +
                        "Bitte geben sie das Cargo in dieser Form an:\n" +
                        "[Frachttyp] [Kundenname] [Wert] [Einlagerungsdauer in Sekunden] " +
                        "[kommaseparierte Gefahrenstoffe, einzelnes Komma für keine] " +
                        "[[zerbrechlich (y/n)] [unter Druck (y/n)] [fest (y/n)]] \n" +
                        "Keine Eingabe ist nicht Erlaubt\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void insertCargoFalseCargoType() throws BusinessLogiceException, CustomerException, CargoException {
        this.businessLogic.putCustomer("Dennis");
        ByteArrayInputStream in = new ByteArrayInputStream(":c\n2\nUnitisCargo Dennis 23.5 23459 , N\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        Cargo[] cargos = businessLogic.getAlleCargo();
        assertEquals(this.mainMenue +
                        "Sie sind im Einfügemodus:\nWollen sie einen Customer Eingben dan 1 oder ein Cargo dan 2 oder exit\n" +
                        "Bitte geben sie das Cargo in dieser Form an:\n" +
                        "[Frachttyp] [Kundenname] [Wert] [Einlagerungsdauer in Sekunden] " +
                        "[kommaseparierte Gefahrenstoffe, einzelnes Komma für keine] " +
                        "[[zerbrechlich (y/n)] [unter Druck (y/n)] [fest (y/n)]] \n" +
                        "Falscher Cargo Type Eingeben.\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void insertCargoAtBusinessLogic_good() throws BusinessLogiceException, CustomerException, CargoException {
        this.businessLogic.putCustomer("Dennis");
        ByteArrayInputStream in = new ByteArrayInputStream(":c\n2\nLiquidCargo Dennis 23.5 2345 explosive Y\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        Cargo[] cargos = businessLogic.getAlleCargo();
        assertEquals("Dennis", cargos[0].getOwner().getName());
    }

    @Test
    public void insertCargoAtBusinessLogicMoreThenOnHazards_good() throws BusinessLogiceException, CustomerException, CargoException {
        this.businessLogic.putCustomer("Dennis");
        ByteArrayInputStream in = new ByteArrayInputStream(":c\n2\nLiquidCargo Dennis 23.5 2345 explosive, flammable, radioactive, toxic Y\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        Cargo[] cargos = businessLogic.getAlleCargo();
        assertEquals(4, cargos[0].getHazards().size());
    }

    @Test
    public void insertCargoAtBusinessLogicSameHazards_good() throws BusinessLogiceException, CustomerException, CargoException {
        this.businessLogic.putCustomer("Dennis");
        ByteArrayInputStream in = new ByteArrayInputStream(":c\n2\nLiquidCargo Dennis 23.5 2345 explosive explosive  Y\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        Cargo[] cargos = businessLogic.getAlleCargo();
        assertEquals(1, cargos[0].getHazards().size());
    }

    @Test
    public void insertCargoAtBusinessLogicBlankAtHazardAndtrade_good() throws BusinessLogiceException, CustomerException, CargoException {
        this.businessLogic.putCustomer("Dennis");
        ByteArrayInputStream in = new ByteArrayInputStream(":c\n2\nLiquidCargo Dennis 23.5 2345 explosive , flammable , radioactive , toxic , Y\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        Cargo[] cargos = businessLogic.getAlleCargo();
        assertEquals(4, cargos[0].getHazards().size());
    }

    @Test
    public void insertCargoNotHazardType_goodError() throws BusinessLogiceException, CustomerException, CargoException {
        this.businessLogic.putCustomer("Dennis");
        ByteArrayInputStream in = new ByteArrayInputStream(":c\n2\nLiquidCargo Dennis 23.5 2345 explosve  Y\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        Cargo[] cargos = businessLogic.getAlleCargo();
        assertEquals(this.mainMenue +
                        "Sie sind im Einfügemodus:\nWollen sie einen Customer Eingben dan 1 oder ein Cargo dan 2 oder exit\n" +
                        "Bitte geben sie das Cargo in dieser Form an:\n" +
                        "[Frachttyp] [Kundenname] [Wert] [Einlagerungsdauer in Sekunden] " +
                        "[kommaseparierte Gefahrenstoffe, einzelnes Komma für keine] " +
                        "[[zerbrechlich (y/n)] [unter Druck (y/n)] [fest (y/n)]] \n" +
                        "Falscher Hazard Type Eingeben.\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void insertCargoNotEnoughAtribut_goodError() throws BusinessLogiceException, CustomerException, CargoException {
        this.businessLogic.putCustomer("Dennis");
        ByteArrayInputStream in = new ByteArrayInputStream(":c\n2\nLiquidCargo Dennis 23.5 2345 explosive , flammable , radioactive , toxic, explosive , flammable , radioactive , toxic Y\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        Cargo[] cargos = businessLogic.getAlleCargo();
        assertEquals(this.mainMenue +
                        "Sie sind im Einfügemodus:\nWollen sie einen Customer Eingben dan 1 oder ein Cargo dan 2 oder exit\n" +
                        "Bitte geben sie das Cargo in dieser Form an:\n" +
                        "[Frachttyp] [Kundenname] [Wert] [Einlagerungsdauer in Sekunden] " +
                        "[kommaseparierte Gefahrenstoffe, einzelnes Komma für keine] " +
                        "[[zerbrechlich (y/n)] [unter Druck (y/n)] [fest (y/n)]] \n" +
                        "Zu viel Argument\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void insertCargoNotEnoughAtributNoGetCargoAtBusinessLogic_goodError() throws BusinessLogiceException, CustomerException, CargoException {
        this.businessLogic.putCustomer("Dennis");
        ByteArrayInputStream in = new ByteArrayInputStream(":c\n2\nLiquidCargo Dennis 23.5 2345 explosive , flammable , radioactive , toxic, explosive , flammable , radioactive , toxic Y\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        Cargo[] cargos = businessLogic.getAlleCargo();
        assertNull(cargos[0]);
    }

    @Test
    public void insertCargoChanceAttributes_goodError() throws BusinessLogiceException, CustomerException, CargoException {
        this.businessLogic.putCustomer("Dennis");
        ByteArrayInputStream in = new ByteArrayInputStream(":c\n2\nLiquidCargo Dennis 23.5 2345 Y\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        Cargo[] cargos = businessLogic.getAlleCargo();
        assertEquals(this.mainMenue +
                        "Sie sind im Einfügemodus:\nWollen sie einen Customer Eingben dan 1 oder ein Cargo dan 2 oder exit\n" +
                        "Bitte geben sie das Cargo in dieser Form an:\n" +
                        "[Frachttyp] [Kundenname] [Wert] [Einlagerungsdauer in Sekunden] " +
                        "[kommaseparierte Gefahrenstoffe, einzelnes Komma für keine] " +
                        "[[zerbrechlich (y/n)] [unter Druck (y/n)] [fest (y/n)]] \n" +
                        "Zu wenig Argument\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void insertCargoChanceAttributesNoGetCargoAtBusinessLogic_goodError() throws BusinessLogiceException, CustomerException, CargoException {
        this.businessLogic.putCustomer("Dennis");
        ByteArrayInputStream in = new ByteArrayInputStream(":c\n2\nLiquidCargo Dennis 23.5 2345 Y\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        Cargo[] cargos = businessLogic.getAlleCargo();
        assertNull(cargos[0]);
    }

    @Test
    public void insertCargoWrongOrder() throws BusinessLogiceException, CustomerException, CargoException {
        this.businessLogic.putCustomer("Dennis");
        ByteArrayInputStream in = new ByteArrayInputStream(":c\n2\nLiquidCargo 23.5 Dennis 2345 , Y\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Einfügemodus:\nWollen sie einen Customer Eingben dan 1 oder ein Cargo dan 2 oder exit\n" +
                        "Bitte geben sie das Cargo in dieser Form an:\n" +
                        "[Frachttyp] [Kundenname] [Wert] [Einlagerungsdauer in Sekunden] " +
                        "[kommaseparierte Gefahrenstoffe, einzelnes Komma für keine] " +
                        "[[zerbrechlich (y/n)] [unter Druck (y/n)] [fest (y/n)]] \n" +
                        "BigDecimal oder Duration Zahl war falsch.\n" +
                        mainMenue,
                outContent.toString());

    }

    @Test
    public void insertCargoWrongAttributesNoGetCargoAtBusinessLogicOrder() throws BusinessLogiceException, CustomerException, CargoException {
        this.businessLogic.putCustomer("Dennis");
        ByteArrayInputStream in = new ByteArrayInputStream(":c\n2\nLiquidCargo 23.5 Dennis 2345 , Y\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        Cargo[] cargos = businessLogic.getAlleCargo();
        assertNull(cargos[0]);

    }

    @Test
    public void insertCargoBigDecimalFalse() throws BusinessLogiceException, CustomerException, CargoException {
        this.businessLogic.putCustomer("Dennis");
        ByteArrayInputStream in = new ByteArrayInputStream(":c\n2\nLiquidCargo Dennis 23,5 2345 , Y\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        Cargo[] cargos = businessLogic.getAlleCargo();
        assertEquals(this.mainMenue +
                        "Sie sind im Einfügemodus:\nWollen sie einen Customer Eingben dan 1 oder ein Cargo dan 2 oder exit\n" +
                        "Bitte geben sie das Cargo in dieser Form an:\n" +
                        "[Frachttyp] [Kundenname] [Wert] [Einlagerungsdauer in Sekunden] " +
                        "[kommaseparierte Gefahrenstoffe, einzelnes Komma für keine] " +
                        "[[zerbrechlich (y/n)] [unter Druck (y/n)] [fest (y/n)]] \n" +
                        "BigDecimal oder Duration Zahl war falsch.\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void insertCargoDurationFalse() throws BusinessLogiceException, CustomerException, CargoException {
        this.businessLogic.putCustomer("Dennis");
        ByteArrayInputStream in = new ByteArrayInputStream(":c\n2\nLiquidCargo Dennis 23.5 2345.9 , Y\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Einfügemodus:\nWollen sie einen Customer Eingben dan 1 oder ein Cargo dan 2 oder exit\n" +
                        "Bitte geben sie das Cargo in dieser Form an:\n" +
                        "[Frachttyp] [Kundenname] [Wert] [Einlagerungsdauer in Sekunden] " +
                        "[kommaseparierte Gefahrenstoffe, einzelnes Komma für keine] " +
                        "[[zerbrechlich (y/n)] [unter Druck (y/n)] [fest (y/n)]] \n" +
                        "BigDecimal oder Duration Zahl war falsch.\n" +
                        mainMenue,
                outContent.toString());

    }

    @Test
    public void insertCargoOwnerFalse() throws BusinessLogiceException, CustomerException, CargoException {
        this.businessLogic.putCustomer("Dennis");
        ByteArrayInputStream in = new ByteArrayInputStream(":c\n2\nLiquidCargo Tim 23.5 23459 , Y\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        Cargo[] cargos = businessLogic.getAlleCargo();
        assertEquals(this.mainMenue +
                        "Sie sind im Einfügemodus:\nWollen sie einen Customer Eingben dan 1 oder ein Cargo dan 2 oder exit\n" +
                        "Bitte geben sie das Cargo in dieser Form an:\n" +
                        "[Frachttyp] [Kundenname] [Wert] [Einlagerungsdauer in Sekunden] " +
                        "[kommaseparierte Gefahrenstoffe, einzelnes Komma für keine] " +
                        "[[zerbrechlich (y/n)] [unter Druck (y/n)] [fest (y/n)]] \n" +
                        "Das Cargo hat keine Owner.\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void insertCargoTrueAndFalseFalse() throws BusinessLogiceException, CustomerException, CargoException {
        this.businessLogic.putCustomer("Dennis");
        ByteArrayInputStream in = new ByteArrayInputStream(":c\n2\nLiquidCargo Dennis 23.5 23459 , W\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        Cargo[] cargos = businessLogic.getAlleCargo();
        assertEquals(this.mainMenue +
                        "Sie sind im Einfügemodus:\nWollen sie einen Customer Eingben dan 1 oder ein Cargo dan 2 oder exit\n" +
                        "Bitte geben sie das Cargo in dieser Form an:\n" +
                        "[Frachttyp] [Kundenname] [Wert] [Einlagerungsdauer in Sekunden] " +
                        "[kommaseparierte Gefahrenstoffe, einzelnes Komma für keine] " +
                        "[[zerbrechlich (y/n)] [unter Druck (y/n)] [fest (y/n)]] \n" +
                        "True or Flase angaben war falsch ein gbae war falsch\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void insertCargoTypeWitheToTrueAndFalseFalse() throws BusinessLogiceException, CustomerException, CargoException {
        this.businessLogic.putCustomer("Dennis");
        ByteArrayInputStream in = new ByteArrayInputStream(":c\n2\nMixedCargo Dennis 23.5 23459 , Y\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        Cargo[] cargos = businessLogic.getAlleCargo();
        assertEquals(this.mainMenue +
                        "Sie sind im Einfügemodus:\nWollen sie einen Customer Eingben dan 1 oder ein Cargo dan 2 oder exit\n" +
                        "Bitte geben sie das Cargo in dieser Form an:\n" +
                        "[Frachttyp] [Kundenname] [Wert] [Einlagerungsdauer in Sekunden] " +
                        "[kommaseparierte Gefahrenstoffe, einzelnes Komma für keine] " +
                        "[[zerbrechlich (y/n)] [unter Druck (y/n)] [fest (y/n)]] \n" +
                        "True or Flase angaben war falsch ein gbae war falsch\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void insertCargoTypeMixedCargo_good() throws BusinessLogiceException, CustomerException, CargoException {
        this.businessLogic.putCustomer("Dennis");
        ByteArrayInputStream in = new ByteArrayInputStream(":c\n2\nMixedCargo Dennis 23.5 23459 , Y Y\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        Cargo[] cargos = businessLogic.getAlleCargo();
        assertEquals(this.mainMenue +
                        "Sie sind im Einfügemodus:\nWollen sie einen Customer Eingben dan 1 oder ein Cargo dan 2 oder exit\n" +
                        "Bitte geben sie das Cargo in dieser Form an:\n" +
                        "[Frachttyp] [Kundenname] [Wert] [Einlagerungsdauer in Sekunden] " +
                        "[kommaseparierte Gefahrenstoffe, einzelnes Komma für keine] " +
                        "[[zerbrechlich (y/n)] [unter Druck (y/n)] [fest (y/n)]] \n" +
                        "Das Cargo liegt an Platz: 0\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void insertCargoCallTypeLiquidCargo_good() throws BusinessLogiceException, CustomerException, CargoException {
        this.businessLogic.putCustomer("Dennis");
        ByteArrayInputStream in = new ByteArrayInputStream(":c\n2\nLiquidCargo Dennis 23.5 23459 , Y\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        Cargo[] cargos = businessLogic.getAlleCargo();
        assertEquals(this.mainMenue +
                        "Sie sind im Einfügemodus:\nWollen sie einen Customer Eingben dan 1 oder ein Cargo dan 2 oder exit\n" +
                        "Bitte geben sie das Cargo in dieser Form an:\n" +
                        "[Frachttyp] [Kundenname] [Wert] [Einlagerungsdauer in Sekunden] " +
                        "[kommaseparierte Gefahrenstoffe, einzelnes Komma für keine] " +
                        "[[zerbrechlich (y/n)] [unter Druck (y/n)] [fest (y/n)]] \n" +
                        "Das Cargo liegt an Platz: 0\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void insertCargoCallTypeUnitisedCargo_good() throws BusinessLogiceException, CustomerException, CargoException {
        this.businessLogic.putCustomer("Dennis");
        ByteArrayInputStream in = new ByteArrayInputStream(":c\n2\nUnitisedCargo Dennis 23.5 23459 , N\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        Cargo[] cargos = businessLogic.getAlleCargo();
        assertEquals(this.mainMenue +
                        "Sie sind im Einfügemodus:\nWollen sie einen Customer Eingben dan 1 oder ein Cargo dan 2 oder exit\n" +
                        "Bitte geben sie das Cargo in dieser Form an:\n" +
                        "[Frachttyp] [Kundenname] [Wert] [Einlagerungsdauer in Sekunden] " +
                        "[kommaseparierte Gefahrenstoffe, einzelnes Komma für keine] " +
                        "[[zerbrechlich (y/n)] [unter Druck (y/n)] [fest (y/n)]] \n" +
                        "Das Cargo liegt an Platz: 0\n" +
                        mainMenue,
                outContent.toString());
    }
    //=========================================== Delete Mode ====================================================//

    @Test
    public void deleteCustomerCall_good() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":d\n1".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Löschenmodus:\n" +
                        "Wollen sie einen Customer Löschen dan 1 oder ein Cargo dan 2\n" +
                        "Geben sie den Namen des Customers an den sie Löschen wollen.\n" +
                        "java.lang.NullPointerException\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void deleteCustomerOrCargoCall_bad() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":d\n3".getBytes());

        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Löschenmodus:\n" +
                        "Wollen sie einen Customer Löschen dan 1 oder ein Cargo dan 2\n" +
                        "Falsche Eingabe\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void deleteCustomerAtBusinessLogic_good() throws BusinessLogiceException, CustomerException, CargoException {
        businessLogic.putCustomer("Dennis");
        ByteArrayInputStream in = new ByteArrayInputStream(":d\n1\nDennis".getBytes());

        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertNull(businessLogic.getCustomer("Dennis"));
    }

    @Test
    public void deleteCustomerWitheCargosAtBusinessLogic_good() throws BusinessLogiceException, CustomerException, CargoException {
        Customer customer = businessLogic.putCustomer("Dennis");
        Cargo cargo = new LiquidBulkCargoImpl(customer, BigDecimal.ZERO, Duration.ZERO, new LinkedList<Hazard>(), true);
        businessLogic.putCargo(cargo);
        ByteArrayInputStream in = new ByteArrayInputStream(":d\n1\nDennis".getBytes());

        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertNull(businessLogic.getCargo(0));
    }

    @Test
    public void deleteCustomer_good() throws BusinessLogiceException, CustomerException, CargoException {
        businessLogic.putCustomer("Dennis");
        ByteArrayInputStream in = new ByteArrayInputStream(":d\n1\nDennis".getBytes());

        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Löschenmodus:\n" +
                        "Wollen sie einen Customer Löschen dan 1 oder ein Cargo dan 2\n" +
                        "Geben sie den Namen des Customers an den sie Löschen wollen.\n" +
                        "Der Customer wurde gelöscht und die dazugehörigen Cargos auch.\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void deleteCustomerAtBusinessLogic_badErrorString() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":d\n1\n\n".getBytes());

        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Löschenmodus:\n" +
                        "Wollen sie einen Customer Löschen dan 1 oder ein Cargo dan 2\n" +
                        "Geben sie den Namen des Customers an den sie Löschen wollen.\n" +
                        "Customer ist nicht in der Verwaltung.\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void deleteCargoCall_good() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":d\n2".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Löschenmodus:\n" +
                        "Wollen sie einen Customer Löschen dan 1 oder ein Cargo dan 2\n" +
                        "Geben sie den Lagerplatz ein des Cargos was sie Löschen wollen.\n" +
                        "java.lang.NullPointerException\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void deleteCargoAtBusinessLogic_good() throws BusinessLogiceException, CustomerException, CargoException {
        Customer customer = businessLogic.putCustomer("Dennis");
        Cargo cargo = new LiquidBulkCargoImpl(customer, BigDecimal.ZERO, Duration.ZERO, new LinkedList<Hazard>(), true);
        businessLogic.putCargo(cargo);
        ByteArrayInputStream in = new ByteArrayInputStream(":d\n2\n0".getBytes());

        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertNull(businessLogic.getCargo(0));
    }

    @Test
    public void deleteCargo_good() throws BusinessLogiceException, CustomerException, CargoException {
        Customer customer = businessLogic.putCustomer("Dennis");
        Cargo cargo = new LiquidBulkCargoImpl(customer, BigDecimal.ZERO, Duration.ZERO, new LinkedList<Hazard>(), true);
        businessLogic.putCargo(cargo);
        ByteArrayInputStream in = new ByteArrayInputStream(":d\n2\n0".getBytes());

        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Löschenmodus:\n" +
                        "Wollen sie einen Customer Löschen dan 1 oder ein Cargo dan 2\n" +
                        "Geben sie den Lagerplatz ein des Cargos was sie Löschen wollen.\n" +
                        "Cargo wurde gelöscht.\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void deleteCargoAtBusinessLogic_badErrorString() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":d\n2\n1\n".getBytes());

        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Löschenmodus:\n" +
                        "Wollen sie einen Customer Löschen dan 1 oder ein Cargo dan 2\n" +
                        "Geben sie den Lagerplatz ein des Cargos was sie Löschen wollen.\n" +
                        "Es ist kein Cargo an disem Platz um es zu Löschen oder sie sind Out of Rang von der größe des Lagerns\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void deleteCargoOutOfRangeAtBusinessLogic_badErrorString() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":d\n2\n40\n".getBytes());

        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Löschenmodus:\n" +
                        "Wollen sie einen Customer Löschen dan 1 oder ein Cargo dan 2\n" +
                        "Geben sie den Lagerplatz ein des Cargos was sie Löschen wollen.\n" +
                        "Es ist kein Cargo an disem Platz um es zu Löschen oder sie sind Out of Rang von der größe des Lagerns\n" +
                        mainMenue,
                outContent.toString());
    }

    //=========================================== Display Mode ===================================================//

    @Test
    public void displayCustomerCall_good() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":r\n1\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Anzeigemodus:\n" +
                        "Mit der 1 Anzeige der Kunden mit der Anzahl eingelagerter Frachtstücke\n" +
                        "Mit der 2 Anzeige der Frachtstücke\n" +
                        "Mit der 3 Anzeige der vorhandenen bzw. nicht vorhandenen Gefahrenstoffe\n" +
                        "Mit exit kommen sie zurück in das Haupmenü\n" +
                        "Customer mit eingelagerter Cargo Anzahl.\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void displayMenuCall_bad() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":r\n5".getBytes());

        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Anzeigemodus:\n" +
                        "Mit der 1 Anzeige der Kunden mit der Anzahl eingelagerter Frachtstücke\n" +
                        "Mit der 2 Anzeige der Frachtstücke\n" +
                        "Mit der 3 Anzeige der vorhandenen bzw. nicht vorhandenen Gefahrenstoffe\n" +
                        "Mit exit kommen sie zurück in das Haupmenü\n" +
                        "Das eingegeben Kommentar ist Falsch.\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void displayCustomerCallWithOneCustomer() throws BusinessLogiceException, CustomerException, CargoException {
        businessLogic.putCustomer("Dennis");
        ByteArrayInputStream in = new ByteArrayInputStream(":r\n1".getBytes());

        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Anzeigemodus:\n" +
                        "Mit der 1 Anzeige der Kunden mit der Anzahl eingelagerter Frachtstücke\n" +
                        "Mit der 2 Anzeige der Frachtstücke\n" +
                        "Mit der 3 Anzeige der vorhandenen bzw. nicht vorhandenen Gefahrenstoffe\n" +
                        "Mit exit kommen sie zurück in das Haupmenü\n" +
                        "Customer mit eingelagerter Cargo Anzahl.\n" +
                        "Der Customer Dennis hat 0 Cargos im Lager.\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void displayCargoCall_good() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":r\n2\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Anzeigemodus:\n" +
                        "Mit der 1 Anzeige der Kunden mit der Anzahl eingelagerter Frachtstücke\n" +
                        "Mit der 2 Anzeige der Frachtstücke\n" +
                        "Mit der 3 Anzeige der vorhandenen bzw. nicht vorhandenen Gefahrenstoffe\n" +
                        "Mit exit kommen sie zurück in das Haupmenü\n" +
                        "Bitte einer der drei Fracht Typen angben oder keinen um Alles Anzuzeigen. Unitised, Liquid, Mixed\n" +
                        "java.lang.NullPointerException\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void displayCargoTypeUnitised_good() throws BusinessLogiceException, CustomerException, CargoException {
        Customer customer = businessLogic.putCustomer("Dennis");
        UnitisedCargoImpl cargo = new UnitisedCargoImpl(customer, BigDecimal.ZERO, Duration.ZERO, new LinkedList<Hazard>(), true);
        businessLogic.putCargo(cargo);

        ByteArrayInputStream in = new ByteArrayInputStream(":r\n2\nUnitised".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Anzeigemodus:\n" +
                        "Mit der 1 Anzeige der Kunden mit der Anzahl eingelagerter Frachtstücke\n" +
                        "Mit der 2 Anzeige der Frachtstücke\n" +
                        "Mit der 3 Anzeige der vorhandenen bzw. nicht vorhandenen Gefahrenstoffe\n" +
                        "Mit exit kommen sie zurück in das Haupmenü\n" +
                        "Bitte einer der drei Fracht Typen angben oder keinen um Alles Anzuzeigen. Unitised, Liquid, Mixed\n" +
                        "Die Auflistung der Im Lager befindlichen Carogs des Typs: Unitised\n" +
                        "Cargo auf Lagerposition: 0\n" +
                        "Das Datum der Einlagerung ist:" + cargo.getStorageDate() + "\n" +
                        "Das Datum der letzten Inspektion ist:" + cargo.getLastInspectionDate() + "\n" +
                        "\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void displayCargoTypeLiquid_good() throws BusinessLogiceException, CustomerException, CargoException {
        Customer customer = businessLogic.putCustomer("Dennis");
        LiquidBulkCargoImpl cargo = new LiquidBulkCargoImpl(customer, BigDecimal.ZERO, Duration.ZERO, new LinkedList<Hazard>(), true);
        businessLogic.putCargo(cargo);

        ByteArrayInputStream in = new ByteArrayInputStream(":r\n2\nLiquid".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Anzeigemodus:\n" +
                        "Mit der 1 Anzeige der Kunden mit der Anzahl eingelagerter Frachtstücke\n" +
                        "Mit der 2 Anzeige der Frachtstücke\n" +
                        "Mit der 3 Anzeige der vorhandenen bzw. nicht vorhandenen Gefahrenstoffe\n" +
                        "Mit exit kommen sie zurück in das Haupmenü\n" +
                        "Bitte einer der drei Fracht Typen angben oder keinen um Alles Anzuzeigen. Unitised, Liquid, Mixed\n" +
                        "Die Auflistung der Im Lager befindlichen Carogs des Typs: Liquid\n" +
                        "Cargo auf Lagerposition: 0\n" +
                        "Das Datum der Einlagerung ist:" + cargo.getStorageDate() + "\n" +
                        "Das Datum der letzten Inspektion ist:" + cargo.getLastInspectionDate() + "\n" +
                        "\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void displayCargoTypeMixed_good() throws BusinessLogiceException, CustomerException, CargoException {
        Customer customer = businessLogic.putCustomer("Dennis");
        MixedCargoLiquidBulkAndUnitisedImpl cargo = new MixedCargoLiquidBulkAndUnitisedImpl(customer, BigDecimal.ZERO, Duration.ZERO, new LinkedList<Hazard>(), true, true);
        businessLogic.putCargo(cargo);

        ByteArrayInputStream in = new ByteArrayInputStream(":r\n2\nMixed".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Anzeigemodus:\n" +
                        "Mit der 1 Anzeige der Kunden mit der Anzahl eingelagerter Frachtstücke\n" +
                        "Mit der 2 Anzeige der Frachtstücke\n" +
                        "Mit der 3 Anzeige der vorhandenen bzw. nicht vorhandenen Gefahrenstoffe\n" +
                        "Mit exit kommen sie zurück in das Haupmenü\n" +
                        "Bitte einer der drei Fracht Typen angben oder keinen um Alles Anzuzeigen. Unitised, Liquid, Mixed\n" +
                        "Die Auflistung der Im Lager befindlichen Carogs des Typs: Mixed\n" +
                        "Cargo auf Lagerposition: 0\n" +
                        "Das Datum der Einlagerung ist:" + cargo.getStorageDate() + "\n" +
                        "Das Datum der letzten Inspektion ist:" + cargo.getLastInspectionDate() + "\n" +
                        "\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void displayCargoTypeAll_good() throws BusinessLogiceException, CustomerException, CargoException {
        Customer customer = businessLogic.putCustomer("Dennis");
        MixedCargoLiquidBulkAndUnitisedImpl cargo = new MixedCargoLiquidBulkAndUnitisedImpl(customer, BigDecimal.ZERO, Duration.ZERO, new LinkedList<Hazard>(), true, true);
        businessLogic.putCargo(cargo);
        LiquidBulkCargoImpl cargo1 = new LiquidBulkCargoImpl(customer, BigDecimal.ZERO, Duration.ZERO, new LinkedList<Hazard>(), true);
        businessLogic.putCargo(cargo1);
        UnitisedCargoImpl cargo2 = new UnitisedCargoImpl(customer, BigDecimal.ZERO, Duration.ZERO, new LinkedList<Hazard>(), true);
        businessLogic.putCargo(cargo2);


        ByteArrayInputStream in = new ByteArrayInputStream(":r\n2\n ".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Anzeigemodus:\n" +
                        "Mit der 1 Anzeige der Kunden mit der Anzahl eingelagerter Frachtstücke\n" +
                        "Mit der 2 Anzeige der Frachtstücke\n" +
                        "Mit der 3 Anzeige der vorhandenen bzw. nicht vorhandenen Gefahrenstoffe\n" +
                        "Mit exit kommen sie zurück in das Haupmenü\n" +
                        "Bitte einer der drei Fracht Typen angben oder keinen um Alles Anzuzeigen. Unitised, Liquid, Mixed\n" +
                        "Cargo auf Lagerposition: 0\n" +
                        "Das Datum der letzten Inspektion ist:" + cargo.getLastInspectionDate() + "\n" +
                        "Cargo auf Lagerposition: 1\n" +
                        "Das Datum der letzten Inspektion ist:" + cargo1.getLastInspectionDate() + "\n" +
                        "Cargo auf Lagerposition: 2\n" +
                        "Das Datum der letzten Inspektion ist:" + cargo2.getLastInspectionDate() + "\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void displayCargoType_bad() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":r\n2\nMixe".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Anzeigemodus:\n" +
                        "Mit der 1 Anzeige der Kunden mit der Anzahl eingelagerter Frachtstücke\n" +
                        "Mit der 2 Anzeige der Frachtstücke\n" +
                        "Mit der 3 Anzeige der vorhandenen bzw. nicht vorhandenen Gefahrenstoffe\n" +
                        "Mit exit kommen sie zurück in das Haupmenü\n" +
                        "Bitte einer der drei Fracht Typen angben oder keinen um Alles Anzuzeigen. Unitised, Liquid, Mixed\n" +
                        "Der Cargo Type ist nicht vorhanden\n" +
                        mainMenue,
                outContent.toString());
    }


    @Test
    public void displayHazardCall_good() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":r\n3\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Anzeigemodus:\n" +
                        "Mit der 1 Anzeige der Kunden mit der Anzahl eingelagerter Frachtstücke\n" +
                        "Mit der 2 Anzeige der Frachtstücke\n" +
                        "Mit der 3 Anzeige der vorhandenen bzw. nicht vorhandenen Gefahrenstoffe\n" +
                        "Mit exit kommen sie zurück in das Haupmenü\n" +
                        "Bitte geben sie True ein um die Enthaltende Harzard sich anzeigen zu lassen oder False um die nicht enthaltenden.\n" +
                        "java.lang.NullPointerException\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void displayHazardIncludCall_good() throws BusinessLogiceException, CustomerException, CargoException {
        Collection<Hazard> hazard1 = new ArrayList<>();
        Collection<Hazard> hazard2 = new ArrayList<>();
        hazard1.add(Hazard.explosive);
        hazard2.add(Hazard.explosive);
        hazard2.add(Hazard.toxic);
        Customer customer = businessLogic.putCustomer("Dennis");
        Cargo cargo = new MixedCargoLiquidBulkAndUnitisedImpl(customer, BigDecimal.ZERO, Duration.ZERO, hazard1, true, true);
        businessLogic.putCargo(cargo);
        Cargo cargo1 = new LiquidBulkCargoImpl(customer, BigDecimal.ZERO, Duration.ZERO, hazard2, true);
        businessLogic.putCargo(cargo1);

        ByteArrayInputStream in = new ByteArrayInputStream(":r\n3\nTrue".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Anzeigemodus:\n" +
                        "Mit der 1 Anzeige der Kunden mit der Anzahl eingelagerter Frachtstücke\n" +
                        "Mit der 2 Anzeige der Frachtstücke\n" +
                        "Mit der 3 Anzeige der vorhandenen bzw. nicht vorhandenen Gefahrenstoffe\n" +
                        "Mit exit kommen sie zurück in das Haupmenü\n" +
                        "Bitte geben sie True ein um die Enthaltende Harzard sich anzeigen zu lassen oder False um die nicht enthaltenden.\n" +
                        "In den Cargos enthalten sind:\n" +
                        "explosive,\n" +
                        "toxic,\n\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void displayHazardNotIncludCall_good() throws BusinessLogiceException, CustomerException, CargoException {
        Collection<Hazard> hazard1 = new ArrayList<>();
        Collection<Hazard> hazard2 = new ArrayList<>();
        hazard1.add(Hazard.explosive);
        hazard2.add(Hazard.explosive);
        hazard2.add(Hazard.toxic);
        Customer customer = businessLogic.putCustomer("Dennis");
        Cargo cargo = new MixedCargoLiquidBulkAndUnitisedImpl(customer, BigDecimal.ZERO, Duration.ZERO, hazard1, true, true);
        businessLogic.putCargo(cargo);
        Cargo cargo1 = new LiquidBulkCargoImpl(customer, BigDecimal.ZERO, Duration.ZERO, hazard2, true);
        businessLogic.putCargo(cargo1);
        ByteArrayInputStream in = new ByteArrayInputStream(":r\n3\nFalse".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Anzeigemodus:\n" +
                        "Mit der 1 Anzeige der Kunden mit der Anzahl eingelagerter Frachtstücke\n" +
                        "Mit der 2 Anzeige der Frachtstücke\n" +
                        "Mit der 3 Anzeige der vorhandenen bzw. nicht vorhandenen Gefahrenstoffe\n" +
                        "Mit exit kommen sie zurück in das Haupmenü\n" +
                        "Bitte geben sie True ein um die Enthaltende Harzard sich anzeigen zu lassen oder False um die nicht enthaltenden.\n" +
                        "In den Cargos nicht enthalten sind:\n" +
                        "flammable,\n" +
                        "radioactive,\n\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void displayHazardCall_bad() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":r\n3\nTR".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Anzeigemodus:\n" +
                        "Mit der 1 Anzeige der Kunden mit der Anzahl eingelagerter Frachtstücke\n" +
                        "Mit der 2 Anzeige der Frachtstücke\n" +
                        "Mit der 3 Anzeige der vorhandenen bzw. nicht vorhandenen Gefahrenstoffe\n" +
                        "Mit exit kommen sie zurück in das Haupmenü\n" +
                        "Bitte geben sie True ein um die Enthaltende Harzard sich anzeigen zu lassen oder False um die nicht enthaltenden.\n" +
                        "Das eingegeben Kommentar ist Falsch.\n" +
                        mainMenue,
                outContent.toString());
    }

    //=========================================== Persistens Mode ================================================//

    @Test
    public void persistensJOSSaveCall_good() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":p\n1\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Persistenenzmodus:\n" +
                        "Mit der 1 Speichern mit JOS\n" +
                        "Mit der 2 Speichern mit JBP\n" +
                        "Mit der 3 Laden mit JOS\n" +
                        "Mit der 4 Laden mit JBP\n" +
                        "Mit der 5 Speichern eines Cargos\n" +
                        "Mit der 6 Laden eines Cargos\n" +
                        "Mit exit kommen sie zurück in das Haupmenü\n" +
                        "Bitte geben sie den Filename für das Speichern in JOS an\n" +
                        "java.lang.NullPointerException\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void persistensJOSSaveCall_fileName() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":p\n1\ntestfile\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Persistenenzmodus:\n" +
                        "Mit der 1 Speichern mit JOS\n" +
                        "Mit der 2 Speichern mit JBP\n" +
                        "Mit der 3 Laden mit JOS\n" +
                        "Mit der 4 Laden mit JBP\n" +
                        "Mit der 5 Speichern eines Cargos\n" +
                        "Mit der 6 Laden eines Cargos\n" +
                        "Mit exit kommen sie zurück in das Haupmenü\n" +
                        "Bitte geben sie den Filename für das Speichern in JOS an\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void persistensJOSSaveCall_fileExist() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":p\n1\ntestfile\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertTrue(new File("testfile.txt").exists());
    }

    @Test
    public void persistensJBPSaveCall_good() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":p\n2\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Persistenenzmodus:\n" +
                        "Mit der 1 Speichern mit JOS\n" +
                        "Mit der 2 Speichern mit JBP\n" +
                        "Mit der 3 Laden mit JOS\n" +
                        "Mit der 4 Laden mit JBP\n" +
                        "Mit der 5 Speichern eines Cargos\n" +
                        "Mit der 6 Laden eines Cargos\n" +
                        "Mit exit kommen sie zurück in das Haupmenü\n" +
                        "Bitte geben sie den Filename für das Speichern in JBP an\n" +
                        "java.lang.NullPointerException\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void persistensJBPSaveCall_fileName() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":p\n2\ntestfileXML\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Persistenenzmodus:\n" +
                        "Mit der 1 Speichern mit JOS\n" +
                        "Mit der 2 Speichern mit JBP\n" +
                        "Mit der 3 Laden mit JOS\n" +
                        "Mit der 4 Laden mit JBP\n" +
                        "Mit der 5 Speichern eines Cargos\n" +
                        "Mit der 6 Laden eines Cargos\n" +
                        "Mit exit kommen sie zurück in das Haupmenü\n" +
                        "Bitte geben sie den Filename für das Speichern in JBP an\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void persistensJBPSaveCall_fileExist() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":p\n2\ntestfileXML\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertTrue(new File("testfileXML.xml").exists());
    }

    @Test
    public void persistensJOSLoadCall_good() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":p\n3\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Persistenenzmodus:\n" +
                        "Mit der 1 Speichern mit JOS\n" +
                        "Mit der 2 Speichern mit JBP\n" +
                        "Mit der 3 Laden mit JOS\n" +
                        "Mit der 4 Laden mit JBP\n" +
                        "Mit der 5 Speichern eines Cargos\n" +
                        "Mit der 6 Laden eines Cargos\n" +
                        "Mit exit kommen sie zurück in das Haupmenü\n" +
                        "Bitte geben sie den Filename für das Laden in JOS an\n" +
                        "java.lang.NullPointerException\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void persistensJOSLoadCall_fileName() throws BusinessLogiceException, CustomerException, CargoException {
        Customer customer = businessLogic.putCustomer("Dennis");
        ByteArrayInputStream in = new ByteArrayInputStream(":p\n1\ntestfile\n:c\n1\nTom\n:p\n3\ntestfile\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(1, controlModel.getModel().getAlleCustomer().size());
    }

    @Test
    public void persistensJOSLoadCall_customerTreu() throws BusinessLogiceException, CustomerException, CargoException {
        Customer customer = businessLogic.putCustomer("Dennis");
        ByteArrayInputStream in = new ByteArrayInputStream(":p\n1\ntestfile\n:c\n1\nTom\n:p\n3\ntestfile\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        Customer customerTest = (Customer) controlModel.getModel().getAlleCustomer().get("Dennis");
        assertEquals(customer.getName(), customer.getName());
    }

    @Test
    public void persistensJOSLoadCall_loadCompiet() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":p\n1\ntestfile\n:p\n3\ntestfile\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Persistenenzmodus:\n" +
                        "Mit der 1 Speichern mit JOS\n" +
                        "Mit der 2 Speichern mit JBP\n" +
                        "Mit der 3 Laden mit JOS\n" +
                        "Mit der 4 Laden mit JBP\n" +
                        "Mit der 5 Speichern eines Cargos\n" +
                        "Mit der 6 Laden eines Cargos\n" +
                        "Mit exit kommen sie zurück in das Haupmenü\n" +
                        "Bitte geben sie den Filename für das Speichern in JOS an\n" +
                        mainMenue +
                        "Sie sind im Persistenenzmodus:\n" +
                        "Mit der 1 Speichern mit JOS\n" +
                        "Mit der 2 Speichern mit JBP\n" +
                        "Mit der 3 Laden mit JOS\n" +
                        "Mit der 4 Laden mit JBP\n" +
                        "Mit der 5 Speichern eines Cargos\n" +
                        "Mit der 6 Laden eines Cargos\n" +
                        "Mit exit kommen sie zurück in das Haupmenü\n" +
                        "Bitte geben sie den Filename für das Laden in JOS an\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void persistensJBPLoadCall_good() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":p\n4\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Persistenenzmodus:\n" +
                        "Mit der 1 Speichern mit JOS\n" +
                        "Mit der 2 Speichern mit JBP\n" +
                        "Mit der 3 Laden mit JOS\n" +
                        "Mit der 4 Laden mit JBP\n" +
                        "Mit der 5 Speichern eines Cargos\n" +
                        "Mit der 6 Laden eines Cargos\n" +
                        "Mit exit kommen sie zurück in das Haupmenü\n" +
                        "Bitte geben sie den Filename für das Laden in JBP an\n" +
                        "java.lang.NullPointerException\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void persistensJBPLoadCall_fileName() throws BusinessLogiceException, CustomerException, CargoException {
        Customer customer = controlModel.getModel().putCustomer("Dennis");
        ByteArrayInputStream in = new ByteArrayInputStream(":p\n2\ntestfileXML\n:c\n1\nTom\n:p\n4\ntestfileXML\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(1, controlModel.getModel().getAlleCustomer().size());
    }

    @Test
    public void persistensJBPLoadCall_customerTreu() throws BusinessLogiceException, CustomerException, CargoException {
        Customer customer = businessLogic.putCustomer("Dennis");
        ByteArrayInputStream in = new ByteArrayInputStream(":p\n2\ntestfileXML\n:c\n1\nTom\n:p\n4\ntestfileXML\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        Customer customerTest = (Customer) controlModel.getModel().getAlleCustomer().get("Dennis");
        assertEquals(customer.getName(), customer.getName());
    }

    @Test
    public void persistensJBPLoadCall_loadCompiet() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":p\n2\ntestfileXML\n:p\n4\ntestfileXML\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Persistenenzmodus:\n" +
                        "Mit der 1 Speichern mit JOS\n" +
                        "Mit der 2 Speichern mit JBP\n" +
                        "Mit der 3 Laden mit JOS\n" +
                        "Mit der 4 Laden mit JBP\n" +
                        "Mit der 5 Speichern eines Cargos\n" +
                        "Mit der 6 Laden eines Cargos\n" +
                        "Mit exit kommen sie zurück in das Haupmenü\n" +
                        "Bitte geben sie den Filename für das Speichern in JBP an\n" +
                        mainMenue +
                        "Sie sind im Persistenenzmodus:\n" +
                        "Mit der 1 Speichern mit JOS\n" +
                        "Mit der 2 Speichern mit JBP\n" +
                        "Mit der 3 Laden mit JOS\n" +
                        "Mit der 4 Laden mit JBP\n" +
                        "Mit der 5 Speichern eines Cargos\n" +
                        "Mit der 6 Laden eines Cargos\n" +
                        "Mit exit kommen sie zurück in das Haupmenü\n" +
                        "Bitte geben sie den Filename für das Laden in JBP an\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void persistensCargoSave_good() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":p\n5\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Persistenenzmodus:\n" +
                        "Mit der 1 Speichern mit JOS\n" +
                        "Mit der 2 Speichern mit JBP\n" +
                        "Mit der 3 Laden mit JOS\n" +
                        "Mit der 4 Laden mit JBP\n" +
                        "Mit der 5 Speichern eines Cargos\n" +
                        "Mit der 6 Laden eines Cargos\n" +
                        "Mit exit kommen sie zurück in das Haupmenü\n" +
                        "Bitte geben sie den Platz an wo das Cargo liegt das sie Speichern möchten.\n" +
                        "java.lang.NullPointerException\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void persistensCargoSave_fileExist() throws BusinessLogiceException, CustomerException, CargoException {
        Customer customer = businessLogic.putCustomer("Dennis");
        businessLogic.putCargo(new LiquidBulkCargoImpl(customer, BigDecimal.valueOf(20), Duration.ZERO, new LinkedList<Hazard>(), true));
        ByteArrayInputStream in = new ByteArrayInputStream(":p\n5\n0\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertTrue(new File("CargoSave.txt").exists());
    }

    @Test
    public void persistensCargoSave_NoCargoOnThePlace() throws BusinessLogiceException, CustomerException, CargoException {
        Customer customer = businessLogic.putCustomer("Dennis");
        businessLogic.putCargo(new LiquidBulkCargoImpl(customer, BigDecimal.valueOf(20), Duration.ZERO, new LinkedList<Hazard>(), true));
        ByteArrayInputStream in = new ByteArrayInputStream(":p\n5\n3\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Persistenenzmodus:\n" +
                        "Mit der 1 Speichern mit JOS\n" +
                        "Mit der 2 Speichern mit JBP\n" +
                        "Mit der 3 Laden mit JOS\n" +
                        "Mit der 4 Laden mit JBP\n" +
                        "Mit der 5 Speichern eines Cargos\n" +
                        "Mit der 6 Laden eines Cargos\n" +
                        "Mit exit kommen sie zurück in das Haupmenü\n" +
                        "Bitte geben sie den Platz an wo das Cargo liegt das sie Speichern möchten.\n" +
                        "model.businessLogice.BusinessLogiceException: Es beindet sich kein Cargo an dem Platz.\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void persistensCargoSave_NoCargoOutOfRange() throws BusinessLogiceException, CustomerException, CargoException {
        Customer customer = businessLogic.putCustomer("Dennis");
        businessLogic.putCargo(new LiquidBulkCargoImpl(customer, BigDecimal.valueOf(20), Duration.ZERO, new LinkedList<Hazard>(), true));
        ByteArrayInputStream in = new ByteArrayInputStream(":p\n5\n40\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Persistenenzmodus:\n" +
                        "Mit der 1 Speichern mit JOS\n" +
                        "Mit der 2 Speichern mit JBP\n" +
                        "Mit der 3 Laden mit JOS\n" +
                        "Mit der 4 Laden mit JBP\n" +
                        "Mit der 5 Speichern eines Cargos\n" +
                        "Mit der 6 Laden eines Cargos\n" +
                        "Mit exit kommen sie zurück in das Haupmenü\n" +
                        "Bitte geben sie den Platz an wo das Cargo liegt das sie Speichern möchten.\n" +
                        "model.businessLogice.BusinessLogiceException: Die Size wurde zu groß gewählt\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void persistensCargoLoad() throws BusinessLogiceException, CustomerException, CargoException {
        Customer customer = businessLogic.putCustomer("Dennis");
        businessLogic.putCargo(new LiquidBulkCargoImpl(customer, BigDecimal.valueOf(20), Duration.ZERO, new LinkedList<Hazard>(), true));
        ByteArrayInputStream in = new ByteArrayInputStream(":p\n5\n0\n:p\n6\n1".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        Cargo[] cargos = businessLogic.getAlleCargo();
        assertNotNull(cargos[1]);
    }

    @Test
    public void persistensCargoLoad_OnThePlaceIsCargo() throws BusinessLogiceException, CustomerException, CargoException {
        Customer customer = businessLogic.putCustomer("Dennis");
        businessLogic.putCargo(new LiquidBulkCargoImpl(customer, BigDecimal.valueOf(20), Duration.ZERO, new LinkedList<Hazard>(), true));
        ByteArrayInputStream in = new ByteArrayInputStream(":p\n5\n0\n:p\n6\n0".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        Cargo[] cargos = businessLogic.getAlleCargo();
        assertEquals(this.mainMenue +
                        "Sie sind im Persistenenzmodus:\n" +
                        "Mit der 1 Speichern mit JOS\n" +
                        "Mit der 2 Speichern mit JBP\n" +
                        "Mit der 3 Laden mit JOS\n" +
                        "Mit der 4 Laden mit JBP\n" +
                        "Mit der 5 Speichern eines Cargos\n" +
                        "Mit der 6 Laden eines Cargos\n" +
                        "Mit exit kommen sie zurück in das Haupmenü\n" +
                        "Bitte geben sie den Platz an wo das Cargo liegt das sie Speichern möchten.\n" +
                        mainMenue +
                        "Sie sind im Persistenenzmodus:\n" +
                        "Mit der 1 Speichern mit JOS\n" +
                        "Mit der 2 Speichern mit JBP\n" +
                        "Mit der 3 Laden mit JOS\n" +
                        "Mit der 4 Laden mit JBP\n" +
                        "Mit der 5 Speichern eines Cargos\n" +
                        "Mit der 6 Laden eines Cargos\n" +
                        "Mit exit kommen sie zurück in das Haupmenü\n" +
                        "Bitte geben sie den Platz an wo das Cargo liegt soll das geladen werden soll.\n" +
                        "java.lang.IllegalArgumentException: An dem Platz befindet sich schon ein Cargo\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void persistensCargoLoad_OutOfRange() throws BusinessLogiceException, CustomerException, CargoException {
        Customer customer = businessLogic.putCustomer("Dennis");
        businessLogic.putCargo(new LiquidBulkCargoImpl(customer, BigDecimal.valueOf(20), Duration.ZERO, new LinkedList<Hazard>(), true));
        ByteArrayInputStream in = new ByteArrayInputStream(":p\n5\n0\n:p\n6\n30".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        Cargo[] cargos = businessLogic.getAlleCargo();
        assertEquals(this.mainMenue +
                        "Sie sind im Persistenenzmodus:\n" +
                        "Mit der 1 Speichern mit JOS\n" +
                        "Mit der 2 Speichern mit JBP\n" +
                        "Mit der 3 Laden mit JOS\n" +
                        "Mit der 4 Laden mit JBP\n" +
                        "Mit der 5 Speichern eines Cargos\n" +
                        "Mit der 6 Laden eines Cargos\n" +
                        "Mit exit kommen sie zurück in das Haupmenü\n" +
                        "Bitte geben sie den Platz an wo das Cargo liegt das sie Speichern möchten.\n" +
                        mainMenue +
                        "Sie sind im Persistenenzmodus:\n" +
                        "Mit der 1 Speichern mit JOS\n" +
                        "Mit der 2 Speichern mit JBP\n" +
                        "Mit der 3 Laden mit JOS\n" +
                        "Mit der 4 Laden mit JBP\n" +
                        "Mit der 5 Speichern eines Cargos\n" +
                        "Mit der 6 Laden eines Cargos\n" +
                        "Mit exit kommen sie zurück in das Haupmenü\n" +
                        "Bitte geben sie den Platz an wo das Cargo liegt soll das geladen werden soll.\n" +
                        "java.lang.IllegalArgumentException: Die Size ist zu groß für das Lager.\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void persistensCargoLoad_good() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":p\n6\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Persistenenzmodus:\n" +
                        "Mit der 1 Speichern mit JOS\n" +
                        "Mit der 2 Speichern mit JBP\n" +
                        "Mit der 3 Laden mit JOS\n" +
                        "Mit der 4 Laden mit JBP\n" +
                        "Mit der 5 Speichern eines Cargos\n" +
                        "Mit der 6 Laden eines Cargos\n" +
                        "Mit exit kommen sie zurück in das Haupmenü\n" +
                        "Bitte geben sie den Platz an wo das Cargo liegt soll das geladen werden soll.\n" +
                        "java.lang.NullPointerException\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void persistensJBPAndJOSCall_bad() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":p\n8\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Persistenenzmodus:\n" +
                        "Mit der 1 Speichern mit JOS\n" +
                        "Mit der 2 Speichern mit JBP\n" +
                        "Mit der 3 Laden mit JOS\n" +
                        "Mit der 4 Laden mit JBP\n" +
                        "Mit der 5 Speichern eines Cargos\n" +
                        "Mit der 6 Laden eines Cargos\n" +
                        "Mit exit kommen sie zurück in das Haupmenü\n" +
                        "Das eingegeben Kommentar ist Falsch.\n" +
                        mainMenue,
                outContent.toString());
    }

    //=========================================== Config Mode =====================================================//

    @Test
    public void configAddObserverLoadCall_good() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":config\n1\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Configmodus:\n" +
                        "Mit der 1 add von Beobachtern\n" +
                        "Mit der 2 remove von Beobachtern\n" +
                        "Mit exit kommen sie zurück in das Haupmenü\n" +
                        "Bitte geben sie einen der Observer an(die Zahl), den sie Einhängen wollen: CargoDelet(1), Hazard(2), Storage(3), StorageSpace(4)\n" +
                        "java.lang.NullPointerException\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void configAddObserverCall_CargoDeletObserver1() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":config\n1\n1\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(1, this.controlModel.getModel().getObserverList().size());
    }

    @Test
    public void configAddObserverCall_CargoDeletObserver2() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":config\n1\n1\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        List<Observer> observers = this.controlModel.getModel().getObserverList();
        assertEquals(CargoDeletObserver.class, observers.get(0).getClass());
    }

    @Test
    public void configAddObserverCall_StorageObserver1() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":config\n1\n3\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(1, this.controlModel.getModel().getObserverList().size());
    }

    @Test
    public void configAddObserverCall_StorageObserver2() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":config\n1\n3\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        List<Observer> observers = this.controlModel.getModel().getObserverList();
        assertEquals(StorageObserver.class, observers.get(0).getClass());
    }

    @Test
    public void configAddObserverCall_StorageSpaceObserver1() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":config\n1\n4\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(1, this.controlModel.getModel().getObserverList().size());
    }

    @Test
    public void configAddObserverCall_StorageSpaceObserver2() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":config\n1\n4\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        List<Observer> observers = this.controlModel.getModel().getObserverList();
        assertEquals(StorageSpaceObserver.class, observers.get(0).getClass());
    }

    @Test
    public void configAddObserverCall_HazardObserver1() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":config\n1\n2\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(1, this.controlModel.getModel().getObserverList().size());
    }

    @Test
    public void configAddObserverCall_HazardObserver2() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":config\n1\n2\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        List<Observer> observers = this.controlModel.getModel().getObserverList();
        assertEquals(HazardObserver.class, observers.get(0).getClass());
    }

    @Test
    public void configAddObserverLoadCall_folseNumber() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":config\n1\n5\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Configmodus:\n" +
                        "Mit der 1 add von Beobachtern\n" +
                        "Mit der 2 remove von Beobachtern\n" +
                        "Mit exit kommen sie zurück in das Haupmenü\n" +
                        "Bitte geben sie einen der Observer an(die Zahl), den sie Einhängen wollen: CargoDelet(1), Hazard(2), Storage(3), StorageSpace(4)\n" +
                        "java.lang.IllegalArgumentException: False Nummer\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void configRemoveObserverCall_good() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":config\n2\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Configmodus:\n" +
                        "Mit der 1 add von Beobachtern\n" +
                        "Mit der 2 remove von Beobachtern\n" +
                        "Mit exit kommen sie zurück in das Haupmenü\n" +
                        "Bitte geben sie einen der Observer an(die Zahl), den sie Aushängen wollen: CargoDelet(1), Hazard(2), Storage(3), StorageSpace(4)\n" +
                        "java.lang.NullPointerException\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void configRemoveObserverCall_CargoDeletObserver() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":config\n1\n1\n:config\n2\n1\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(0, this.controlModel.getModel().getObserverList().size());
    }

    @Test
    public void configRemoveObserverCall_StorageObserver() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":config\n1\n3\n:config\n2\n3\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(0, this.controlModel.getModel().getObserverList().size());
    }

    @Test
    public void configRemoveObserverCall_StorageSpaceObserver() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":config\n1\n4\n:config\n2\n4\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(0, this.controlModel.getModel().getObserverList().size());
    }

    @Test
    public void configRemoveObserverCall_HazardObserver() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":config\n1\n2\n:config\n2\n2\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(0, this.controlModel.getModel().getObserverList().size());
    }

    @Test
    public void configRemoveObserverLoadCall_folseNumber() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":config\n2\n5\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Configmodus:\n" +
                        "Mit der 1 add von Beobachtern\n" +
                        "Mit der 2 remove von Beobachtern\n" +
                        "Mit exit kommen sie zurück in das Haupmenü\n" +
                        "Bitte geben sie einen der Observer an(die Zahl), den sie Aushängen wollen: CargoDelet(1), Hazard(2), Storage(3), StorageSpace(4)\n" +
                        "java.lang.IllegalArgumentException: False Nummer\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void configObserverCall_bad() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":config\n4\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Configmodus:\n" +
                        "Mit der 1 add von Beobachtern\n" +
                        "Mit der 2 remove von Beobachtern\n" +
                        "Mit exit kommen sie zurück in das Haupmenü\n" +
                        "Das eingegeben Kommentar ist Falsch.\n" +
                        mainMenue,
                outContent.toString());
    }

    //=========================================== Modification Mode ===============================================//

    @Test
    public void modificationCall_good() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":u\n\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Änderungsmodus:\n" +
                        "Bitte geben sie den Lagerplatz an wo das Inspektionsdatum auf den aktuellen Zeitpunkt gesetzt werden soll.\n" +
                        "java.lang.NumberFormatException: For input string: \"\"\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void modificationUpadetCargoDate_good() throws BusinessLogiceException, CustomerException, CargoException {
        Customer customer = businessLogic.putCustomer("Dennis");
        Cargo cargo = new MixedCargoLiquidBulkAndUnitisedImpl(customer, BigDecimal.ZERO, Duration.ZERO, new LinkedList<Hazard>(), true, true);
        businessLogic.putCargo(cargo);
        ByteArrayInputStream in = new ByteArrayInputStream(":u\n0\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Änderungsmodus:\n" +
                        "Bitte geben sie den Lagerplatz an wo das Inspektionsdatum auf den aktuellen Zeitpunkt gesetzt werden soll.\n" +
                        "Das Inspation Datum wurde auf die Aktuelle Zeit gesetzt.\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void modificationUpadetCargoDateAtBusinessLogic_good() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":u\n0\n".getBytes());
        Customer customer = businessLogic.putCustomer("Dennis");
        MixedCargoLiquidBulkAndUnitisedImpl cargo = new MixedCargoLiquidBulkAndUnitisedImpl(customer, BigDecimal.ZERO, Duration.ZERO, new LinkedList<Hazard>(), true, true);
        businessLogic.putCargo(cargo);
        Date date = new Date(65);
        cargo.setLastInspectionDate(date);

        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertNotNull(businessLogic.getCargo(0).getLastInspectionDate());
    }

    @Test
    public void modificationUpadetCargoDate_bad() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":u\n5\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Änderungsmodus:\n" +
                        "Bitte geben sie den Lagerplatz an wo das Inspektionsdatum auf den aktuellen Zeitpunkt gesetzt werden soll.\n" +
                        "Auf dem Platz ist kein Cargo Eingelagert\n" +
                        mainMenue,
                outContent.toString());
    }

    @Test
    public void modificationUpadetCargoDateOutOffRang_bad() throws BusinessLogiceException, CustomerException, CargoException {
        ByteArrayInputStream in = new ByteArrayInputStream(":u\n40\n".getBytes());
        System.setIn(in);
        this.cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid, cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler, consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler, observerAddEventEventHandler, removeEventEventHandler, cargoPersistensEventHandler);
        this.cli.runCLI();
        assertEquals(this.mainMenue +
                        "Sie sind im Änderungsmodus:\n" +
                        "Bitte geben sie den Lagerplatz an wo das Inspektionsdatum auf den aktuellen Zeitpunkt gesetzt werden soll.\n" +
                        "Die Size gibt es nicht im Array\n" +
                        mainMenue,
                outContent.toString());
    }

}