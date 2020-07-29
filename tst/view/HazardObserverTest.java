package view;

import model.businessLogice.BusinessLogic;
import model.businessLogice.BusinessLogiceException;
import model.storageContract.administration.Customer;
import model.storageContract.administration.CustomerException;
import model.storageContract.cargo.Cargo;
import model.storageContract.cargo.CargoException;
import model.storageContract.cargo.Hazard;
import model.storageContract.cargo.UnitisedCargoImpl;
import org.junit.jupiter.api.Test;

import java.io.PrintStream;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Dennis Dominik Lehmann
 */
class HazardObserverTest {

    @Test
    public void hazardObserverIntalization() {
        BusinessLogic bl = new BusinessLogic(6);
        HazardObserver observer = new HazardObserver(bl);
        assertEquals(HazardObserver.class, observer.getClass());
    }

    @Test
    public void update_PrintHazard() throws CustomerException, CargoException, BusinessLogiceException {
        PrintStream out = mock(PrintStream.class);
        System.setOut(out);
        HashSet<Hazard> list = new HashSet<>();
        list.add(Hazard.flammable);
        list.add(Hazard.explosive);
        BusinessLogic businessLogic = new BusinessLogic(10);
        Customer customer = businessLogic.putCustomer("Dennis");
        Cargo cargo1 = mock(UnitisedCargoImpl.class);
        when(cargo1.getOwner()).thenReturn(customer);
        when(cargo1.getHazards()).thenReturn(list);
        businessLogic.putCargo(cargo1);
        Observer observer = new HazardObserver(businessLogic);
        observer.update();
        verify(out).println(list);
    }

    @Test
    public void update_PrintHazardWithAVariation() throws CustomerException, CargoException, BusinessLogiceException {
        PrintStream out = mock(PrintStream.class);
        System.setOut(out);
        HashSet<Hazard> finel = new HashSet<>();
        finel.add(Hazard.flammable);
        finel.add(Hazard.explosive);
        finel.add(Hazard.radioactive);
        HashSet<Hazard> list1 = new HashSet<>();
        list1.add(Hazard.flammable);
        list1.add(Hazard.explosive);
        HashSet<Hazard> list2 = new HashSet<>();
        list2.add(Hazard.radioactive);
        BusinessLogic businessLogic = new BusinessLogic(10);
        Observer observer = new HazardObserver(businessLogic);
        businessLogic.login(observer);
        Customer customer = businessLogic.putCustomer("Dennis");
        Cargo cargo1 = mock(UnitisedCargoImpl.class);
        when(cargo1.getOwner()).thenReturn(customer);
        when(cargo1.getHazards()).thenReturn(list1);

        Cargo cargo2 = mock(UnitisedCargoImpl.class);
        when(cargo2.getOwner()).thenReturn(customer);
        when(cargo2.getHazards()).thenReturn(list2);

        businessLogic.putCargo(cargo2);
        businessLogic.putCargo(cargo1);

        verify(out, times(2)).println(finel);
    }

    @Test
    public void update_PrintNoHazard() throws CustomerException, CargoException, BusinessLogiceException {
        HashSet<Hazard> finel =new HashSet<>();
        finel.clear();
        PrintStream out = mock(PrintStream.class);
        System.setOut(out);
        HashSet<Hazard> list1 = new HashSet<>();
        list1.add(Hazard.flammable);
        list1.add(Hazard.explosive);
        BusinessLogic businessLogic = new BusinessLogic(10);
        Customer customer = businessLogic.putCustomer("Dennis");
        Cargo cargo1 = mock(UnitisedCargoImpl.class);
        when(cargo1.getOwner()).thenReturn(customer);
        when(cargo1.getHazards()).thenReturn(list1);
        Observer observer = new HazardObserver(businessLogic);
        businessLogic.login(observer);
        businessLogic.putCargo(cargo1);
        businessLogic.deletCargo(0);
        verify(out,times(2)).println(finel);
    }


}