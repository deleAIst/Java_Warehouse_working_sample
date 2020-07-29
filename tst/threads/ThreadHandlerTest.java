package threads;

import model.businessLogice.BusinessLogic;
import model.businessLogice.BusinessLogiceException;
import model.storageContract.administration.Customer;
import model.storageContract.cargo.Cargo;
import model.storageContract.cargo.CargoException;
import model.storageContract.cargo.LiquidBulkCargoImpl;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;

import static org.mockito.Mockito.*;

/**
 * @author Dennis Dominik Lehmann
 */
class ThreadHandlerTest {

    @Test
    public void good_put() throws InterruptedException, CargoException, BusinessLogiceException {
        BusinessLogic businessLogic = spy(new BusinessLogic(10));
        Customer customer = mock(Customer.class);
        when(businessLogic.getCustomer("Dennis")).thenReturn(customer);
        when(customer.getName()).thenReturn("Dennis");
        ThreadHandler threadHandler = new ThreadHandler();
        Cargo cargo = new LiquidBulkCargoImpl(customer, BigDecimal.valueOf(0.0), Duration.ZERO, null, false);
        threadHandler.put(businessLogic, cargo);
        verify(businessLogic, times(1)).putCargo(cargo);
    }
}