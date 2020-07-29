package model.storageContract.cargo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.storageContract.administration.Customer;
import model.storageContract.administration.CustomerException;
import model.storageContract.administration.CustomerImpl;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Dennis Dominik Lehmann
 */
class MixedCargoLiquidBulkAndUnitisedImplTest {

    private MixedCargoLiquidBulkAndUnitisedImpl mixedCargoLiquidBulkAndUnitised;

    @BeforeEach
    public void testLiquidBulkCargoObject() throws CustomerException {
        Customer customer = new CustomerImpl("Dennis");
        Collection<Hazard> collection = new ArrayList<Hazard>();
        collection.add(Hazard.explosive);
        this.mixedCargoLiquidBulkAndUnitised = new MixedCargoLiquidBulkAndUnitisedImpl(customer, BigDecimal.valueOf(20.0), Duration.ofMinutes(1), collection, true, true);
    }

    @Test
    public void goodIntalization() throws Exception {
        Customer customer1 = new CustomerImpl("Dennis");
        Collection<Hazard> collection1 = new ArrayList<Hazard>();
        collection1.add(Hazard.explosive);
        MixedCargoLiquidBulkAndUnitisedImpl mixedCargoLiquidBulkAndUnitised1 = new MixedCargoLiquidBulkAndUnitisedImpl(customer1, BigDecimal.valueOf(20.0), Duration.ofMinutes(1), collection1, true, true );
        assertEquals(MixedCargoLiquidBulkAndUnitisedImpl.class, mixedCargoLiquidBulkAndUnitised1.getClass());
    }

    @Test
    public void goodIntalization_defultCunstrukter(){
        MixedCargoLiquidBulkAndUnitisedImpl mixedCargoLiquidBulkAndUnitised1 = new MixedCargoLiquidBulkAndUnitisedImpl();
        assertEquals(MixedCargoLiquidBulkAndUnitisedImpl.class, mixedCargoLiquidBulkAndUnitised1.getClass());
    }

    @Test
    public void getOwner(){
        assertEquals("Dennis", this.mixedCargoLiquidBulkAndUnitised.getOwner().getName());
    }

    @Test
    public void setOwner() throws CustomerException {
        Customer customerTest= new CustomerImpl("Tom");
        this.mixedCargoLiquidBulkAndUnitised.setOwner(customerTest);
        assertEquals("Tom", this.mixedCargoLiquidBulkAndUnitised.getOwner().getName());
    }

    @Test
    public void getValue(){
        assertEquals(BigDecimal.valueOf(20.0), this.mixedCargoLiquidBulkAndUnitised.getValue());
    }

    @Test
    public void setValue(){
        this.mixedCargoLiquidBulkAndUnitised.setValue(BigDecimal.valueOf(30.0));
        assertEquals(BigDecimal.valueOf(30.0), this.mixedCargoLiquidBulkAndUnitised.getValue());
    }

    @Test
    public void getDurationOfStorage(){
        assertEquals(Duration.ofMinutes(1), this.mixedCargoLiquidBulkAndUnitised.getDurationOfStorage());
    }

    @Test
    public void setDurationOfStorage(){
        this.mixedCargoLiquidBulkAndUnitised.setDurationOfStorage(Duration.ofMinutes(333));
        assertEquals(Duration.ofMinutes(333), this.mixedCargoLiquidBulkAndUnitised.getDurationOfStorage());
    }

    @Test
    public void getHazards(){
        Collection<Hazard> co = this.mixedCargoLiquidBulkAndUnitised.getHazards();
        assertEquals(1, co.size());
    }

    @Test
    public void setHazards(){
        List<Hazard> list= new LinkedList<>();
        list.add(Hazard.explosive);
        list.add(Hazard.toxic);
        this.mixedCargoLiquidBulkAndUnitised.setHazards(list);
        Collection<Hazard> co = this.mixedCargoLiquidBulkAndUnitised.getHazards();
        assertEquals(2, co.size());
    }

    @Test
    public void getLastInspectionDate(){
        this.mixedCargoLiquidBulkAndUnitised.setLastInspectionDate(Date.valueOf(LocalDate.now()));
        assertEquals(Date.valueOf(LocalDate.now()), this.mixedCargoLiquidBulkAndUnitised.getLastInspectionDate());
    }

    @Test
    public void  getStorageSize(){
        this.mixedCargoLiquidBulkAndUnitised.setStorageSize(5);
        assertEquals(5, this.mixedCargoLiquidBulkAndUnitised.getStorageSize());
    }

    @Test
    public void getStorageDate(){
        this.mixedCargoLiquidBulkAndUnitised.setStorageDate(Date.valueOf(LocalDate.now()));
        assertEquals(Date.valueOf(LocalDate.now()), this.mixedCargoLiquidBulkAndUnitised.getStorageDate());
    }

    @Test
    public void setLastInspectionDate(){
        this.mixedCargoLiquidBulkAndUnitised.setLastInspectionDate(Date.valueOf(LocalDate.now()));
        assertEquals(Date.valueOf(LocalDate.now()), this.mixedCargoLiquidBulkAndUnitised.getLastInspectionDate());
    }

    @Test
    public void  setStoregSize(){
        this.mixedCargoLiquidBulkAndUnitised.setStorageSize(5);
        assertEquals(5, this.mixedCargoLiquidBulkAndUnitised.getStorageSize());
    }

    @Test
    public void setStorageDate(){
        this.mixedCargoLiquidBulkAndUnitised.setStorageDate(Date.valueOf(LocalDate.now()));
        assertEquals(Date.valueOf(LocalDate.now()), this.mixedCargoLiquidBulkAndUnitised.getStorageDate());
    }

    @Test
    public void isPressurized(){
        assertTrue(this.mixedCargoLiquidBulkAndUnitised.isPressurized());
    }

    @Test
    public void setPressurized(){
        this.mixedCargoLiquidBulkAndUnitised.setPressurized(false);
        assertFalse(this.mixedCargoLiquidBulkAndUnitised.isPressurized());
    }

    @Test
    public void isFragile(){
        assertTrue(this.mixedCargoLiquidBulkAndUnitised.isFragile());
    }

    @Test
    public void setFragile(){
        this.mixedCargoLiquidBulkAndUnitised.setFragile(false);
        assertFalse(this.mixedCargoLiquidBulkAndUnitised.isFragile());
    }
}