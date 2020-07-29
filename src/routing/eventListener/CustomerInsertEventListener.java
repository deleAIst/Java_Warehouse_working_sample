package routing.eventListener;

import control.ControlModel;
import model.storageContract.administration.CustomerException;
import routing.EventListener;
import routing.event.CustomerInsertEvent;

/**
 * @author Dennis Dominik Lehmann
 * Matrikelnummer 568827
 */
public class CustomerInsertEventListener implements EventListener<CustomerInsertEvent> {

    private final ControlModel model;

    public CustomerInsertEventListener(ControlModel model){
        this.model=model;
    }

    public void onEvent(CustomerInsertEvent event) throws CustomerException {
            model.getModel().putCustomer(event.getName());
    }
}
