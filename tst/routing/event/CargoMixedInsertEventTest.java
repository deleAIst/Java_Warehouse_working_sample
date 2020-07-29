package routing.event;

import model.storageContract.cargo.Hazard;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CargoMixedInsertEventTest {

    @Test
    public void cargoMixedInsertEventIntalization() {
        List<Hazard> collection = new ArrayList<>();
        CargoMixedInsertEvent event = new CargoMixedInsertEvent(this, "Dennis", BigDecimal.valueOf(20.5), Duration.ZERO, collection, false, true);
        assertEquals(CargoMixedInsertEvent.class, event.getClass());
    }

    @Test
    void getOwner() {
        List<Hazard> collection = new ArrayList<>();
        CargoMixedInsertEvent event = new CargoMixedInsertEvent(this, "Dennis", BigDecimal.valueOf(20.5), Duration.ZERO, collection, false, true);

        assertEquals("Dennis", event.getOwner());
    }

    @Test
    void getValue() {
        List<Hazard> collection = new ArrayList<>();
        CargoMixedInsertEvent event = new CargoMixedInsertEvent(this, "Dennis", BigDecimal.valueOf(20.5), Duration.ZERO, collection, false, true);
        assertEquals(BigDecimal.valueOf(20.5), event.getValue());
    }

    @Test
    void getHazard() {
        List<Hazard> collection = new ArrayList<>();
        collection.add(Hazard.radioactive);
        CargoMixedInsertEvent event = new CargoMixedInsertEvent(this, "Dennis", BigDecimal.valueOf(20.5), Duration.ZERO, collection, false, true);
        assertEquals(Hazard.radioactive, event.getHazard().iterator().next());
    }

    @Test
    void getDurationOfStorage() {
        List<Hazard> collection = new ArrayList<>();
        CargoMixedInsertEvent event = new CargoMixedInsertEvent(this, "Dennis", BigDecimal.valueOf(20.5), Duration.ZERO, collection, false, true);
        assertEquals(Duration.ZERO, event.getDurationOfStorage());
    }

    @Test
    void getPressurized() {
        List<Hazard> collection = new ArrayList<>();
        CargoMixedInsertEvent event = new CargoMixedInsertEvent(this, "Dennis", BigDecimal.valueOf(20.5), Duration.ZERO, collection, false, true);
        assertFalse(event.getPressurized());
    }

@Test
    void getFragile(){
    List<Hazard> collection = new ArrayList<>();
    CargoMixedInsertEvent event = new CargoMixedInsertEvent(this, "Dennis", BigDecimal.valueOf(20.5), Duration.ZERO, collection, false, true);
    assertTrue(event.getFragile());
}

}
