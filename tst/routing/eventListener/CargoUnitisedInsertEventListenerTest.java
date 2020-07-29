package routing.eventListener;

import control.ControlModel;
import model.businessLogice.BusinessLogic;
import model.businessLogice.BusinessLogiceException;
import model.storageContract.administration.CustomerException;
import model.storageContract.cargo.Cargo;
import model.storageContract.cargo.CargoException;
import model.storageContract.cargo.UnitisedCargoImpl;
import org.junit.jupiter.api.Test;
import routing.event.CargoUnitisedInsertEvent;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

class CargoUnitisedInsertEventListenerTest {
    @Test
    public void onEvent_callsTheMehthodePutCargo1() throws CargoException, BusinessLogiceException, CustomerException {
        List list = new LinkedList();
        CargoUnitisedInsertEvent event = new CargoUnitisedInsertEvent(this, "Dennis", BigDecimal.valueOf(15), Duration.ZERO, list, true);
        BusinessLogic businessLogic = spy(new BusinessLogic(4));
        ControlModel controlModel= new ControlModel(businessLogic);
        Cargo cargo = mock(Cargo.class);
        businessLogic.putCustomer("Dennis");
        CargoUnitisedInsertEventListener listener = new CargoUnitisedInsertEventListener(controlModel);
        listener.onEvent(event);
        verify(businessLogic, times(1)).putCargo(isA(UnitisedCargoImpl.class));
    }
}