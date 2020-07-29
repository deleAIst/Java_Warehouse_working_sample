package routing.event;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CargoDeleteEventTest {

    @Test
    public void cargoDeleteEventIntalization() {
        CargoDeleteEvent cargoDeleteEvent = new CargoDeleteEvent(this, 5);
        assertEquals(CargoDeleteEvent.class, cargoDeleteEvent.getClass());
    }

    @Test
    public void getStorageLocation_SameOutputAsInput() {
        CargoDeleteEvent cargoDeleteEvent = new CargoDeleteEvent(this, 11);
        assertEquals(11, cargoDeleteEvent.getStorageLocation());
    }
}