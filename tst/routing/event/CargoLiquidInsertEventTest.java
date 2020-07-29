package routing.event;

import model.storageContract.cargo.Hazard;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class CargoLiquidInsertEventTest {

    @Test
    public void cargoLiquidInsertEventIntalization() {
        List<Hazard> collection= new ArrayList<>();
        CargoLiquidInsertEvent event =new CargoLiquidInsertEvent(this,"Dennis", BigDecimal.valueOf(20.5), Duration.ZERO, collection,false);
        assertEquals(CargoLiquidInsertEvent.class, event.getClass());
    }

    @Test
    void getOwner() {
        List<Hazard> collection= new ArrayList<>();
        CargoLiquidInsertEvent event =new CargoLiquidInsertEvent(this,"Dennis", BigDecimal.valueOf(20.5), Duration.ZERO, collection,false);
        assertEquals("Dennis", event.getOwner());
    }

    @Test
    void getValue() {
        List<Hazard> collection= new ArrayList<>();
        CargoLiquidInsertEvent event =new CargoLiquidInsertEvent(this,"Dennis", BigDecimal.valueOf(20.5), Duration.ZERO, collection,false);
        assertEquals(BigDecimal.valueOf(20.5), event.getValue());
    }

    @Test
    void getHazard() {
        List<Hazard> collection= new ArrayList<>();
        collection.add(Hazard.radioactive);
        CargoLiquidInsertEvent event =new CargoLiquidInsertEvent(this,"Dennis", BigDecimal.valueOf(20.5), Duration.ZERO, collection,false);
        assertEquals(Hazard.radioactive, event.getHazard().iterator().next());
    }

    @Test
    void getDurationOfStorage() {
        List<Hazard> collection= new ArrayList<>();
        CargoLiquidInsertEvent event =new CargoLiquidInsertEvent(this,"Dennis", BigDecimal.valueOf(20.5), Duration.ZERO, collection,false);
        assertEquals(Duration.ZERO, event.getDurationOfStorage());
    }

    @Test
    void getPressurized() {
        List<Hazard> collection= new ArrayList<>();
        CargoLiquidInsertEvent event =new CargoLiquidInsertEvent(this,"Dennis", BigDecimal.valueOf(20.5), Duration.ZERO, collection,false);
        assertFalse(event.getPressurized());
    }
}