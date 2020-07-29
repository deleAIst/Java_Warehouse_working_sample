package routing.eventListener;

import control.ControlModel;
import model.storageContract.administration.CustomerException;
import routing.EventListener;
import routing.event.CustomerDeleteEvent;

/**
 * @author Dennis Dominik Lehmann
 * Matrikelnummer 568827
 */
public class CustomerDeleteEventListener implements EventListener<CustomerDeleteEvent> {
    private final ControlModel model;

    public CustomerDeleteEventListener(ControlModel model){
        this.model=model;
    }

    public void onEvent(CustomerDeleteEvent event) throws CustomerException {
        boolean e=this.model.getModel().deletCustomer(event.getName());
        if(!e){
            throw new CustomerException("Customer ist nicht in der Verwaltung.");
        }
    }
}
