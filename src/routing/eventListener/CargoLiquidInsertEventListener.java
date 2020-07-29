package routing.eventListener;

import control.ControlModel;
import model.businessLogice.BusinessLogiceException;
import model.storageContract.administration.Customer;
import model.storageContract.cargo.Cargo;
import model.storageContract.cargo.CargoException;
import model.storageContract.cargo.LiquidBulkCargoImpl;
import routing.EventListener;
import routing.event.CargoLiquidInsertEvent;

/**
 * @author Dennis Dominik Lehmann
 * Matrikelnummer 568827
 */
public class CargoLiquidInsertEventListener implements EventListener<CargoLiquidInsertEvent> {

    private final ControlModel model;

    public CargoLiquidInsertEventListener(ControlModel model) {
        this.model = model;
    }

    public void onEvent(CargoLiquidInsertEvent event) throws CargoException, BusinessLogiceException {
        Customer customer = model.getModel().getCustomer(event.getOwner());
        Cargo cargo = new LiquidBulkCargoImpl(customer, event.getValue(), event.getDurationOfStorage(), event.getHazard(), event.getPressurized());
       int location =this.model.getModel().putCargo(cargo);
       System.out.println("Das Cargo liegt an Platz: "+location);

    }
}
