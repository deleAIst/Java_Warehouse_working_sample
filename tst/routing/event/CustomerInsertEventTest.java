package routing.event;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerInsertEventTest {

    @Test
    public void CustomerInsertEventIntalization() {
       CustomerInsertEvent customerInsertEvent = new CustomerInsertEvent(this, "Dennis");
        assertEquals(CustomerInsertEvent.class, customerInsertEvent.getClass());
    }

    @Test
    public void getName_SameOutputAsInput() {
        CustomerInsertEvent customerInsertEvent = new CustomerInsertEvent(this, "Dennis");
        assertEquals("Dennis", customerInsertEvent.getName());
    }
}