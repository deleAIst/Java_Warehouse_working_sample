package routing.event;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HazardDisplayEventTest {
    @Test
    public void HazardDisplayEventIntalization() {
        HazardDisplayEvent hazardDisplayEvent = new HazardDisplayEvent(this, true);
        assertEquals(HazardDisplayEvent.class,hazardDisplayEvent.getClass());
    }

    @Test
    public void getHazard_SameOutputAsInput() {
        HazardDisplayEvent hazardDisplayEvent = new HazardDisplayEvent(this, true);
        assertEquals(true, hazardDisplayEvent.isIncluded());
    }

    @Test
    public void isIncluded_SameOutputAsInput() {
        HazardDisplayEvent hazardDisplayEvent = new HazardDisplayEvent(this, true);
        assertTrue(hazardDisplayEvent.isIncluded());
    }
}