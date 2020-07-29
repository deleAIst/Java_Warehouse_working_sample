package control;

import model.businessLogice.BusinessLogiceException;
import model.storageContract.administration.CustomerException;
import model.storageContract.cargo.CargoException;
import model.storageContract.cargo.Hazard;
import routing.event.*;
import routing.eventHandler.EventHandler;

import java.io.*;
import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.Duration;
import java.util.Collection;
import java.util.HashSet;
import java.util.StringTokenizer;

/**
 * @author Dennis Dominik Lehmann
 */
public class CLI {

    private final EventHandler<CustomerInsertEvent> cuHandler;
    private final EventHandler<CustomerDeleteEvent> customerDeleteHandler;
    private final EventHandler<CustomerDisplayEvent> customerDisplayHandler;
    private final EventHandler<CargoLiquidInsertEvent> caLHandler;
    private final EventHandler<CargoMixedInsertEvent> caMHandler;
    private final EventHandler<CargoUnitisedInsertEvent> caUHandler;
    private final EventHandler<CargoDeleteEvent> cargoDeleteHandler;
    private final EventHandler<CargoTypeDisplayEvent> cargoTypeDisplayEventHandler;
    private final EventHandler<ConsoleOutputEvent> coHandler;
    private final EventHandler<DateOfInspectionEvent> dateOfInspectionEventHandler;
    private final EventHandler<HazardDisplayEvent> hazardDisplayEventHandler;
    private final EventHandler<PersistentEvent> persistenceEventHandler;
    private final EventHandler<ObserverAddEvent> observerAddEventHandler;
    private final EventHandler<ObserverRemoveEvent> observerRemoveEventHandler;
    private final EventHandler<CargoPersistentEvent> cargoPersistenEventHandler;
    private final BufferedReader userInput;


    public CLI(InputStream is, EventHandler<CustomerInsertEvent> cieh, EventHandler<CustomerDeleteEvent> cudh, EventHandler<CustomerDisplayEvent> cudih, EventHandler<CargoLiquidInsertEvent> chL, EventHandler<CargoMixedInsertEvent> chM, EventHandler<CargoUnitisedInsertEvent> chU, EventHandler<CargoDeleteEvent> cdh, EventHandler<CargoTypeDisplayEvent> ctdeh, EventHandler<ConsoleOutputEvent> consoleH, EventHandler<DateOfInspectionEvent> doeh, EventHandler<HazardDisplayEvent> hdeh, EventHandler<PersistentEvent> peh, EventHandler<ObserverAddEvent> oae, EventHandler<ObserverRemoveEvent> oreh, EventHandler<CargoPersistentEvent> cpe) {
        this.userInput = new BufferedReader(new InputStreamReader(is));

        this.cuHandler = cieh;
        this.customerDeleteHandler = cudh;
        this.customerDisplayHandler = cudih;
        this.caLHandler = chL;
        this.caMHandler = chM;
        this.caUHandler = chU;
        this.cargoDeleteHandler = cdh;
        this.cargoTypeDisplayEventHandler = ctdeh;
        this.coHandler = consoleH;
        this.dateOfInspectionEventHandler = doeh;
        this.hazardDisplayEventHandler = hdeh;
        this.persistenceEventHandler = peh;
        this.observerAddEventHandler = oae;
        this.observerRemoveEventHandler = oreh;
        this.cargoPersistenEventHandler=cpe;
    }


    public void runCLI() throws BusinessLogiceException, CustomerException, CargoException {
        boolean again = true;
        while (again) {
            try {
                ConsoleOutputEvent m = new ConsoleOutputEvent(this, "Mode", false);
                this.coHandler.handle(m);

                String cmdLineString = userInput.readLine();

                if (cmdLineString == null) break;

                cmdLineString = cmdLineString.trim().toLowerCase();

                switch (cmdLineString) {
                    case ":c":
                        this.insertMode();
                        break;
                    case ":d":
                        this.deleteMode();
                        break;
                    case ":r":
                        this.displayMode();
                        break;
                    case ":u":
                        this.modificationMode();
                        break;
                    case ":p":
                        this.persistenceMode();
                        break;
                    case ":config":
                        this.configMode();
                        break;
                    case ":exit":
                        again = false;
                        break;
                    default:
                        ConsoleOutputEvent c = new ConsoleOutputEvent(this, "Falsche Eingabe", false);
                        this.coHandler.handle(c);
                }
            } catch (IOException ex) {
                ConsoleOutputEvent c = new ConsoleOutputEvent(this, "IO Fehler", false);
                this.coHandler.handle(c);
                System.exit(0);
            } catch (CargoException | CustomerException | BusinessLogiceException | IllegalArgumentException | NullPointerException e) {
                ConsoleOutputEvent c = new ConsoleOutputEvent(this, e.toString(), true);
                this.coHandler.handle(c);
            }
        }
    }

    private void insertMode() throws BusinessLogiceException, CustomerException, CargoException {
        try {
            ConsoleOutputEvent i = new ConsoleOutputEvent(this, "Insert", false);
            this.coHandler.handle(i);


            String cmdLineString = userInput.readLine().trim();
            switch (cmdLineString) {
                case "1":
                    ConsoleOutputEvent customerI = new ConsoleOutputEvent(this, "CustomerI", false);
                    this.coHandler.handle(customerI);
                    try {
                        String name = userInput.readLine().trim();
                        CustomerInsertEvent customerEvent = new CustomerInsertEvent(this, name);
                        this.cuHandler.handle(customerEvent);
                    } catch (CustomerException ce) {
                        ConsoleOutputEvent c = new ConsoleOutputEvent(this, ce.getLocalizedMessage(), true);
                        this.coHandler.handle(c);
                    }
                    break;
                case "2":
                    this.insertCargo();
                    break;
                case "exit":
                    break;
                default:
                    ConsoleOutputEvent falscheEingabe = new ConsoleOutputEvent(this, "Falsche Eingabe", false);
                    this.coHandler.handle(falscheEingabe);
            }

        } catch (IOException | BusinessLogiceException | CargoException | CustomerException ex) {
            ConsoleOutputEvent c = new ConsoleOutputEvent(this, ex.getLocalizedMessage(), true);
            this.coHandler.handle(c);
            System.exit(0);
        } catch (Exception e) {
            ConsoleOutputEvent c = new ConsoleOutputEvent(this, "Falsche eingabe", true);
            this.coHandler.handle(c);
        }


    }

    private void deleteMode() throws BusinessLogiceException, CustomerException, CargoException {
        try {
            ConsoleOutputEvent d = new ConsoleOutputEvent(this, "delete", false);
            this.coHandler.handle(d);

            String cmdLineString = userInput.readLine().trim();
            switch (cmdLineString) {
                case "1":
                    ConsoleOutputEvent deleteCustomer = new ConsoleOutputEvent(this, "delete Customer", false);
                    this.coHandler.handle(deleteCustomer);
                    String customerName = userInput.readLine().trim();
                    CustomerDeleteEvent customerDeleteEvent = new CustomerDeleteEvent(this, customerName);
                    this.customerDeleteHandler.handle(customerDeleteEvent);
                    ConsoleOutputEvent deleteCustomerFinal = new ConsoleOutputEvent(this, "delete Customer final", false);
                    this.coHandler.handle(deleteCustomerFinal);
                    break;
                case "2":
                    ConsoleOutputEvent deleteCargo = new ConsoleOutputEvent(this, "delete Cargo", false);
                    this.coHandler.handle(deleteCargo);
                    String cargoLocationStr = userInput.readLine().trim();
                    int cargoLocation = Integer.parseInt(cargoLocationStr);
                    CargoDeleteEvent cargoDeleteEvent = new CargoDeleteEvent(this, cargoLocation);
                    this.cargoDeleteHandler.handle(cargoDeleteEvent);
                    ConsoleOutputEvent deleteCargoFinal = new ConsoleOutputEvent(this, "delete Cargo final", false);
                    this.coHandler.handle(deleteCargoFinal);
                    break;
                case "exit":
                    break;
                default:
                    ConsoleOutputEvent falscheEingabe = new ConsoleOutputEvent(this, "Falsche Eingabe", true);
                    this.coHandler.handle(falscheEingabe);
            }
        } catch (IOException ie) {
            ConsoleOutputEvent falscheEingabe = new ConsoleOutputEvent(this, "cannot read from input stream", true);
            this.coHandler.handle(falscheEingabe);
            System.exit(0);
        } catch (CustomerException | BusinessLogiceException | CargoException ce) {
            ConsoleOutputEvent falscheEingabe = new ConsoleOutputEvent(this, ce.getLocalizedMessage(), true);
            this.coHandler.handle(falscheEingabe);
        }
    }

    private void displayMode() throws BusinessLogiceException, CustomerException, CargoException {
        try {
            ConsoleOutputEvent d = new ConsoleOutputEvent(this, "display", false);
            this.coHandler.handle(d);

            String cmdLineString = userInput.readLine().trim();
            switch (cmdLineString) {
                case "1":
                    ConsoleOutputEvent customer = new ConsoleOutputEvent(this, "customer display", false);
                    this.coHandler.handle(customer);
                    CustomerDisplayEvent customerDisplay = new CustomerDisplayEvent(this);
                    this.customerDisplayHandler.handle(customerDisplay);
                    break;
                case "2":
                    ConsoleOutputEvent cargoTypeString = new ConsoleOutputEvent(this, "Cargo FrachtType Display", false);
                    this.coHandler.handle(cargoTypeString);
                    String cargoType = userInput.readLine().trim();
                    CargoTypeDisplayEvent cargoTypeDisplayEvent = new CargoTypeDisplayEvent(this, cargoType);
                    this.cargoTypeDisplayEventHandler.handle(cargoTypeDisplayEvent);
                    break;
                case "exit":
                    break;
                case "3":
                    ConsoleOutputEvent hazardDisplay = new ConsoleOutputEvent(this, "Hazard Display", false);
                    this.coHandler.handle(hazardDisplay);
                    String hazard = userInput.readLine().trim();
                    if (hazard.equals("True") || hazard.equals("False")) {
                        HazardDisplayEvent hazardDisplayEvent = new HazardDisplayEvent(this, Boolean.parseBoolean(hazard));
                        hazardDisplayEventHandler.handle(hazardDisplayEvent);
                        break;
                    }
                default:
                    ConsoleOutputEvent falscheEingabe = new ConsoleOutputEvent(this, "Falsche Eingabe", false);
                    this.coHandler.handle(falscheEingabe);
            }
        } catch (IOException ie) {
            ConsoleOutputEvent falscheEingabe = new ConsoleOutputEvent(this, "cannot read from input stream", true);
            this.coHandler.handle(falscheEingabe);
            System.exit(0);
        } catch (BusinessLogiceException | CargoException | CustomerException bl) {
            ConsoleOutputEvent falscheEingabe = new ConsoleOutputEvent(this, bl.getLocalizedMessage(), true);
            this.coHandler.handle(falscheEingabe);
        }
    }

    private void persistenceMode() throws BusinessLogiceException, CustomerException, CargoException, IOException {
        ConsoleOutputEvent i = new ConsoleOutputEvent(this, "Persistence", false);
        this.coHandler.handle(i);

        String cmdLineString = userInput.readLine().trim();
        switch (cmdLineString) {
            case "1":
                ConsoleOutputEvent saveJOS = new ConsoleOutputEvent(this, "SaveJOS", false);
                this.coHandler.handle(saveJOS);
                String filename = userInput.readLine().trim();
                PersistentEvent persistentEvent = new PersistentEvent(this, "saveJOS", filename);
                this.persistenceEventHandler.handle(persistentEvent);
                break;
            case "2":
                ConsoleOutputEvent saveJBP = new ConsoleOutputEvent(this, "SaveJBP", false);
                this.coHandler.handle(saveJBP);
                String filenameJBP = userInput.readLine().trim();
                PersistentEvent persistentEventSaveJBP = new PersistentEvent(this, "saveJBP", filenameJBP);
                this.persistenceEventHandler.handle(persistentEventSaveJBP);
                break;
            case "3":
                ConsoleOutputEvent loadJOS = new ConsoleOutputEvent(this, "LoadJOS", false);
                this.coHandler.handle(loadJOS);
                String filenameJOS = userInput.readLine().trim();
                PersistentEvent persistentEventLoadJOS = new PersistentEvent(this, "loadJOS", filenameJOS);
                this.persistenceEventHandler.handle(persistentEventLoadJOS);
                break;
            case "4":
                ConsoleOutputEvent loadJBP = new ConsoleOutputEvent(this, "LoadJBP", false);
                this.coHandler.handle(loadJBP);
                String filenameloadJBP = userInput.readLine().trim();
                PersistentEvent persistentEventLoadJBP = new PersistentEvent(this, "loadJBP", filenameloadJBP);
                this.persistenceEventHandler.handle(persistentEventLoadJBP);
                break;
            case "5":
                ConsoleOutputEvent saveCargo = new ConsoleOutputEvent(this, "SaveCargo", false);
                this.coHandler.handle(saveCargo);
                String placheString = userInput.readLine().trim();
                CargoPersistentEvent cargoPersistentEvent = new CargoPersistentEvent(this, true, Integer.parseInt(placheString));
                this.cargoPersistenEventHandler.handle(cargoPersistentEvent);
                break;
            case "6":
                ConsoleOutputEvent loadCargo = new ConsoleOutputEvent(this, "LoadCargo", false);
                this.coHandler.handle(loadCargo);
                String plactStringLoad = userInput.readLine().trim();
                CargoPersistentEvent cargoPersistentEventLoad = new CargoPersistentEvent(this, false, Integer.parseInt(plactStringLoad));
                this.cargoPersistenEventHandler.handle(cargoPersistentEventLoad);
            case "exit":
                break;
            default:
                ConsoleOutputEvent falscheEingabe = new ConsoleOutputEvent(this, "Falsche Eingabe", false);
                this.coHandler.handle(falscheEingabe);
        }

    }

    private void configMode() throws IllegalArgumentException, BusinessLogiceException, CustomerException, CargoException, IOException {
        ConsoleOutputEvent i = new ConsoleOutputEvent(this, "Config", false);
        this.coHandler.handle(i);

        String cmdLineString = userInput.readLine().trim();
        switch (cmdLineString) {
            case "1":
                ConsoleOutputEvent j = new ConsoleOutputEvent(this, "Add", false);
                this.coHandler.handle(j);
                ObserverAddEvent observerAddEvent = new ObserverAddEvent(this, this.observerSearch());
                this.observerAddEventHandler.handle(observerAddEvent);
                break;
            case "2":
                ConsoleOutputEvent h = new ConsoleOutputEvent(this, "Remove", false);
                this.coHandler.handle(h);
                ObserverRemoveEvent removeEvent = new ObserverRemoveEvent(this, this.observerSearch());
                this.observerRemoveEventHandler.handle(removeEvent);
                break;
            case "exit":
                break;
            default:
                ConsoleOutputEvent falscheEingabe = new ConsoleOutputEvent(this, "Falsche Eingabe", false);
                this.coHandler.handle(falscheEingabe);
        }
    }

    private void modificationMode() throws BusinessLogiceException, CustomerException, CargoException {
        try {
            ConsoleOutputEvent modificationMode = new ConsoleOutputEvent(this, "Inspetions Datum", false);
            this.coHandler.handle(modificationMode);
            String cargoLocationStr = userInput.readLine().trim();
            if (cargoLocationStr.equals("exit")) {
                return;
            }
            int cargoLocation = Integer.parseInt(cargoLocationStr);
            DateOfInspectionEvent dateOfInspectionEvent = new DateOfInspectionEvent(this, cargoLocation);
            this.dateOfInspectionEventHandler.handle(dateOfInspectionEvent);
            ConsoleOutputEvent modificationModeFinal = new ConsoleOutputEvent(this, "Inspetions Datum final", false);
            this.coHandler.handle(modificationModeFinal);
        } catch (IOException ie) {
            ConsoleOutputEvent falscheEingabe = new ConsoleOutputEvent(this, "cannot read from input stream", true);
            this.coHandler.handle(falscheEingabe);
            System.exit(0);
        } catch (BusinessLogiceException | CustomerException | CargoException ble) {
            ConsoleOutputEvent falscheEingabe = new ConsoleOutputEvent(this, ble.getLocalizedMessage(), true);
            this.coHandler.handle(falscheEingabe);
        }
    }

    private void insertCargo() throws BusinessLogiceException, CustomerException, CargoException {
        boolean mixedCargo = false;
        boolean liguidCargo = false;
        boolean unitisedCargo = false;
        String cargoOwner;
        BigDecimal valueCargo;
        Duration durationOfStorageCargo;
        boolean pressurizedCargo;
        boolean fragileCargo;
        Collection<Hazard> hazardCollection = new HashSet<>();
        String hazardMeber;
        ConsoleOutputEvent insertCargo = new ConsoleOutputEvent(this, "insertCargo", false);
        this.coHandler.handle(insertCargo);
        try {
            String cmdLineString = userInput.readLine();
            StringTokenizer st = new StringTokenizer(cmdLineString);
            if (st.countTokens() < 6) {
                ConsoleOutputEvent falscheEingabe = new ConsoleOutputEvent(this, "Zu wenig Argument", true);
                this.coHandler.handle(falscheEingabe);
                return;

            } else if (st.countTokens() > 13) {
                ConsoleOutputEvent falscheEingabe = new ConsoleOutputEvent(this, "Zu viel Argument", true);
                this.coHandler.handle(falscheEingabe);
                return;

            }
            switch (st.nextToken()) {
                case "UnitisedCargo":
                    unitisedCargo = true;
                    break;
                case "LiquidCargo":
                    liguidCargo = true;
                    break;
                case "MixedCargo":
                    mixedCargo = true;
                    break;
                default:
                    throw new CargoException("Falscher Cargo Type Eingeben.");
            }
            cargoOwner = st.nextToken();
            valueCargo = BigDecimal.valueOf(Double.parseDouble(st.nextToken()));
            durationOfStorageCargo = Duration.ofSeconds(Long.parseLong(st.nextToken()));
            hazardMeber = st.nextToken();
            while (st.hasMoreTokens() && !hazardMeber.equals("N") && !hazardMeber.equals("Y")) {
                if (hazardMeber.equals(",")) {
                    hazardMeber = st.nextToken();
                    continue;
                }
                String hazard = hazardMeber.replaceAll(",", "");
                switch (hazard) {
                    case "explosive":
                        hazardCollection.add(Hazard.explosive);
                        break;
                    case "flammable":
                        hazardCollection.add(Hazard.flammable);
                        break;
                    case "radioactive":
                        hazardCollection.add(Hazard.radioactive);
                        break;
                    case "toxic":
                        hazardCollection.add(Hazard.toxic);
                        break;
                    default:
                        throw new CargoException("Falscher Hazard Type Eingeben.");
                }
                hazardMeber = st.nextToken();
            }
            if (liguidCargo) {
                pressurizedCargo = trueOrFalse(hazardMeber.toLowerCase());
                CargoLiquidInsertEvent liquidInsertEvent = new CargoLiquidInsertEvent(this, cargoOwner, valueCargo, durationOfStorageCargo, hazardCollection, pressurizedCargo);
                this.caLHandler.handle(liquidInsertEvent);
            } else if (unitisedCargo) {
                fragileCargo = trueOrFalse(hazardMeber.toLowerCase());
                CargoUnitisedInsertEvent unitisedInsertEvent = new CargoUnitisedInsertEvent(this, cargoOwner, valueCargo, durationOfStorageCargo, hazardCollection, fragileCargo);
                this.caUHandler.handle(unitisedInsertEvent);
            } else if (mixedCargo) {
                pressurizedCargo = trueOrFalse(hazardMeber.toLowerCase());
                fragileCargo = trueOrFalse(st.nextToken().toLowerCase());
                CargoMixedInsertEvent mixedInsertEvent = new CargoMixedInsertEvent(this, cargoOwner, valueCargo, durationOfStorageCargo, hazardCollection, pressurizedCargo, fragileCargo);
                this.caMHandler.handle(mixedInsertEvent);
            }


        } catch (IOException i) {
            ConsoleOutputEvent falscheEingabe = new ConsoleOutputEvent(this, "cannot read from input stream", true);
            this.coHandler.handle(falscheEingabe);
            System.exit(0);
        } catch (NumberFormatException nfe) {
            ConsoleOutputEvent falscheEingabe = new ConsoleOutputEvent(this, "BigDecimal oder Duration Zahl war falsch.", true);
            this.coHandler.handle(falscheEingabe);
        } catch (DateTimeException dte) {
            ConsoleOutputEvent falscheEingabe = new ConsoleOutputEvent(this, "Duration angaben war falsch.", true);
            this.coHandler.handle(falscheEingabe);
        } catch (CargoException | BusinessLogiceException | CustomerException ce) {
            ConsoleOutputEvent falscheEingabe = new ConsoleOutputEvent(this, ce.getLocalizedMessage(), true);
            this.coHandler.handle(falscheEingabe);
        } catch (NullPointerException npe) {
            ConsoleOutputEvent falscheEingabe = new ConsoleOutputEvent(this, "Keine Eingabe ist nicht Erlaubt", true);
            this.coHandler.handle(falscheEingabe);
        } catch (Exception e) {
            ConsoleOutputEvent falscheEingabe = new ConsoleOutputEvent(this, "True or Flase angaben war falsch ein gbae war falsch", true);
            this.coHandler.handle(falscheEingabe);
        }
    }

    private boolean trueOrFalse(String b) throws Exception {
        switch (b) {
            case "y":
                return true;
            case "n":
                return false;
            default:
                throw new Exception();
        }
    }

    private String observerSearch() throws IllegalArgumentException, IOException {
        String cmdLineString = userInput.readLine();
        switch (cmdLineString) {
            case "1":
                return "CargoDelet";
            case "2":
                return "Hazard";
            case "3":
                return "Storage";
            case "4":
                return "StorageSpace";
            default:
                throw new IllegalArgumentException("False Nummer");

        }
    }

}
