package control;

import model.businessLogice.BusinessLogic;
import model.businessLogice.BusinessLogiceException;
import model.storageContract.administration.CustomerException;
import model.storageContract.cargo.CargoException;
import routing.event.*;
import routing.eventHandler.EventHandler;
import routing.eventListener.*;
import view.LogObserver;
import view.Observer;
import view.ViewCLI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * @author Dennis Dominik Lehmann
 */
public class GeneralSettings {
    private ControlModel controlModel;

    public void setup(){
        int capacity;
        String filename;
        String language;
        boolean again = true;
        while (again) {
            try {
                System.out.println("Bitte geben sie hier ihre Start ein stellungen an.\n[Lagerkapzität],[Log filename],[Log Sprache en/de]");
                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
                String settings = userInput.readLine();
                if (settings == null) break;
                settings=settings.toLowerCase().trim();
                StringTokenizer parameter = new StringTokenizer(settings, ",");
                if (parameter.countTokens() < 1) {
                    throw new IllegalArgumentException("Zu wennig Argumente eingegeben.");
                }
                capacity = Integer.parseInt(parameter.nextToken().trim());
                if(capacity>0){
                    this.controlModel = new ControlModel(new BusinessLogic(capacity));
                }else {
                    throw new IllegalArgumentException("Die Capacity ist 0 oder Negativ das ist nicht Erlaubt.");
                }

                if (parameter.countTokens() == 2) {
                    filename = parameter.nextToken().trim();
                    language = parameter.nextToken().trim();
                    if (!language.equals("en") && !language.equals("de")) {
                        controlModel.setModel(null);
                        throw new IllegalArgumentException("Es wurde keine der unterstüzten sprachen ausgewählt.");
                    }
                    Observer log = new LogObserver(language, filename, controlModel);
                    controlModel.getModel().login(log);
                }
                if(parameter.hasMoreTokens()){
                    controlModel.setModel(null);
                    throw new IllegalArgumentException("Zu wennig oder zu viele Argumente Argumente eingegeben.");
                }
                this.cli();
                again = false;

            } catch (IllegalArgumentException iae) {
                System.out.println(iae.getMessage());
            } catch (IOException ie) {
                System.out.println(ie.getMessage());
                System.exit(111);
            } catch (NullPointerException nfe) {
                controlModel.setModel(null);
                System.out.println(nfe.getMessage());
            }catch (NoSuchFieldError nsfe){
                controlModel.setModel(null);
                System.out.println("Nicht genug elemente.");
            }
        }
    }

    private void cli() {
        try {
            ViewCLI cliView = new ViewCLI(System.out);
            EventHandler<CargoDeleteEvent> eventHandlerCargoDelete = new EventHandler<>();
            EventHandler<CargoLiquidInsertEvent> eventHandlerCargoLiquid = new EventHandler<>();
            EventHandler<CargoMixedInsertEvent> cargoMixedInsertEventEventHandler = new EventHandler<>();
            EventHandler<CargoUnitisedInsertEvent> cargoUnitisedInsertEventEventHandler = new EventHandler<>();
            EventHandler<CargoTypeDisplayEvent> cargoTypeDisplayEventEventHandler = new EventHandler<>();
            EventHandler<HazardDisplayEvent> hazardDisplayEventEventHandler = new EventHandler<>();
            EventHandler<CustomerDeleteEvent> customerDeleteEventHandler = new EventHandler<>();
            EventHandler<CustomerInsertEvent> customerInsertEventHandler = new EventHandler<>();
            EventHandler<CustomerDisplayEvent> customerDisplayEventEventHandler = new EventHandler<>();
            EventHandler<ConsoleOutputEvent> consoleOutputEventEventHandler = new EventHandler<>();
            EventHandler<DateOfInspectionEvent> dateOfInspectionEventHandler = new EventHandler<>();
            EventHandler<PersistentEvent> persistentEventEventHandler = new EventHandler<>();
            EventHandler<ObserverAddEvent> observerAddEventEventHandler = new EventHandler<>();
            EventHandler<ObserverRemoveEvent> removeEventEventHandler = new EventHandler<>();
            EventHandler<HazardOutputEvent> hazardOutputEventEventHandler = new EventHandler<>();
            EventHandler<CargoPersistentEvent> cargoPersistentEventEventHandler = new EventHandler<>();

            HazardOutputEventListener hazardOutputEventListener = new HazardOutputEventListener(cliView);
            hazardOutputEventEventHandler.add(hazardOutputEventListener);
            CustomerInsertEventListener customerInsertEventListener = new CustomerInsertEventListener(controlModel);
            CargoUnitisedInsertEventListener cargoUnitisedInsertEventListener = new CargoUnitisedInsertEventListener(controlModel);
            CargoMixedInsertEventListener cargoMixedInsertEventListener = new CargoMixedInsertEventListener(controlModel);
            CargoLiquidInsertEventListener cargoLiquidInsertEventListener = new CargoLiquidInsertEventListener(controlModel);
            CustomerDisplayEventListener customerDisplayEventListener = new CustomerDisplayEventListener(controlModel, cliView);
            CargoTypeDisplayEventListener cargoTypeDisplayEventListener = new CargoTypeDisplayEventListener(controlModel, cliView);
            HazardDisplayEventListener hazardDisplayEventListener = new HazardDisplayEventListener(controlModel, hazardOutputEventEventHandler);
            CargoDeleteEventListener cargoDeleteEventListener = new CargoDeleteEventListener(controlModel);
            CustomerDeleteEventListener customerDeleteEventListener = new CustomerDeleteEventListener(controlModel);
            DateOfInspectionEventListener dateOfInspectionEventListener = new DateOfInspectionEventListener(controlModel);
            ConsoleOutputEventListener consoleOutputEventListener = new ConsoleOutputEventListener(cliView);
            PersistendEventListener persistendEventListener = new PersistendEventListener(controlModel);
            ObserverAddEventListener observerAddEventListener = new ObserverAddEventListener(controlModel);
            ObserverRemoveEventListener observerRemoveEventListener = new ObserverRemoveEventListener(controlModel);
            CargoPersistendEventListener cargoPersistendEventListener = new CargoPersistendEventListener(controlModel);

            eventHandlerCargoDelete.add(cargoDeleteEventListener);
            eventHandlerCargoLiquid.add(cargoLiquidInsertEventListener);
            cargoMixedInsertEventEventHandler.add(cargoMixedInsertEventListener);
            cargoUnitisedInsertEventEventHandler.add(cargoUnitisedInsertEventListener);
            cargoTypeDisplayEventEventHandler.add(cargoTypeDisplayEventListener);
            hazardDisplayEventEventHandler.add(hazardDisplayEventListener);
            customerDeleteEventHandler.add(customerDeleteEventListener);
            customerDisplayEventEventHandler.add(customerDisplayEventListener);
            customerInsertEventHandler.add(customerInsertEventListener);
            consoleOutputEventEventHandler.add(consoleOutputEventListener);
            dateOfInspectionEventHandler.add(dateOfInspectionEventListener);
            persistentEventEventHandler.add(persistendEventListener);
            observerAddEventEventHandler.add(observerAddEventListener);
            removeEventEventHandler.add(observerRemoveEventListener);
            cargoPersistentEventEventHandler.add(cargoPersistendEventListener);


            CLI cli = new CLI(System.in, customerInsertEventHandler, customerDeleteEventHandler, customerDisplayEventEventHandler, eventHandlerCargoLiquid,
                    cargoMixedInsertEventEventHandler, cargoUnitisedInsertEventEventHandler, eventHandlerCargoDelete, cargoTypeDisplayEventEventHandler,
                    consoleOutputEventEventHandler, dateOfInspectionEventHandler, hazardDisplayEventEventHandler, persistentEventEventHandler,
                    observerAddEventEventHandler, removeEventEventHandler, cargoPersistentEventEventHandler);
            cli.runCLI();
        } catch (NumberFormatException n) {
            System.out.println("Das ist keiner der Drei Formate bitte noch mal Eingeben.");
        } catch (CargoException | CustomerException | BusinessLogiceException e) {
            e.printStackTrace();
        }
    }

}
