package routing.eventListener;

import control.ControlModel;
import model.businessLogice.BusinessLogiceException;
import model.storageContract.administration.Customer;
import model.storageContract.cargo.Cargo;
import model.storageContract.cargo.CargoException;
import model.storageContract.cargo.MixedCargoLiquidBulkAndUnitisedImpl;
import routing.EventListener;
import routing.event.CargoMixedInsertEvent;


/**
 * @author Dennis Dominik Lehmann
 * Matrikelnummer 568827
 */
public class CargoMixedInsertEventListener implements EventListener<CargoMixedInsertEvent> {

    private final ControlModel model;

    public CargoMixedInsertEventListener(ControlModel model) {
        this.model = model;
    }

    public void onEvent(CargoMixedInsertEvent event) throws CargoException, BusinessLogiceException {
        Customer customer = model.getModel().getCustomer(event.getOwner());
        Cargo cargo = new MixedCargoLiquidBulkAndUnitisedImpl(customer, event.getValue(), event.getDurationOfStorage(), event.getHazard(), event.getPressurized(), event.getFragile());
        int location=this.model.getModel().putCargo(cargo);
        System.out.println("Das Cargo liegt an Platz: "+location);
    }
}
