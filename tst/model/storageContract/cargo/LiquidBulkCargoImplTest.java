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
class LiquidBulkCargoImplTest {
    private LiquidBulkCargoImpl liquidBulkCargo;

    @BeforeEach
    public void testLiquidBulkCargoObject() throws CustomerException {
        Customer customer = new CustomerImpl("Dennis");
        Collection<Hazard> collection = new ArrayList<Hazard>();
        collection.add(Hazard.explosive);
        this.liquidBulkCargo = new LiquidBulkCargoImpl(customer, BigDecimal.valueOf(20.0), Duration.ofMinutes(1), collection, true );
    }

    @Test
    public void goodIntalization() throws Exception {
        Customer customer1 = new CustomerImpl("Dennis");
        Collection<Hazard> collection1 = new ArrayList<Hazard>();
        collection1.add(Hazard.explosive);
        LiquidBulkCargoImpl liquidBulkCargo1 = new LiquidBulkCargoImpl(customer1, BigDecimal.valueOf(20.0), Duration.ofMinutes(1), collection1, true );
        assertEquals(LiquidBulkCargoImpl.class, liquidBulkCargo1.getClass());

    }

    @Test
    public void goodIntalization_defultCunstrukter(){
        LiquidBulkCargoImpl liquidBulkCargo1 = new LiquidBulkCargoImpl();
        assertEquals(LiquidBulkCargoImpl.class, liquidBulkCargo1.getClass());
    }

    @Test
    public void getOwner(){
        assertEquals("Dennis", this.liquidBulkCargo.getOwner().getName());
    }

    @Test
    public void setOwner() throws CustomerException {
        Customer customerTest= new CustomerImpl("Tom");
        this.liquidBulkCargo.setOwner(customerTest);
        assertEquals("Tom", this.liquidBulkCargo.getOwner().getName());
    }

    @Test
    public void getValue(){
        assertEquals(BigDecimal.valueOf(20.0), this.liquidBulkCargo.getValue());
    }

    @Test
    public void setValue(){
        this.liquidBulkCargo.setValue(BigDecimal.valueOf(30.0));
        assertEquals(BigDecimal.valueOf(30.0), this.liquidBulkCargo.getValue());
    }

    @Test
    public void getDurationOfStorage(){
        assertEquals(Duration.ofMinutes(1), this.liquidBulkCargo.getDurationOfStorage());
    }

    @Test
    public void setDurationOfStorage(){
        this.liquidBulkCargo.setDurationOfStorage(Duration.ofMinutes(333));
        assertEquals(Duration.ofMinutes(333), this.liquidBulkCargo.getDurationOfStorage());
    }

    @Test
    public void getHazards(){
        Collection<Hazard> co = this.liquidBulkCargo.getHazards();
        assertEquals(1, co.size());
    }

    @Test
    public void setHazards(){
        List<Hazard> list= new LinkedList<>();
        list.add(Hazard.explosive);
        list.add(Hazard.toxic);
        this.liquidBulkCargo.setHazards(list);
        Collection<Hazard> co = this.liquidBulkCargo.getHazards();
        assertEquals(2, co.size());
    }

    @Test
    public void getLastInspectionDate(){
        this.liquidBulkCargo.setLastInspectionDate(Date.valueOf(LocalDate.now()));
        assertEquals(Date.valueOf(LocalDate.now()), this.liquidBulkCargo.getLastInspectionDate());
    }

    @Test
    public void  getStoregSize(){
        this.liquidBulkCargo.setStorageSize(5);
        assertEquals(5, this.liquidBulkCargo.getStorageSize());
    }

    @Test
    public void getStorageDate(){
        this.liquidBulkCargo.setStorageDate(Date.valueOf(LocalDate.now()));
        assertEquals(Date.valueOf(LocalDate.now()), this.liquidBulkCargo.getStorageDate());
    }

    @Test
    public void setLastInspectionDate(){
        this.liquidBulkCargo.setLastInspectionDate(Date.valueOf(LocalDate.now()));
        assertEquals(Date.valueOf(LocalDate.now()), this.liquidBulkCargo.getLastInspectionDate());
    }

    @Test
    public void  setStoregSize(){
        this.liquidBulkCargo.setStorageSize(5);
        assertEquals(5, this.liquidBulkCargo.getStorageSize());
    }

    @Test
    public void setStorageDate(){
        this.liquidBulkCargo.setStorageDate(Date.valueOf(LocalDate.now()));
        assertEquals(Date.valueOf(LocalDate.now()), this.liquidBulkCargo.getStorageDate());
    }

    @Test
    public void isPressurized(){
        assertTrue(this.liquidBulkCargo.isPressurized());
    }

    @Test
    public void setPressurized(){
        this.liquidBulkCargo.setPressurized(false);
        assertFalse(this.liquidBulkCargo.isPressurized());
    }
}