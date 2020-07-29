package routing.eventListener;

import control.ControlModel;
import model.businessLogice.BusinessLogic;
import model.storageContract.administration.CustomerException;
import org.junit.jupiter.api.Test;
import routing.event.CustomerInsertEvent;

import static org.mockito.Mockito.*;

class CustomerInsertEventListenerTest {

    @Test
    public void onEvent_callsTheMehthoPutCustomer() throws CustomerException {
        CustomerInsertEvent event = new CustomerInsertEvent(this, "Dennis");
        BusinessLogic businessLogic = spy(new BusinessLogic(4));
        ControlModel controlModel= new ControlModel(businessLogic);
        CustomerInsertEventListener listener = new CustomerInsertEventListener(controlModel);
        listener.onEvent(event);
        verify(businessLogic, times(1)).putCustomer("Dennis");
    }

}