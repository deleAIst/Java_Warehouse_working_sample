package routing.event;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerDeleteEventTest {


    @Test
    public void CustomerDeleteEventIntalization() {
        CustomerDeleteEvent customerDeleteEvent = new CustomerDeleteEvent(this, "Dennis");
        assertEquals(CustomerDeleteEvent.class, customerDeleteEvent.getClass());
    }

    @Test
    public void getName_SameOutputAsInput() {
        CustomerDeleteEvent customerDeleteEvent = new CustomerDeleteEvent(this, "Dennis");
        assertEquals("Dennis", customerDeleteEvent.getName());
    }

}