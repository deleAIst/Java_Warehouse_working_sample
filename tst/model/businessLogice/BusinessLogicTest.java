package model.businessLogice;

import model.storageContract.administration.Customer;
import model.storageContract.administration.CustomerException;
import model.storageContract.administration.CustomerImpl;
import model.storageContract.cargo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.CargoDeletObserver;
import view.Observer;
import view.StorageSpaceObserver;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Dennis Dominik Lehmann
 */
class BusinessLogicTest {

    private BusinessLogic bl;

    @BeforeEach
    public void init() {
        this.bl = new BusinessLogic(6);
    }

    @Test
    public void businessLogicIntalization() {
        BusinessLogic bl = new BusinessLogic(6);
        Cargo[] alleCargo = bl.getAlleCargo();
        assertEquals(6, alleCargo.length);
    }

    @Test
    public void businessLogicIntalizationWitheDefalt() {
        BusinessLogic bl = new BusinessLogic();
        Cargo[] alleCargo = bl.getAlleCargo();
        assertEquals(BusinessLogic.class, bl.getClass());
    }

    //------------------------- Customer ---------------------------------------------------------------

    @Test
    public void getCustomerStorage_good() throws CustomerException {
        this.bl.putCustomer("Dennis");
        Map<String, Customer> customers = this.bl.getCustomerStorage();
        assertEquals(1, customers.size());
    }

    @Test
    public void setCustomerStorage_good() throws CustomerException {
        this.bl.putCustomer("Dennis");
        Map<String, Customer> customersTest = new HashMap<>();
        this.bl.setCustomerStorage(customersTest);
        Map<String, Customer> customers = this.bl.getCustomerStorage();
        assertEquals(0, customers.size());
    }


    @Test
    public void putCustomerGood() throws CustomerException {
        this.bl.putCustomer("Dennis");
        assertEquals("Dennis", this.bl.getCustomer("Dennis").getName());
    }

    @Test
    public void putCustomer_WhiteTheSameNameException() throws CustomerException {
        this.bl.putCustomer("Dennis");
        assertThrows(Exception.class, () -> {
            this.bl.putCustomer("Dennis");
        });
    }

    @Test
    public void getCustomerGood() throws CustomerException {
        this.bl.putCustomer("Dennis");
        assertEquals("Dennis", this.bl.getCustomer("Dennis").getName());
    }

    @Test
    public void getCustomer_WhiteNameNotExist() {
        assertEquals(null, this.bl.getCustomer("Tim"));
    }

    @Test
    public void deletCustermerGood1() throws CustomerException {
        this.bl.putCustomer("Dennis");
        this.bl.deletCustomer("Dennis");
        assertFalse(this.bl.getAlleCustomer().containsKey("Dennis"));
    }

    @Test
    public void deletCustermerGood2() throws CustomerException {
        this.bl.putCustomer("Dennis");
        assertTrue(this.bl.deletCustomer("Dennis"));
    }

    @Test
    public void deletCustomer_WhiteNameNotExist() {
        assertFalse(this.bl.deletCustomer("Dennis"));
    }

    @Test
    public void deletCustomer_deletCagoSameName1() throws CustomerException, CargoException, BusinessLogiceException {
        Customer customer = bl.putCustomer("Dennis");
        Cargo cargo = mock(LiquidBulkCargoImpl.class);
        when(cargo.getOwner()).thenReturn(customer);
        bl.putCargo(cargo);
        bl.putCargo(cargo);
        bl.deletCustomer("Dennis");
        assertNull(bl.getCargo(0));
    }

    @Test
    public void deletCustomer_deletCagoSameName2() throws CustomerException, CargoException, BusinessLogiceException {
        Customer customer = bl.putCustomer("Dennis");
        Cargo cargo = mock(LiquidBulkCargoImpl.class);
        when(cargo.getOwner()).thenReturn(customer);
        bl.putCargo(cargo);
        bl.putCargo(cargo);
        bl.deletCustomer("Dennis");
        assertNull(bl.getCargo(1));
    }

    @Test
    public void getAlleCustomer1() throws CustomerException {
        bl.putCustomer("Dennis");
        assertEquals(1, bl.getAlleCustomer().size());
    }

    @Test
    public void getAlleCustomer2() throws CustomerException {
        bl.putCustomer("Dennis");
        assertTrue(bl.getAlleCustomer().containsKey("Dennis"));
    }

    //---------------------------- Cargo -------------------------------------------------------------------------

    @Test
    public void getCargoStorage_good() {
        Cargo[] cargos = this.bl.getCargoStorage();
        assertEquals(6, cargos.length);
    }

    @Test
    public void setCargoStorage_good() {
        Cargo[] cargosTest = new Cargo[4];
        this.bl.setCargoStorage(cargosTest);
        Cargo[] cargos = this.bl.getCargoStorage();
        assertEquals(4, cargos.length);
    }

    @Test
    public void putCargoGood1() throws CustomerException, BusinessLogiceException, CargoException {
        bl.putCustomer("Dennis");
        LiquidBulkCargo liquidBulkCargo = new LiquidBulkCargoImpl(bl.getCustomer("Dennis"), BigDecimal.valueOf(20.0), Duration.ZERO, new LinkedList<Hazard>(), false);
        int size = bl.putCargo(liquidBulkCargo);
        assertEquals("Dennis", bl.getCargo(size).getOwner().getName());

    }

    @Test
    public void putCargoGood2() throws CustomerException, BusinessLogiceException, CargoException {
        bl.putCustomer("Dennis");
        UnitisedCargo unitisedCargo = new UnitisedCargoImpl(bl.getCustomer("Dennis"), BigDecimal.valueOf(20.0), Duration.ZERO, new LinkedList<Hazard>(), false);
        int size = bl.putCargo(unitisedCargo);
        assertEquals("Dennis", bl.getCargo(size).getOwner().getName());

    }

    @Test
    public void putCargoGood3() throws CustomerException, BusinessLogiceException, CargoException {
        bl.putCustomer("Dennis");
        MixedCargoLiquidBulkAndUnitised mixedCargoLiquidBulkAndUnitised = new MixedCargoLiquidBulkAndUnitisedImpl(bl.getCustomer("Dennis"), BigDecimal.valueOf(20.0), Duration.ZERO, new LinkedList<Hazard>(), false, false);
        int size = bl.putCargo(mixedCargoLiquidBulkAndUnitised);
        assertEquals("Dennis", bl.getCargo(size).getOwner().getName());

    }

    @Test
    public void putCargo_MoreCagos() throws CargoException, BusinessLogiceException, CustomerException {
        Customer customer = bl.putCustomer("Dennis");
        Cargo cargo = mock(LiquidBulkCargoImpl.class);
        when(cargo.getOwner()).thenReturn(customer);
        bl.putCargo(cargo);
        bl.putCargo(cargo);
        assertEquals("Dennis", bl.getCargo(1).getOwner().getName());
    }

    @Test
    public void putCargo_NotOnwer() {
        Cargo cargo = mock(LiquidBulkCargoImpl.class);
        when(cargo.getOwner()).thenReturn(null);
        assertThrows(CargoException.class, () -> {
            this.bl.putCargo(cargo);
        });
    }

    @Test
    public void putCargo_OnwerNotInSystem() {
        Customer customer = mock(CustomerImpl.class);
        when(customer.getName()).thenReturn("Dennis");
        Cargo cargo = mock(LiquidBulkCargoImpl.class);
        when(cargo.getOwner()).thenReturn(customer);
        assertThrows(CargoException.class, () -> {
            this.bl.putCargo(cargo);
        });

    }

    @Test
    public void putCargo_NotCaroTyp() {
        Cargo cargo = mock(Cargo.class);
        assertThrows(CargoException.class, () -> {
            this.bl.putCargo(cargo);
        });
    }

    @Test
    public void  putCargo_HazardNull(){
        Cargo cargo = mock(Cargo.class);
        assertThrows(CargoException.class, () -> {
            this.bl.putCargo(cargo);
        });
    }
    @Test
    public void putCargo_updateInspectionDate() throws CustomerException, CargoException, BusinessLogiceException {
        bl.putCustomer("Dennis");
        LiquidBulkCargo liquidBulkCargo = new LiquidBulkCargoImpl(bl.getCustomer("Dennis"), BigDecimal.valueOf(20.0), Duration.ZERO, new LinkedList<Hazard>(), false);
        int size = bl.putCargo(liquidBulkCargo);
        assertEquals(new java.util.Date(), bl.getCargo(size).getLastInspectionDate());
    }

    @Test
    public void putCargo_StockIsFull() throws BusinessLogiceException, CargoException, CustomerException {
        Cargo cargo = mock(LiquidBulkCargoImpl.class);
        when(cargo.getHazards()).thenReturn(new LinkedList<Hazard>());
        Customer customer = bl.putCustomer("Dennis");
        when(cargo.getOwner()).thenReturn(customer);
        this.bl.putCargo(cargo);
        this.bl.putCargo(cargo);
        this.bl.putCargo(cargo);
        this.bl.putCargo(cargo);
        this.bl.putCargo(cargo);
        this.bl.putCargo(cargo);
        assertThrows(BusinessLogiceException.class, () -> {
            this.bl.putCargo(null);
        });
    }



    @Test
    public void getCargo1() throws CustomerException, CargoException, BusinessLogiceException {
        Cargo cargo = mock(LiquidBulkCargoImpl.class);
        Customer customer = bl.putCustomer("Dennis");
        when(cargo.getOwner()).thenReturn(customer);
        bl.putCargo(cargo);
        assertEquals("Dennis", bl.getCargo(0).getOwner().getName());
    }

    @Test
    public void getCargo2() throws CustomerException, BusinessLogiceException, CargoException {
        Cargo cargo = mock(LiquidBulkCargoImpl.class);
        Customer customer = bl.putCustomer("Dennis");
        when(cargo.getOwner()).thenReturn(customer);
        when(cargo.getValue()).thenReturn(BigDecimal.valueOf(20.0));
        bl.putCargo(cargo);
        assertEquals(BigDecimal.valueOf(20.0), bl.getCargo(0).getValue());
    }

    @Test
    public void getCargo_SizeNotInStorage1() {
        assertThrows(BusinessLogiceException.class, () -> {
            this.bl.getCargo(7);
        });
    }

    @Test
    public void getCargo_SizeNotInStorage2() {
        assertThrows(BusinessLogiceException.class, () -> {
            this.bl.getCargo(-1);
        });
    }

    @Test
    public void getCargo_OnSizeNoCargo() throws BusinessLogiceException {
        assertNull(bl.getCargo(1));
    }

    @Test
    public void deletCargo1() throws Exception {
        Cargo cargo = mock(LiquidBulkCargoImpl.class);
        Customer customer = bl.putCustomer("Dennis");
        when(cargo.getOwner()).thenReturn(customer);
        this.bl.putCargo(cargo);
        assertTrue(this.bl.deletCargo(0));
    }

    @Test
    public void deletCargo2() throws Exception {
        Cargo cargo = mock(LiquidBulkCargoImpl.class);
        Customer customer = bl.putCustomer("Dennis");
        when(cargo.getOwner()).thenReturn(customer);
        this.bl.putCargo(cargo);
        this.bl.deletCargo(0);
        assertEquals(null, bl.getCargo(0));

    }

    @Test
    public void deletCargo_NullOnTheSize() {
        assertFalse(bl.deletCargo(1));
    }

    @Test
    public void deletCargo_SizeOutOfRange() {
        assertFalse(bl.deletCargo(7));
    }

    @Test
    public void deletCargo_SizeNotInStorage1() {
        assertFalse(this.bl.deletCargo(7));
    }

    @Test
    public void deletCargo_SizeNotInStorage2() {
        assertFalse(this.bl.deletCargo(-1));
    }

    @Test
    public void updateInspectionDate1() throws CustomerException, CargoException, BusinessLogiceException {
        bl.putCustomer("Dennis");
        LiquidBulkCargo liquidBulkCargo = new LiquidBulkCargoImpl(bl.getCustomer("Dennis"), BigDecimal.valueOf(20.0), Duration.ZERO, new LinkedList<Hazard>(), false);
        int size = bl.putCargo(liquidBulkCargo);
        bl.updateInspectionDate(size);
        assertNotNull( bl.getCargo(size).getLastInspectionDate());
    }

    @Test
    public void updateInspectionDate2() throws CustomerException, CargoException, BusinessLogiceException {
        bl.putCustomer("Dennis");
        UnitisedCargo unitisedCargo = new UnitisedCargoImpl(bl.getCustomer("Dennis"), BigDecimal.valueOf(20.0), Duration.ZERO, new LinkedList<Hazard>(), false);
        int size = bl.putCargo(unitisedCargo);
        bl.updateInspectionDate(size);
        assertNotNull(bl.getCargo(size).getLastInspectionDate());
    }

    @Test
    public void updateInspectionDate3() throws CustomerException, CargoException, BusinessLogiceException {
        bl.putCustomer("Dennis");
        MixedCargoLiquidBulkAndUnitised mixedCargoLiquidBulkAndUnitised = new MixedCargoLiquidBulkAndUnitisedImpl(bl.getCustomer("Dennis"), BigDecimal.valueOf(20.0), Duration.ZERO, new LinkedList<Hazard>(), false, false);
        int size = bl.putCargo(mixedCargoLiquidBulkAndUnitised);
        bl.updateInspectionDate(size);
        assertEquals(new java.util.Date(), bl.getCargo(size).getLastInspectionDate());
    }

    @Test
    public void updateInspectionDate_SizeOffRange1() {
        assertThrows(BusinessLogiceException.class, () -> {
            this.bl.updateInspectionDate(-1);
        });
    }

    @Test
    public void updateInspectionDate_SizeOffRange2() {
        assertThrows(BusinessLogiceException.class, () -> {
            this.bl.updateInspectionDate(7);
        });
    }

    @Test
    public void updateInspectionDate_noCargoOnthePlace() {
        assertThrows(BusinessLogiceException.class, () -> {
            this.bl.updateInspectionDate(0);
        });
    }

    @Test
    public void getAlleCargo1() {
        assertEquals(6, bl.getAlleCargo().length);
    }

    @Test
    public void getAlleCargo2() throws CustomerException, CargoException, BusinessLogiceException {
        bl.putCustomer("Dennis");
        UnitisedCargo unitisedCargo = new UnitisedCargoImpl(bl.getCustomer("Dennis"), BigDecimal.valueOf(20.0), Duration.ZERO, new LinkedList<Hazard>(), false);
        bl.putCargo(unitisedCargo);
        Cargo[] cargoSt = bl.getAlleCargo();
        assertEquals("Dennis", cargoSt[0].getOwner().getName());
    }


    @Test
    public void filterHazard_GodSize() throws CustomerException, CargoException, BusinessLogiceException {
        List<Hazard> list1 = new LinkedList<>();
        List<Hazard> list2 = new LinkedList<>();
        list1.add(Hazard.explosive);
        list1.add(Hazard.flammable);
        list2.add(Hazard.flammable);
        list2.add(Hazard.radioactive);
        list2.add(Hazard.toxic);
        Customer customer = bl.putCustomer("Dennis");
        Cargo cargo1 = mock(UnitisedCargoImpl.class);
        when(cargo1.getHazards()).thenReturn(list1);
        when(cargo1.getOwner()).thenReturn(customer);

        Cargo cargo2 = mock(MixedCargoLiquidBulkAndUnitisedImpl.class);
        when(cargo2.getHazards()).thenReturn(list1);
        when(cargo2.getOwner()).thenReturn(customer);

        Cargo cargo3 = mock(UnitisedCargoImpl.class);
        when(cargo3.getHazards()).thenReturn(list2);
        when(cargo3.getOwner()).thenReturn(customer);

        bl.putCargo(cargo1);
        bl.putCargo(cargo1);
        bl.putCargo(cargo2);
        bl.putCargo(cargo3);
        List cargofFlterHazard = bl.filterHazard(Hazard.explosive);
        assertEquals(3, cargofFlterHazard.size());
    }

    @Test
    public void filterHazard_God() throws CustomerException, CargoException, BusinessLogiceException {
        List<Hazard> list1 = new LinkedList<>();
        List<Hazard> list2 = new LinkedList<>();
        list1.add(Hazard.explosive);
        list1.add(Hazard.flammable);
        list2.add(Hazard.flammable);
        list2.add(Hazard.radioactive);
        list2.add(Hazard.toxic);
        Customer customer = bl.putCustomer("Dennis");
        Cargo cargo1 = mock(UnitisedCargoImpl.class);
        when(cargo1.getHazards()).thenReturn(list1);
        when(cargo1.getOwner()).thenReturn(customer);

        Cargo cargo2 = mock(MixedCargoLiquidBulkAndUnitisedImpl.class);
        when(cargo2.getHazards()).thenReturn(list1);
        when(cargo2.getOwner()).thenReturn(customer);

        Cargo cargo3 = mock(UnitisedCargoImpl.class);
        when(cargo3.getHazards()).thenReturn(list2);
        when(cargo3.getOwner()).thenReturn(customer);

        bl.putCargo(cargo1);
        bl.putCargo(cargo1);
        bl.putCargo(cargo2);
        bl.putCargo(cargo3);
        List<Cargo> cargofFlterHazard = bl.filterHazard(Hazard.explosive);
        assertTrue(cargofFlterHazard.get(1).getHazards().contains(Hazard.explosive));
    }


    @Test
    public void filterCargoType_godSize() throws CustomerException, CargoException, BusinessLogiceException {
        Customer customer = bl.putCustomer("Dennis");
        Cargo cargo1 = mock(UnitisedCargoImpl.class);

        when(cargo1.getOwner()).thenReturn(customer);

        Cargo cargo2 = mock(MixedCargoLiquidBulkAndUnitisedImpl.class);

        when(cargo2.getOwner()).thenReturn(customer);

        Cargo cargo3 = mock(UnitisedCargoImpl.class);
        when(cargo3.getOwner()).thenReturn(customer);

        bl.putCargo(cargo1);
        bl.putCargo(cargo1);
        bl.putCargo(cargo2);
        bl.putCargo(cargo3);
        bl.putCargo(cargo2);
        bl.putCargo(cargo1);
        List<Cargo> list = bl.filterCargoType("Unitised");
        assertEquals(4, list.size());
    }

    @Test
    public void filterCargoType_godType() throws CustomerException, CargoException, BusinessLogiceException {
        Customer customer = bl.putCustomer("Dennis");
        Cargo cargo1 = mock(UnitisedCargoImpl.class);

        when(cargo1.getOwner()).thenReturn(customer);

        Cargo cargo2 = mock(MixedCargoLiquidBulkAndUnitisedImpl.class);

        when(cargo2.getOwner()).thenReturn(customer);

        Cargo cargo3 = mock(UnitisedCargoImpl.class);
        when(cargo3.getOwner()).thenReturn(customer);

        bl.putCargo(cargo1);
        bl.putCargo(cargo1);
        bl.putCargo(cargo2);
        bl.putCargo(cargo3);
        bl.putCargo(cargo2);
        bl.putCargo(cargo1);
        List<Cargo> list = bl.filterCargoType("Unitised");
        assertEquals(UnitisedCargoImpl.class, list.get(1).getClass());
    }

    @Test
    public void filterCargoType_BusinessLogiceException() throws CustomerException, CargoException, BusinessLogiceException {
        Customer customer = bl.putCustomer("Dennis");
        Cargo cargo1 = mock(UnitisedCargoImpl.class);

        when(cargo1.getOwner()).thenReturn(customer);

        Cargo cargo2 = mock(MixedCargoLiquidBulkAndUnitisedImpl.class);

        when(cargo2.getOwner()).thenReturn(customer);

        Cargo cargo3 = mock(UnitisedCargoImpl.class);
        when(cargo3.getOwner()).thenReturn(customer);

        bl.putCargo(cargo1);
        bl.putCargo(cargo1);
        bl.putCargo(cargo2);
        bl.putCargo(cargo3);
        bl.putCargo(cargo2);
        bl.putCargo(cargo1);
        assertThrows(BusinessLogiceException.class, () -> {
            this.bl.filterCargoType("");
        });
    }

    @Test
    public void login_god() {
        Observer observer = spy(new StorageSpaceObserver(bl));
        bl.login(observer);
        bl.notifySubjects();
        verify(observer, times(1)).update();
    }

    @Test
    public void logOff_god() {
        Observer observer = spy(new StorageSpaceObserver(bl));
        bl.login(observer);
        bl.logOff(observer);
        bl.notifySubjects();
        verify(observer, times(0)).update();
    }

    @Test
    public void isFull_trueForFullStoreg() throws CustomerException, CargoException, BusinessLogiceException {
        BusinessLogic blT = new BusinessLogic(1);
        blT.putCustomer("Dennis");
        UnitisedCargo unitisedCargo = new UnitisedCargoImpl(blT.getCustomer("Dennis"), BigDecimal.valueOf(20.0), Duration.ZERO, new LinkedList<Hazard>(), false);
        blT.putCargo(unitisedCargo);
        assertTrue(blT.isFull());
    }

    @Test
    public void isFull_FalseIsNotFullStoreg() throws CustomerException, CargoException, BusinessLogiceException {
        BusinessLogic blT = new BusinessLogic(2);
        blT.putCustomer("Dennis");
        UnitisedCargo unitisedCargo = new UnitisedCargoImpl(blT.getCustomer("Dennis"), BigDecimal.valueOf(20.0), Duration.ZERO, new LinkedList<Hazard>(), false);
        blT.putCargo(unitisedCargo);
        assertFalse(blT.isFull());
    }

    @Test
    public void  getObserverList_good(){
        Observer observer = new CargoDeletObserver(this.bl);
        bl.login(observer);
        assertEquals(1, bl.getObserverList().size());
    }

}