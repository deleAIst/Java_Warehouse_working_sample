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
import routing.event.CargoDeleteEvent;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.LinkedList;

import static org.mockito.Mockito.*;

class CargoDeleteEventListenerTest {

    @Test
    public void onEvent_callsTheMehthodeDeleteCustomer1() throws CargoException, CustomerException, BusinessLogiceException {
        CargoDeleteEvent cargoDeleteEvent= new CargoDeleteEvent(this, 0);
        BusinessLogic businessLogic= spy(new BusinessLogic(4));
        Customer customer =businessLogic.putCustomer("Dennis");
        ControlModel controlModel= new ControlModel(businessLogic);
        Cargo cargo = new LiquidBulkCargoImpl(customer, BigDecimal.ZERO, Duration.ZERO,new LinkedList<Hazard>(),true);
        businessLogic.putCargo(cargo);
        CargoDeleteEventListener cargoDeleteEventListener = new CargoDeleteEventListener(controlModel);
        cargoDeleteEventListener.onEvent(cargoDeleteEvent);
        verify(businessLogic, times(1)).deletCargo(0);

    }


}