package routing.eventListener;

import control.ControlModel;
import model.businessLogice.BusinessLogic;
import model.businessLogice.BusinessLogiceException;
import model.storageContract.administration.Customer;
import model.storageContract.administration.CustomerException;
import model.storageContract.cargo.Cargo;
import model.storageContract.cargo.CargoException;
import model.storageContract.cargo.Hazard;
import model.storageContract.cargo.LiquidBulkCargoImpl;
import org.junit.jupiter.api.Test;
import routing.event.DateOfInspectionEvent;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.LinkedList;

import static org.mockito.Mockito.*;

class DateOfInspectionEventListenerTest {

    @Test
    public void onEvent_callsTheMehthoUpdateInspectionDate() throws CustomerException, BusinessLogiceException, CargoException {
        DateOfInspectionEvent event = new DateOfInspectionEvent(this, 0);
        BusinessLogic businessLogic = spy(new BusinessLogic(4));
        ControlModel controlModel= new ControlModel(businessLogic);
        Customer customer =businessLogic.putCustomer("Dennis");
        Cargo cargo = new LiquidBulkCargoImpl(customer, BigDecimal.ZERO, Duration.ZERO,new LinkedList<Hazard>(),true);
        businessLogic.putCargo(cargo);
        DateOfInspectionEventListener listener = new DateOfInspectionEventListener(controlModel);
        listener.onEvent(event);
        verify(businessLogic, times(2)).updateInspectionDate(0);
    }
}