package routing.event;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DateOfInspectionEventTest {

    @Test
    public void DateOfInspectionEventIntalization() {
        DateOfInspectionEvent dateOfInspectionEvent= new DateOfInspectionEvent(this, 12);
        assertEquals(DateOfInspectionEvent.class, dateOfInspectionEvent.getClass());
    }

    @Test
    public void getStorageLocation_SameOutputAsInput() {
        DateOfInspectionEvent dateOfInspectionEvent= new DateOfInspectionEvent(this, 12);
        assertEquals(12, dateOfInspectionEvent.getStorageLocation());
    }

}