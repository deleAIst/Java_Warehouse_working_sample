package routing.event;

import model.storageContract.cargo.Hazard;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CargoUnitisedInsertEventTest {

    @Test
    public void  cargoUnitisedInsertEventIntalization() {
        List<Hazard> collection= new ArrayList<>();
        CargoUnitisedInsertEvent event =new CargoUnitisedInsertEvent(this,"Dennis", BigDecimal.valueOf(20.5), Duration.ZERO, collection, true);
        assertEquals(CargoUnitisedInsertEvent.class, event.getClass());
    }

    @Test
    void getOwner() {
        List<Hazard> collection= new ArrayList<>();
        CargoUnitisedInsertEvent event =new CargoUnitisedInsertEvent(this,"Dennis", BigDecimal.valueOf(20.5), Duration.ZERO, collection, true);
        assertEquals("Dennis", event.getOwner());
    }

    @Test
    void getValue() {
        List<Hazard> collection= new ArrayList<>();
        CargoUnitisedInsertEvent event =new CargoUnitisedInsertEvent(this,"Dennis", BigDecimal.valueOf(20.5), Duration.ZERO, collection, true);
        assertEquals(BigDecimal.valueOf(20.5), event.getValue());
    }

    @Test
    void getHazard() {
        List<Hazard> collection= new ArrayList<>();
        collection.add(Hazard.radioactive);
        CargoUnitisedInsertEvent event =new CargoUnitisedInsertEvent(this,"Dennis", BigDecimal.valueOf(20.5), Duration.ZERO, collection, true);
        assertEquals(Hazard.radioactive, event.getHazard().iterator().next());
    }

    @Test
    void getDurationOfStorage() {
        List<Hazard> collection= new ArrayList<>();
        CargoUnitisedInsertEvent event =new CargoUnitisedInsertEvent(this,"Dennis", BigDecimal.valueOf(20.5), Duration.ZERO, collection, true);
        assertEquals(Duration.ZERO, event.getDurationOfStorage());
    }

    @Test
    void getPressurized() {
        List<Hazard> collection= new ArrayList<>();
        CargoUnitisedInsertEvent event =new CargoUnitisedInsertEvent(this,"Dennis", BigDecimal.valueOf(20.5), Duration.ZERO, collection, true);
        assertTrue(event.getFragile());
    }
}