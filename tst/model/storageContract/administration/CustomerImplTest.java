package model.storageContract.administration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Dennis Dominik Lehmann
 */
class CustomerImplTest {
    @Test
    public void goodIntalization() throws Exception {
        Customer customer = new CustomerImpl("Dennis");
        assertEquals(CustomerImpl.class, customer.getClass());

    }

    @Test
    public void goodIntalization_noName() throws Exception {
        Customer customer = new CustomerImpl();
        assertEquals(CustomerImpl.class, customer.getClass());

    }

    @Test
    public void badIntalization() throws Exception {
        Assertions.assertThrows(Exception.class, () -> {
            new CustomerImpl(null);
        });
    }

    @Test
    public void setValue() throws CustomerException {
        CustomerImpl customer = new CustomerImpl("Dennis");
        customer.setMaxValue(BigDecimal.valueOf(20.0));
        assertEquals(BigDecimal.valueOf(20.0), customer.getMaxValue());
    }

    @Test
    public void setDuration() throws CustomerException {
        CustomerImpl customer = new CustomerImpl("Dennis");
        customer.setMaxDurationOfStorage(Duration.ofMinutes(1));
        assertEquals(Duration.ofMinutes(1), customer.getMaxDurationOfStorage());
    }

    @Test
    public void getName() throws CustomerException {
        Customer customer = new CustomerImpl("Dennis");
        assertEquals("Dennis", customer.getName());
    }

    @Test
    public void toString_name() throws CustomerException {
        Customer customer = new CustomerImpl("Dennis");
        assertEquals("Dennis", customer.toString());
    }

    @Test
    public void setName() throws CustomerException {
        CustomerImpl customer = new CustomerImpl("Dennis");
        customer.setName("Tom");
        assertEquals("Tom", customer.getName());
    }

    @Test
    public void getMaxValue() throws CustomerException {
        CustomerImpl customer = new CustomerImpl("Dennis");
        customer.setMaxValue(BigDecimal.valueOf(20.0));
        assertEquals(BigDecimal.valueOf(20.0), customer.getMaxValue());
    }

    @Test
    public void setMaxValue() throws CustomerException {
        CustomerImpl customer = new CustomerImpl("Dennis");
        customer.setMaxValue(BigDecimal.valueOf(20.0));
        assertEquals(BigDecimal.valueOf(20.0), customer.getMaxValue());
    }

    @Test
    public void getDuration() throws CustomerException {
        CustomerImpl customer = new CustomerImpl("Dennis");
        customer.setMaxDurationOfStorage(Duration.ofMinutes(1));
        assertEquals(Duration.ofMinutes(1), customer.getMaxDurationOfStorage());
    }

}