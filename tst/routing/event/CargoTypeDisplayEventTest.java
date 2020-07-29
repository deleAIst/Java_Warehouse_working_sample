package routing.event;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CargoTypeDisplayEventTest {

    @Test
    public void cargoTypeDisplayEventIntalization() {
        CargoTypeDisplayEvent event = new CargoTypeDisplayEvent(this, "Liquid");
        assertEquals(CargoTypeDisplayEvent.class, event.getClass());
    }

    @Test
    void getType() {
        CargoTypeDisplayEvent event = new CargoTypeDisplayEvent(this, "Liquid");
        assertEquals("Liquid", event.getType());
    }

    @Test
    void getConsoleOutput() {
        CargoTypeDisplayEvent event = new CargoTypeDisplayEvent(this, "Liquid");

    }
}