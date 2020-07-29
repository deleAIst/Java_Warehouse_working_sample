package routing.eventListener;

import control.ControlModel;
import model.businessLogice.BusinessLogiceException;
import model.storageContract.administration.Customer;
import model.storageContract.cargo.Cargo;
import model.storageContract.cargo.CargoException;
import model.storageContract.cargo.UnitisedCargoImpl;
import routing.EventListener;
import routing.event.CargoUnitisedInsertEvent;

/**
 * @author Dennis Dominik Lehmann
 * Matrikelnummer 568827
 */public class CargoUnitisedInsertEventListener implements EventListener<CargoUnitisedInsertEvent> {
    private final ControlModel model;

    public CargoUnitisedInsertEventListener(ControlModel model) {
        this.model = model;
    }

    public void onEvent(CargoUnitisedInsertEvent event) throws CargoException, BusinessLogiceException {
        Customer customer = model.getModel().getCustomer(event.getOwner());
        Cargo cargo = new UnitisedCargoImpl(customer, event.getValue(), event.getDurationOfStorage(), event.getHazard(), event.getFragile());
        int location=this.model.getModel().putCargo(cargo);
        System.out.println("Das Cargo liegt an Platz: "+location);
    }
}
