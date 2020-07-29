package model.businessLogice;

import model.storageContract.administration.Customer;
import model.storageContract.administration.CustomerException;
import model.storageContract.administration.CustomerImpl;
import model.storageContract.cargo.*;
import view.Observer;

import java.io.Serializable;
import java.util.*;

/**
 * @author Dennis Dominik Lehmann
 */
public class BusinessLogic implements Subject, Serializable {

    /**
     * Das Array mit den Cargos
     */
    private Cargo[] cargoStorage;

    /**
     * Die Map mit den Customers
     */
    private Map<String, Customer> customerStorage;

    private final List<Observer> observerList = new ArrayList<Observer>();

    public BusinessLogic() {
    }

    public BusinessLogic(int storageValue) {
        this.cargoStorage = new Cargo[storageValue];
        this.customerStorage = new HashMap<String, Customer>();
    }

    public Cargo[] getCargoStorage() {
        return cargoStorage;
    }

    public void setCargoStorage(Cargo[] cargoStorage) {
        this.cargoStorage = cargoStorage;
    }

    public Map<String, Customer> getCustomerStorage() {
        return customerStorage;
    }

    public void setCustomerStorage(Map<String, Customer> customerStorage) {
        this.customerStorage = customerStorage;
    }

    public List<Observer> getObserverList() {
        return observerList;
    }

    /**
     * Gibt ein Cargo objekt in das Array
     *
     * @param cargo Objekt was in das Array gepakt werden soll.
     * @return die size an dem das Objekt Plaziert wurde.
     * @throws BusinessLogiceException Wenn es keinen Freihen Platz mehr gibt.
     * @throws CargoException          wenn das Objekt keinen Owner hat oder es nicht zu den Cargo Typen passt.
     */
    public int putCargo(Cargo cargo) throws BusinessLogiceException, CargoException {

        int size = this.searchEmptyField();


        if (size == -1) {
            throw new BusinessLogiceException("Kein Freien Platz mehr.");
        }
        if (cargo.getOwner() == null || this.getCustomer(cargo.getOwner().getName()) == null) {
            throw new CargoException("Das Cargo hat keine Owner.");
        }

        if(cargo.getHazards()==null){
            throw new BusinessLogiceException("Hazards dürfen nicht Null sein.");
        }
        /*
         * Insperiert von https://stackoverflow.com/questions/7526817/use-of-instanceof-in-java
         */
        if (cargo instanceof LiquidBulkCargoImpl) {
            LiquidBulkCargoImpl liquidBulkCargo = (LiquidBulkCargoImpl) cargo;
            liquidBulkCargo.setStorageDate(new java.util.Date());
            liquidBulkCargo.setStorageSize(size);

        } else if (cargo instanceof UnitisedCargoImpl) {
            UnitisedCargoImpl unitisedCargo = (UnitisedCargoImpl) cargo;
            unitisedCargo.setStorageDate(new java.util.Date());
            unitisedCargo.setStorageSize(size);
        } else if (cargo instanceof MixedCargoLiquidBulkAndUnitisedImpl) {
            MixedCargoLiquidBulkAndUnitisedImpl mixedCargoLiquidBulkAndUnitised = (MixedCargoLiquidBulkAndUnitisedImpl) cargo;
            mixedCargoLiquidBulkAndUnitised.setStorageDate(new java.util.Date());
            mixedCargoLiquidBulkAndUnitised.setStorageSize(size);
        } else {
            throw new CargoException("Es gibt diesen Cargo Typen nicht");
        }
        cargoStorage[size] = cargo;
        this.updateInspectionDate(size);
        this.notifySubjects();
        return size;
    }

    /**
     * Gibt das gesuchte Cargo Objekt zurück
     *
     * @param size vom gesuchten Cargo Objket im Array
     * @return das Cargo Objekt
     * @throws BusinessLogiceException Wenn die Size auserhalb der Array größe liegt.
     */
    public Cargo getCargo(int size) throws BusinessLogiceException {
        if (this.inValueRange(size)) {
            return this.cargoStorage[size];
        }
        throw new BusinessLogiceException("Die Size wurde zu groß gewählt");
    }

    /**
     * Gibt das Array mit den Cargo zurück
     *
     * @return Array mit Cargo
     */
    public Cargo[] getAlleCargo() {
        return this.cargoStorage;
    }

    /**
     * Löscht das Cargo Objekt aus dem Array.
     *
     * @param size im Array des zu löschen den Cargo Objekt.
     * @return true wenn das Cargo Objekt gelöscht wurde und false wenn es kein Objekt an der Stelle gab oder die Size auserhalb der Array größe ist.
     */
    public boolean deletCargo(int size) {
        if (this.inValueRange(size) && this.cargoStorage[size] != null) {
            this.cargoStorage[size] = null;
            this.notifySubjects();
            return true;
        }
        return false;
    }

    /**
     * Erstelt einen Neuen Customer und gibt diesen in Map. Der Name des Customer ist der Key.
     *
     * @param name Des zu erstellen Customer.
     * @return Objekt des Customers
     * @throws CustomerException wenn es Schon  ein Cutomer mit dem gleichen Namen gibt.
     */
    public Customer putCustomer(String name) throws CustomerException {
        Customer customer;
        if (customerStorage.containsKey(name)) {
            throw new CustomerException("Name schon vorhanden in der Verwaltung geben sie eine andern an");
        } else {
            customer = new CustomerImpl(name);
            this.customerStorage.put(customer.getName(), customer);
        }
        this.notifySubjects();
        return customer;
    }

    /**
     * Gibt das Customer Objekt zurück was gesucht wurde.
     *
     * @param name des gesuchten Customer
     * @return Customer Objekt
     */
    public Customer getCustomer(String name) {
        return this.customerStorage.getOrDefault(name, null);
    }

    /**
     * Gibt die Map mit den Customer zurück
     *
     * @return Map mit Custermor
     */
    public Map getAlleCustomer() {
        return this.customerStorage;
    }

    /**
     * Löscht den Custermer aus der Map und die dazu gehörigen Cargos.
     *
     * @param name des zu löschen den Custermer
     * @return true wenn der Customer gelöscht wurde und False wenn es den Namen in der Map nicht gibt.
     */
    public boolean deletCustomer(String name) {

        if (this.getCustomer(name) != null) {
            this.deleteAlleSameName(name);
            customerStorage.remove(name);
            this.notifySubjects();
            return true;
        } else {
            return false;
        }

    }

    /**
     * Update das InspectionDate von einem Cargo Objekt
     *
     * @param size an dem das Cargo Objekt Liegt
     * @throws BusinessLogiceException wenn die Size nicht im Array vorhanden ist.
     */
    public void updateInspectionDate(int size) throws BusinessLogiceException {
        if (this.inValueRange(size)) {
            if (this.cargoStorage[size] instanceof LiquidBulkCargoImpl) {
                LiquidBulkCargoImpl liquidBulkCargo = (LiquidBulkCargoImpl) this.cargoStorage[size];
                liquidBulkCargo.setLastInspectionDate(new java.util.Date());
            } else if (this.cargoStorage[size] instanceof UnitisedCargoImpl) {
                UnitisedCargoImpl unitisedCargo = (UnitisedCargoImpl) this.cargoStorage[size];
                unitisedCargo.setLastInspectionDate(new java.util.Date());
            } else if (this.cargoStorage[size] instanceof MixedCargoLiquidBulkAndUnitisedImpl) {
                MixedCargoLiquidBulkAndUnitisedImpl mixedCargoLiquidBulkAndUnitised = (MixedCargoLiquidBulkAndUnitisedImpl) this.cargoStorage[size];
                mixedCargoLiquidBulkAndUnitised.setLastInspectionDate(new java.util.Date());
            }else {
                throw new BusinessLogiceException("Auf dem Platz ist kein Cargo Eingelagert");
            }
        } else {
            throw new BusinessLogiceException("Die Size gibt es nicht im Array");
        }
    }

    public List<Cargo> filterHazard(Hazard hazard) {
        List<Cargo> hazardCargo = new LinkedList<>();
        for (Cargo cargo : this.cargoStorage) {
            if (cargo != null) {
                if (cargo.getHazards().contains(hazard)) {
                    hazardCargo.add(cargo);
                }
            }
        }
        return hazardCargo;
    }

    public List<Cargo> filterCargoType(String cargoType) throws BusinessLogiceException {
        List<Cargo> cargoList = new LinkedList<>();
        for (Cargo cargo : this.cargoStorage) {
            switch (cargoType) {
                case "Liquid":
                    if (cargo instanceof LiquidBulkCargoImpl) {
                        cargoList.add(cargo);
                    }
                    break;
                case "Unitised":
                    if (cargo instanceof UnitisedCargoImpl) {
                        cargoList.add(cargo);
                    }
                    break;
                case "Mixed":
                    if (cargo instanceof MixedCargoLiquidBulkAndUnitisedImpl) {
                        cargoList.add(cargo);
                    }
                    break;
                default:
                    throw new BusinessLogiceException("Der Cargo Type ist nicht vorhanden");
            }
        }
        return cargoList;
    }

    public Boolean isFull() {
        int counter = 0;
        for (Cargo cargo : cargoStorage) {
            if (cargo != null) {
                counter++;
            }
        }
        return counter == cargoStorage.length;
    }

    @Override
    public void login(Observer observer) {
        this.observerList.add(observer);
    }

    @Override
    public void logOff(Observer observer) {
        this.observerList.remove(observer);
    }

    @Override
    public void notifySubjects() {
        for (Observer b : this.observerList) b.update();
    }

    private int searchEmptyField() {
        int size = 0;
        for (Cargo i : this.cargoStorage) {
            if (i == null) {
                return size;
            }
            size++;
        }
        return -1;
    }

    private boolean inValueRange(int size) {
        return size < this.cargoStorage.length && size >= 0;
    }

    private void deleteAlleSameName(String name) {
        for (int i = 0; i < this.cargoStorage.length; i++) {
            if (this.cargoStorage[i] != null) {
                if (this.cargoStorage[i].getOwner().getName().equals(name)) {
                    this.cargoStorage[i] = null;
                }
            }
        }
    }


}
