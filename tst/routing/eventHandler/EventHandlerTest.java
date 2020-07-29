package routing.eventHandler;

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
import routing.eventListener.CargoDeleteEventListener;
import routing.EventListener;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EventHandlerTest {

    @Test
    public void eventHandlerIntalization() {
        EventHandler<CargoDeleteEvent> eventHandler = new EventHandler<>();
        assertEquals(EventHandler.class, eventHandler.getClass());
    }

    @Test
    public void add_god() throws BusinessLogiceException, CustomerException, CargoException {
        BusinessLogic bl = new BusinessLogic(3);
        ControlModel controlModel= new ControlModel(bl);
        Customer customer = bl.putCustomer("Dennis");
        Cargo cargo = new LiquidBulkCargoImpl(customer, BigDecimal.ZERO, Duration.ZERO,new LinkedList<Hazard>(),true);
        bl.putCargo(cargo);
        CargoDeleteEvent event = new CargoDeleteEvent(this, 0);
        EventHandler<CargoDeleteEvent> eventHandler = new EventHandler<>();
        EventListener<CargoDeleteEvent> eventEventListener=spy(new CargoDeleteEventListener(controlModel));
        eventHandler.add(eventEventListener);
        eventHandler.handle(event);
        verify(eventEventListener, times(1)).onEvent(isA(CargoDeleteEvent.class));

    }

    @Test
    public void remove_god() throws BusinessLogiceException, CustomerException, CargoException {
        CargoDeleteEvent event = new CargoDeleteEvent(this, 0);
        EventHandler<CargoDeleteEvent> eventHandler = new EventHandler<>();
        BusinessLogic bl = new BusinessLogic(3);
        ControlModel controlModel= new ControlModel(bl);
        EventListener<CargoDeleteEvent> eventEventListener=spy(new CargoDeleteEventListener(controlModel));
        eventHandler.add(eventEventListener);
        eventHandler.remove(eventEventListener);
        eventHandler.handle(event);
        verify(eventEventListener, times(0)).onEvent(isA(CargoDeleteEvent.class));

    }



}