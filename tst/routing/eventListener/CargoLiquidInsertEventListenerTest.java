package routing.eventListener;

import control.ControlModel;
import model.businessLogice.BusinessLogic;
import model.businessLogice.BusinessLogiceException;
import model.storageContract.administration.CustomerException;
import model.storageContract.cargo.Cargo;
import model.storageContract.cargo.CargoException;
import model.storageContract.cargo.LiquidBulkCargoImpl;
import org.junit.jupiter.api.Test;
import routing.event.CargoLiquidInsertEvent;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.*;

class CargoLiquidInsertEventListenerTest {

    @Test
    public void onEvent_callsTheMehthodePutCargo1() throws CargoException, BusinessLogiceException, CustomerException {
        List list = new LinkedList();
        CargoLiquidInsertEvent event = new CargoLiquidInsertEvent(this, "Dennis", BigDecimal.valueOf(15), Duration.ZERO, list, true);
        BusinessLogic businessLogic = spy(new BusinessLogic(4));
        ControlModel controlModel= new ControlModel(businessLogic);
        Cargo cargo = mock(Cargo.class);
        businessLogic.putCustomer("Dennis");
        CargoLiquidInsertEventListener listener = new CargoLiquidInsertEventListener(controlModel);
        listener.onEvent(event);
        verify(businessLogic, times(1)).putCargo(isA(LiquidBulkCargoImpl.class));
    }

}