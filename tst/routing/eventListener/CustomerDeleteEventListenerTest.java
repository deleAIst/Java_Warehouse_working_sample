package routing.eventListener;

import control.ControlModel;
import model.businessLogice.BusinessLogic;
import model.businessLogice.BusinessLogiceException;
import model.storageContract.administration.CustomerException;
import model.storageContract.cargo.CargoException;
import org.junit.jupiter.api.Test;
import routing.event.CustomerDeleteEvent;

import static org.mockito.Mockito.*;

class CustomerDeleteEventListenerTest {

    @Test
    public void onEvent_callsTheMehthoDedeletCustomer() throws CargoException, BusinessLogiceException, CustomerException {
       CustomerDeleteEvent event = new CustomerDeleteEvent(this, "Dennis");
        BusinessLogic businessLogic = spy(new BusinessLogic(4));
        ControlModel controlModel= new ControlModel(businessLogic);
        businessLogic.putCustomer("Dennis");
        CustomerDeleteEventListener listener = new CustomerDeleteEventListener(controlModel);
        listener.onEvent(event);
        verify(businessLogic, times(1)).deletCustomer("Dennis");
    }

}