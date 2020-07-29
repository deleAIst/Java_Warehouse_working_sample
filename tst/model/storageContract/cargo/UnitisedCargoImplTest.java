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
class UnitisedCargoImplTest {

    private UnitisedCargoImpl unitisedCargo;

    @BeforeEach
    public void testLiquidBulkCargoObject() throws CustomerException {
        Customer customer = new CustomerImpl("Dennis");
        Collection<Hazard> collection = new ArrayList<Hazard>();
        collection.add(Hazard.explosive);
        this.unitisedCargo = new UnitisedCargoImpl(customer, BigDecimal.valueOf(20.0), Duration.ofMinutes(1), collection, true);
    }

    @Test
    public void goodIntalization() throws Exception {
        Customer customer1 = new CustomerImpl("Dennis");
        Collection<Hazard> collection1 = new ArrayList<Hazard>();
        collection1.add(Hazard.explosive);
        UnitisedCargoImpl unitisedCargo1 = new UnitisedCargoImpl(customer1, BigDecimal.valueOf(20.0), Duration.ofMinutes(1), collection1, true);
        assertEquals(UnitisedCargoImpl.class, unitisedCargo1.getClass());
    }

    @Test
    public void goodIntalization_defultCunstrukter(){
        UnitisedCargoImpl unitisedCargo1 = new UnitisedCargoImpl();
        assertEquals(UnitisedCargoImpl.class, unitisedCargo1.getClass());
    }

    @Test
    public void getOwner(){
        assertEquals("Dennis", this.unitisedCargo.getOwner().getName());
    }

    @Test
    public void setOwner() throws CustomerException {
        Customer customerTest= new CustomerImpl("Tom");
        this.unitisedCargo.setOwner(customerTest);
        assertEquals("Tom", this.unitisedCargo.getOwner().getName());
    }

    @Test
    public void getValue(){
        assertEquals(BigDecimal.valueOf(20.0), this.unitisedCargo.getValue());
    }

    @Test
    public void setValue(){
        this.unitisedCargo.setValue(BigDecimal.valueOf(30.0));
        assertEquals(BigDecimal.valueOf(30.0), this.unitisedCargo.getValue());
    }

    @Test
    public void getDurationOfStorage(){
        assertEquals(Duration.ofMinutes(1), this.unitisedCargo.getDurationOfStorage());
    }

    @Test
    public void setDurationOfStorage(){
        this.unitisedCargo.setDurationOfStorage(Duration.ofMinutes(333));
        assertEquals(Duration.ofMinutes(333), this.unitisedCargo.getDurationOfStorage());
    }

    @Test
    public void getHazards(){
        Collection<Hazard> co = this.unitisedCargo.getHazards();
        assertEquals(1, co.size());
    }

    @Test
    public void setHazards(){
        List<Hazard> list= new LinkedList<>();
        list.add(Hazard.explosive);
        list.add(Hazard.toxic);
        this.unitisedCargo.setHazards(list);
        Collection<Hazard> co = this.unitisedCargo.getHazards();
        assertEquals(2, co.size());
    }

    @Test
    public void getLastInspectionDate(){
        this.unitisedCargo.setLastInspectionDate(Date.valueOf(LocalDate.now()));
        assertEquals(Date.valueOf(LocalDate.now()), this.unitisedCargo.getLastInspectionDate());
    }

    @Test
    public void  getStorageSize(){
        this.unitisedCargo.setStorageSize(5);
        assertEquals(5, this.unitisedCargo.getStorageSize());
    }

    @Test
    public void getStorageDate(){
        this.unitisedCargo.setStorageDate(Date.valueOf(LocalDate.now()));
        assertEquals(Date.valueOf(LocalDate.now()), this.unitisedCargo.getStorageDate());
    }

    @Test
    public void setLastInspectionDate(){
        this.unitisedCargo.setLastInspectionDate(Date.valueOf(LocalDate.now()));
        assertEquals(Date.valueOf(LocalDate.now()), this.unitisedCargo.getLastInspectionDate());
    }

    @Test
    public void  setStoregSize(){
        this.unitisedCargo.setStorageSize(5);
        assertEquals(5, this.unitisedCargo.getStorageSize());
    }

    @Test
    public void setStorageDate(){
        this.unitisedCargo.setStorageDate(Date.valueOf(LocalDate.now()));
        assertEquals(Date.valueOf(LocalDate.now()), this.unitisedCargo.getStorageDate());
    }

    @Test
    public void isFragile(){
        assertTrue(this.unitisedCargo.isFragile());
    }

    @Test
    public void setFragile(){
        this.unitisedCargo.setFragile(false);
        assertFalse(this.unitisedCargo.isFragile());
    }

    }