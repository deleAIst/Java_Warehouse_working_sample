package routing.eventListener;

import control.ControlModel;
import model.storageContract.cargo.Cargo;
import routing.EventListener;
import routing.event.CustomerDisplayEvent;
import view.ViewCLI;

import java.util.Map;

/**
 * @author Dennis Dominik Lehmann
 * Matrikelnummer 568827
 */
public class CustomerDisplayEventListener implements EventListener<CustomerDisplayEvent> {

    private final ControlModel model;
    private final ViewCLI view;

    public CustomerDisplayEventListener(ControlModel model, ViewCLI view) {
        this.model = model;
        this.view=view;
    }

    public void onEvent(CustomerDisplayEvent event) {
        Map alleCustomer = model.getModel().getAlleCustomer();
        Cargo[] cargoArray = this.model.getModel().getAlleCargo();
        int value = 0;
        for (Object key : alleCustomer.keySet()) {
            for (Cargo cargo : cargoArray) {
                if (cargo != null) {
                    if (cargo.getOwner().getName() == key) {
                        value = value + 1;
                    }
                }
            }
            this.view.typeDisplay("Der Customer " + key + " hat " + value + " Cargos im Lager.");
            value = 0;
        }
    }
}
