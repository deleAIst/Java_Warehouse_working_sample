package serialize;

import model.businessLogice.BusinessLogic;
import model.businessLogice.BusinessLogiceException;
import model.storageContract.administration.Customer;
import model.storageContract.administration.CustomerException;
import model.storageContract.cargo.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class JOSandJBPTest {
    private BusinessLogic model;
    private Customer customer;
    private Cargo cargo1;
    private Cargo cargo2;
    private Cargo cargo3;

    @BeforeEach
    public void setup() throws CustomerException, CargoException, BusinessLogiceException {
        this.model = new BusinessLogic(5);
        this.customer = model.putCustomer("Dennis");
        HashSet<Hazard> hazards = new HashSet<>();
        hazards.add(Hazard.explosive);
        this.cargo1 = new LiquidBulkCargoImpl(customer, BigDecimal.ZERO, Duration.ZERO, hazards, true);
        this.cargo2 = new UnitisedCargoImpl(customer, BigDecimal.ZERO, Duration.ZERO, hazards, true);
        this.cargo3 = new MixedCargoLiquidBulkAndUnitisedImpl(customer, BigDecimal.ZERO, Duration.ZERO, hazards, true, false);
        model.putCargo(cargo1);
        model.putCargo(cargo2);
        model.putCargo(cargo3);
    }

    @AfterEach
    public void deleteFiles() throws IOException {
        Files.deleteIfExists(Paths.get("JunitTestDeserializeJBP.xml"));
        Files.deleteIfExists(Paths.get("JunitTestDeserializeJOS.txt"));
        Files.deleteIfExists(Paths.get("JunitTestJBP.xml"));
        Files.deleteIfExists(Paths.get("JunitTestJOS.txt"));
        Files.deleteIfExists(Paths.get("CargoSave.txt"));
    }

    //------------------------------------  JOS ---------------------------------------------------------------//

    @Test
    public void serializeJOS_good(){
        JOSandJBP.serializeJOS("JunitTestJOS", this.model);
        assertTrue(new File("JunitTestJOS.txt").exists());
    }

    @Test
    public void serializeJOS_noFilename(){
        assertThrows(IllegalArgumentException.class, () -> {
            JOSandJBP.serializeJOS(null, this.model);
        });
    }

    @Test
    public void serializeJOS_noModel(){
        assertThrows(IllegalArgumentException.class, () -> {
            JOSandJBP.serializeJOS("test", null);
        });
    }

   @Test
   public void deserializeJOS_good(){
       JOSandJBP.serializeJOS("JunitTestDeserializeJOS", this.model);
       BusinessLogic modelTest = JOSandJBP.deserializeJOS("JunitTestDeserializeJOS");
       assertEquals(BusinessLogic.class, modelTest.getClass() );
   }

   @Test
    public void deserializeJOS_capsityOfCargoStorag(){
       JOSandJBP.serializeJOS("JunitTestDeserializeJOS", this.model);
       BusinessLogic modelTest = JOSandJBP.deserializeJOS("JunitTestDeserializeJOS");
       assertEquals(this.model.getAlleCargo().length, modelTest.getAlleCargo().length);
   }

    @Test
    public void deserializeJOS_capsityOfCustomerStorag(){
        JOSandJBP.serializeJOS("JunitTestDeserializeJOS", this.model);
        BusinessLogic modelTest = JOSandJBP.deserializeJOS("JunitTestDeserializeJOS");
        assertEquals(this.model.getAlleCustomer().size(), modelTest.getAlleCustomer().size());
    }

    @Test
    public void deserializeJOS_SamePsioneOfCargos0(){
        JOSandJBP.serializeJOS("JunitTestDeserializeJOS", this.model);
        BusinessLogic modelTest = JOSandJBP.deserializeJOS("JunitTestDeserializeJOS");
        Cargo[] cargos = modelTest.getAlleCargo();
        Cargo cargoTest= cargos[0];
        assertEquals(cargo1.getClass(), cargoTest.getClass());
    }

    @Test
    public void deserializeJOS_SamePsioneOfCargos1(){
        JOSandJBP.serializeJOS("JunitTestDeserializeJOS", this.model);
        BusinessLogic modelTest = JOSandJBP.deserializeJOS("JunitTestDeserializeJOS");
        Cargo[] cargos = modelTest.getAlleCargo();
        Cargo cargoTest= cargos[1];
        assertEquals(cargo2.getClass(), cargoTest.getClass());
    }

    @Test
    public void deserializeJOS_SamePsioneOfCargos2(){
        JOSandJBP.serializeJOS("JunitTestDeserializeJOS", this.model);
        BusinessLogic modelTest = JOSandJBP.deserializeJOS("JunitTestDeserializeJOS");
        Cargo[] cargos = modelTest.getAlleCargo();
        Cargo cargoTest= cargos[2];
        assertEquals(cargo3.getClass(), cargoTest.getClass());
    }

    @Test
    public void deserializeJOS_customer(){
        JOSandJBP.serializeJOS("JunitTestDeserializeJOS", this.model);
        BusinessLogic modelTest = JOSandJBP.deserializeJOS("JunitTestDeserializeJOS");
        assertEquals(this.customer.getName(), modelTest.getCustomer("Dennis").getName() );
    }

    @Test
    public void deserializeJOS_LiquidBulkCargoImplCustomer(){
        JOSandJBP.serializeJOS("JunitTestDeserializeJOS", this.model);
        BusinessLogic modelTest = JOSandJBP.deserializeJOS("JunitTestDeserializeJOS");
        Cargo[] cargos = modelTest.getAlleCargo();
        Cargo cargoTest= cargos[0];
        assertEquals(cargo1.getOwner().getName(), cargoTest.getOwner().getName());
    }

    @Test
    public void deserializeJOS_LiquidBulkCargoImplValue(){
        JOSandJBP.serializeJOS("JunitTestDeserializeJOS", this.model);
        BusinessLogic modelTest = JOSandJBP.deserializeJOS("JunitTestDeserializeJOS");
        Cargo[] cargos = modelTest.getAlleCargo();
        Cargo cargoTest= cargos[0];
        assertEquals(cargo1.getOwner().getMaxValue(), cargoTest.getOwner().getMaxValue());
    }

    @Test
    public void deserializeJOS_LiquidBulkCargoImplDuration(){
        JOSandJBP.serializeJOS("JunitTestDeserializeJOS", this.model);
        BusinessLogic modelTest = JOSandJBP.deserializeJOS("JunitTestDeserializeJOS");
        Cargo[] cargos = modelTest.getAlleCargo();
        Cargo cargoTest= cargos[0];
        assertEquals(cargo1.getOwner().getMaxDurationOfStorage(), cargoTest.getOwner().getMaxDurationOfStorage());
    }

    @Test
    public void deserializeJOS_LiquidBulkCargoImplLatInspectionDate(){
        JOSandJBP.serializeJOS("JunitTestDeserializeJOS", this.model);
        BusinessLogic modelTest = JOSandJBP.deserializeJOS("JunitTestDeserializeJOS");
        Cargo[] cargos = modelTest.getAlleCargo();
        Cargo cargoTest= cargos[0];
        assertEquals(cargo1.getLastInspectionDate(), cargoTest.getLastInspectionDate());
    }

    @Test
    public void deserializeJOS__LiquidBulkCargoImplHazard(){
        JOSandJBP.serializeJOS("JunitTestDeserializeJOS", this.model);
        BusinessLogic modelTest = JOSandJBP.deserializeJOS("JunitTestDeserializeJOS");
        Cargo[] cargos = modelTest.getAlleCargo();
        Cargo cargoTest= cargos[0];
        assertEquals(cargo1.getHazards(), cargoTest.getHazards());
    }

    @Test
    public void deserializeJOS_UnitisedCargoImplCustomer(){
        JOSandJBP.serializeJOS("JunitTestDeserializeJOS", this.model);
        BusinessLogic modelTest = JOSandJBP.deserializeJOS("JunitTestDeserializeJOS");
        Cargo[] cargos = modelTest.getAlleCargo();
        Cargo cargoTest= cargos[1];
        assertEquals(cargo2.getOwner().getName(), cargoTest.getOwner().getName());
    }

    @Test
    public void deserializeJOS_UnitisedCargoImpllValue(){
        JOSandJBP.serializeJOS("JunitTestDeserializeJOS", this.model);
        BusinessLogic modelTest = JOSandJBP.deserializeJOS("JunitTestDeserializeJOS");
        Cargo[] cargos = modelTest.getAlleCargo();
        Cargo cargoTest= cargos[1];
        assertEquals(cargo2.getOwner().getMaxValue(), cargoTest.getOwner().getMaxValue());
    }

    @Test
    public void deserializeJOS_UnitisedCargoImplDuration(){
        JOSandJBP.serializeJOS("JunitTestDeserializeJOS", this.model);
        BusinessLogic modelTest = JOSandJBP.deserializeJOS("JunitTestDeserializeJOS");
        Cargo[] cargos = modelTest.getAlleCargo();
        Cargo cargoTest= cargos[1];
        assertEquals(cargo2.getOwner().getMaxDurationOfStorage(), cargoTest.getOwner().getMaxDurationOfStorage());
    }

    @Test
    public void deserializeJOS_UnitisedCargoImplLastInspectionDate(){
        JOSandJBP.serializeJOS("JunitTestDeserializeJOS", this.model);
        BusinessLogic modelTest = JOSandJBP.deserializeJOS("JunitTestDeserializeJOS");
        Cargo[] cargos = modelTest.getAlleCargo();
        Cargo cargoTest= cargos[1];
        assertEquals(cargo2.getLastInspectionDate(), cargoTest.getLastInspectionDate());
    }

    @Test
    public void deserializeJOS_UnitisedCargoImplHazard(){
        JOSandJBP.serializeJOS("JunitTestDeserializeJOS", this.model);
        BusinessLogic modelTest = JOSandJBP.deserializeJOS("JunitTestDeserializeJOS");
        Cargo[] cargos = modelTest.getAlleCargo();
        Cargo cargoTest= cargos[1];
        assertEquals(cargo2.getHazards(), cargoTest.getHazards());
    }

    @Test
    public void deserializeJOS_MixedCargoLiquidBulkAndUnitisedImplCustomer(){
        JOSandJBP.serializeJOS("JunitTestDeserializeJOS", this.model);
        BusinessLogic modelTest = JOSandJBP.deserializeJOS("JunitTestDeserializeJOS");
        Cargo[] cargos = modelTest.getAlleCargo();
        Cargo cargoTest= cargos[2];
        assertEquals(cargo3.getOwner().getName(), cargoTest.getOwner().getName());
    }

    @Test
    public void deserializeJOS_MixedCargoLiquidBulkAndUnitisedImplValue(){
        JOSandJBP.serializeJOS("JunitTestDeserializeJOS", this.model);
        BusinessLogic modelTest = JOSandJBP.deserializeJOS("JunitTestDeserializeJOS");
        Cargo[] cargos = modelTest.getAlleCargo();
        Cargo cargoTest= cargos[2];
        assertEquals(cargo3.getOwner().getMaxValue(), cargoTest.getOwner().getMaxValue());
    }

    @Test
    public void deserializeJOS_MixedCargoLiquidBulkAndUnitisedImplDuration(){
        JOSandJBP.serializeJOS("JunitTestDeserializeJOS", this.model);
        BusinessLogic modelTest = JOSandJBP.deserializeJOS("JunitTestDeserializeJOS");
        Cargo[] cargos = modelTest.getAlleCargo();
        Cargo cargoTest= cargos[2];
        assertEquals(cargo3.getOwner().getMaxDurationOfStorage(), cargoTest.getOwner().getMaxDurationOfStorage());
    }

    @Test
    public void deserializeJOS_MixedCargoLiquidBulkAndUnitisedImplLastInspectionDate(){
        JOSandJBP.serializeJOS("JunitTestDeserializeJOS", this.model);
        BusinessLogic modelTest = JOSandJBP.deserializeJOS("JunitTestDeserializeJOS");
        Cargo[] cargos = modelTest.getAlleCargo();
        Cargo cargoTest= cargos[2];
        assertEquals(cargo3.getLastInspectionDate(), cargoTest.getLastInspectionDate());
    }

    @Test
    public void deserializeJOS_MixedCargoLiquidBulkAndUnitisedImplHazard(){
        JOSandJBP.serializeJOS("JunitTestDeserializeJOS", this.model);
        BusinessLogic modelTest = JOSandJBP.deserializeJOS("JunitTestDeserializeJOS");
        Cargo[] cargos = modelTest.getAlleCargo();
        Cargo cargoTest= cargos[2];
        assertEquals(cargo3.getHazards(), cargoTest.getHazards());
    }

    @Test
    public void deserializeJOS_notFile(){
        JOSandJBP.serializeJOS("JunitTestDeserializeJOS", this.model);
        assertThrows(IllegalArgumentException.class, () -> {
            JOSandJBP.deserializeJOS(null);
        });
    }

    @Test
    public void deserializeJOS_falseFilename(){
        JOSandJBP.serializeJOS("JunitTestDeserializeJOS", this.model);
        assertThrows(IllegalArgumentException.class, () -> {
            JOSandJBP.deserializeJOS("JunitT");
        });
    }

    //--------------------------------------- JPB -----------------------------------------------------------------//

    @Test
    public void serializeJBP_good(){
        JOSandJBP.serializeJBP("JunitTestJBP", this.model);
        File file = new File("JunitTestJBP.xml");
        assertTrue(file.exists());
    }

    @Test
    public void serializeJBP_noFilename(){
        assertThrows(IllegalArgumentException.class, () -> {
            JOSandJBP.serializeJBP(null, this.model);
        });
    }

    @Test
    public void serializeJBP_noModel(){
        assertThrows(IllegalArgumentException.class, () -> {
            JOSandJBP.serializeJBP("test", null);
        });
    }

    @Test
    public void deserializeJBP_good(){
        JOSandJBP.serializeJBP("JunitTestDeserializeJBP", this.model);
        BusinessLogic modelTest = JOSandJBP.deserializeJBP("JunitTestDeserializeJBP");
        assertEquals(BusinessLogic.class, modelTest.getClass() );
    }

    @Test
    public void deserializeJBP_capsityOfCargoStorag(){
        JOSandJBP.serializeJBP("JunitTestDeserializeJBP", this.model);
        BusinessLogic modelTest = JOSandJBP.deserializeJBP("JunitTestDeserializeJBP");
        assertEquals(this.model.getAlleCargo().length, modelTest.getAlleCargo().length);
    }

    @Test
    public void deserializeJBP_capsityOfCustomerStorag(){
        JOSandJBP.serializeJBP("JunitTestDeserializeJBP", this.model);
        BusinessLogic modelTest = JOSandJBP.deserializeJBP("JunitTestDeserializeJBP");
        assertEquals(this.model.getAlleCustomer().size(), modelTest.getAlleCustomer().size());
    }

    @Test
    public void deserializeJBP_SamePsioneOfCargos0(){
        JOSandJBP.serializeJBP("JunitTestDeserializeJBP", this.model);
        BusinessLogic modelTest = JOSandJBP.deserializeJBP("JunitTestDeserializeJBP");
        Cargo[] cargos = modelTest.getAlleCargo();
        Cargo cargoTest= cargos[0];
        assertEquals(cargo1.getClass(), cargoTest.getClass());
    }

    @Test
    public void deserializeJBP_SamePsioneOfCargos1(){
        JOSandJBP.serializeJBP("JunitTestDeserializeJBP", this.model);
        BusinessLogic modelTest = JOSandJBP.deserializeJBP("JunitTestDeserializeJBP");
        Cargo[] cargos = modelTest.getAlleCargo();
        Cargo cargoTest= cargos[1];
        assertEquals(cargo2.getClass(), cargoTest.getClass());
    }

    @Test
    public void deserializeJBP_SamePsioneOfCargos2(){
        JOSandJBP.serializeJBP("JunitTestDeserializeJBP", this.model);
        BusinessLogic modelTest = JOSandJBP.deserializeJBP("JunitTestDeserializeJBP");
        Cargo[] cargos = modelTest.getAlleCargo();
        Cargo cargoTest= cargos[2];
        assertEquals(cargo3.getClass(), cargoTest.getClass());
    }

    @Test
    public void deserializeJBP_customer(){
        JOSandJBP.serializeJBP("JunitTestDeserializeJBP", this.model);
        BusinessLogic modelTest = JOSandJBP.deserializeJBP("JunitTestDeserializeJBP");
        assertEquals(this.customer.getName(), modelTest.getCustomer("Dennis").getName() );
    }

    @Test
    public void deserializeJBP_LiquidBulkCargoImplCustomer(){
        JOSandJBP.serializeJBP("JunitTestDeserializeJBP", this.model);
        BusinessLogic modelTest = JOSandJBP.deserializeJBP("JunitTestDeserializeJBP");
        Cargo[] cargos = modelTest.getAlleCargo();
        Cargo cargoTest= cargos[0];
        assertEquals(cargo1.getOwner().getName(), cargoTest.getOwner().getName());
    }

    @Test
    public void deserializeJBP_LiquidBulkCargoImplValue(){
        JOSandJBP.serializeJBP("JunitTestDeserializeJBP", this.model);
        BusinessLogic modelTest = JOSandJBP.deserializeJBP("JunitTestDeserializeJBP");
        Cargo[] cargos = modelTest.getAlleCargo();
        Cargo cargoTest= cargos[0];
        assertEquals(cargo1.getOwner().getMaxValue(), cargoTest.getOwner().getMaxValue());
    }

    @Test
    public void deserializeJBP_LiquidBulkCargoImplDuration(){
        JOSandJBP.serializeJBP("JunitTestDeserializeJBP", this.model);
        BusinessLogic modelTest = JOSandJBP.deserializeJBP("JunitTestDeserializeJBP");
        Cargo[] cargos = modelTest.getAlleCargo();
        Cargo cargoTest= cargos[0];
        assertEquals(cargo1.getOwner().getMaxDurationOfStorage(), cargoTest.getOwner().getMaxDurationOfStorage());
    }

    @Test
    public void deserializeJBP_LiquidBulkCargoImplLatInspectionDate(){
        JOSandJBP.serializeJBP("JunitTestDeserializeJBP", this.model);
        BusinessLogic modelTest = JOSandJBP.deserializeJBP("JunitTestDeserializeJBP");
        Cargo[] cargos = modelTest.getAlleCargo();
        Cargo cargoTest= cargos[0];
        assertEquals(cargo1.getLastInspectionDate(), cargoTest.getLastInspectionDate());
    }

    @Test
    public void deserializeJBP__LiquidBulkCargoImplHazard(){
        JOSandJBP.serializeJBP("JunitTestDeserializeJBP", this.model);
        BusinessLogic modelTest = JOSandJBP.deserializeJBP("JunitTestDeserializeJBP");
        Cargo[] cargos = modelTest.getAlleCargo();
        Cargo cargoTest= cargos[0];
        assertEquals(cargo1.getHazards(), cargoTest.getHazards());
    }

    @Test
    public void deserializeJBP_UnitisedCargoImplCustomer(){
        JOSandJBP.serializeJBP("JunitTestDeserializeJBP", this.model);
        BusinessLogic modelTest = JOSandJBP.deserializeJBP("JunitTestDeserializeJBP");
        Cargo[] cargos = modelTest.getAlleCargo();
        Cargo cargoTest= cargos[1];
        assertEquals(cargo2.getOwner().getName(), cargoTest.getOwner().getName());
    }

    @Test
    public void deserializeJBP_UnitisedCargoImpllValue(){
        JOSandJBP.serializeJBP("JunitTestDeserializeJBP", this.model);
        BusinessLogic modelTest = JOSandJBP.deserializeJBP("JunitTestDeserializeJBP");
        Cargo[] cargos = modelTest.getAlleCargo();
        Cargo cargoTest= cargos[1];
        assertEquals(cargo2.getOwner().getMaxValue(), cargoTest.getOwner().getMaxValue());
    }

    @Test
    public void deserializeJBP_UnitisedCargoImplDuration(){
        JOSandJBP.serializeJBP("JunitTestDeserializeJBP", this.model);
        BusinessLogic modelTest = JOSandJBP.deserializeJBP("JunitTestDeserializeJBP");
        Cargo[] cargos = modelTest.getAlleCargo();
        Cargo cargoTest= cargos[1];
        assertEquals(cargo2.getOwner().getMaxDurationOfStorage(), cargoTest.getOwner().getMaxDurationOfStorage());
    }

    @Test
    public void deserializeJBP_UnitisedCargoImplLastInspectionDate(){
        JOSandJBP.serializeJBP("JunitTestDeserializeJBP", this.model);
        BusinessLogic modelTest = JOSandJBP.deserializeJBP("JunitTestDeserializeJBP");
        Cargo[] cargos = modelTest.getAlleCargo();
        Cargo cargoTest= cargos[1];
        assertEquals(cargo2.getLastInspectionDate(), cargoTest.getLastInspectionDate());
    }

    @Test
    public void deserializeJBP_UnitisedCargoImplHazard(){
        JOSandJBP.serializeJBP("JunitTestDeserializeJBP", this.model);
        BusinessLogic modelTest = JOSandJBP.deserializeJBP("JunitTestDeserializeJBP");
        Cargo[] cargos = modelTest.getAlleCargo();
        Cargo cargoTest= cargos[1];
        assertEquals(cargo2.getHazards(), cargoTest.getHazards());
    }

    @Test
    public void deserializeJBP_MixedCargoLiquidBulkAndUnitisedImplCustomer(){
        JOSandJBP.serializeJBP("JunitTestDeserializeJBP", this.model);
        BusinessLogic modelTest = JOSandJBP.deserializeJBP("JunitTestDeserializeJBP");
        Cargo[] cargos = modelTest.getAlleCargo();
        Cargo cargoTest= cargos[2];
        assertEquals(cargo3.getOwner().getName(), cargoTest.getOwner().getName());
    }

    @Test
    public void deserializeJBP_MixedCargoLiquidBulkAndUnitisedImplValue(){
        JOSandJBP.serializeJBP("JunitTestDeserializeJBP", this.model);
        BusinessLogic modelTest = JOSandJBP.deserializeJBP("JunitTestDeserializeJBP");
        Cargo[] cargos = modelTest.getAlleCargo();
        Cargo cargoTest= cargos[2];
        assertEquals(cargo3.getOwner().getMaxValue(), cargoTest.getOwner().getMaxValue());
    }

    @Test
    public void deserializeJBP_MixedCargoLiquidBulkAndUnitisedImplDuration(){
        JOSandJBP.serializeJBP("JunitTestDeserializeJBP", this.model);
        BusinessLogic modelTest = JOSandJBP.deserializeJBP("JunitTestDeserializeJBP");
        Cargo[] cargos = modelTest.getAlleCargo();
        Cargo cargoTest= cargos[2];
        assertEquals(cargo3.getOwner().getMaxDurationOfStorage(), cargoTest.getOwner().getMaxDurationOfStorage());
    }

    @Test
    public void deserializeJBP_MixedCargoLiquidBulkAndUnitisedImplLastInspectionDate(){
        JOSandJBP.serializeJBP("JunitTestDeserializeJBP", this.model);
        BusinessLogic modelTest = JOSandJBP.deserializeJBP("JunitTestDeserializeJBP");
        Cargo[] cargos = modelTest.getAlleCargo();
        Cargo cargoTest= cargos[2];
        assertEquals(cargo3.getLastInspectionDate(), cargoTest.getLastInspectionDate());
    }

    @Test
    public void deserializeJBP_MixedCargoLiquidBulkAndUnitisedImplHazard(){
        JOSandJBP.serializeJBP("JunitTestDeserializeJBP", this.model);
        BusinessLogic modelTest = JOSandJBP.deserializeJBP("JunitTestDeserializeJBP");
        Cargo[] cargos = modelTest.getAlleCargo();
        Cargo cargoTest= cargos[2];
        assertEquals(cargo3.getHazards(), cargoTest.getHazards());
    }

    @Test
    public void deserializeJBP_notFile(){
        JOSandJBP.serializeJBP("JunitTestDeserializeJBP", this.model);
        assertThrows(IllegalArgumentException.class, () -> {
            JOSandJBP.deserializeJBP(null);
        });
    }

    @Test
    public void deserializeJBP_falseFilename(){
        JOSandJBP.serializeJBP("JunitTestDeserializeJBP", this.model);
        assertThrows(IllegalArgumentException.class, () -> {
            JOSandJBP.deserializeJBP("JunitT");
        });
    }

    //----------------------------------- Cargo Persistens ---------------------------------------------------//

    @Test
    public void serializeCargo_fileExists()throws BusinessLogiceException{
        JOSandJBP.serializeCargo(0, model);
        assertTrue(new File("CargoSave.txt").exists());
    }

    @Test
    public void serializeCargo_CapasitivOutOfRange(){
        assertThrows(BusinessLogiceException.class, () -> {
            JOSandJBP.serializeCargo(40, model);
        });
    }

    @Test
    public void serializeCargo_NoCargoOnThePlace() {
        assertThrows(BusinessLogiceException.class, () -> {
            JOSandJBP.serializeCargo(4, model);
        });
    }

    @Test
    public void deserializeCargo_LiquidBulkCargoImplCustomer() throws BusinessLogiceException {
        JOSandJBP.serializeCargo(0, model);
        Cargo cargo = JOSandJBP.deserializeCargo();
        assertEquals(cargo1.getOwner().getName(), cargo.getOwner().getName());
    }

    @Test
    public void deserializeCargo_LiquidBulkCargoImplValue() throws BusinessLogiceException {
        JOSandJBP.serializeCargo(0, model);
        Cargo cargo = JOSandJBP.deserializeCargo();
        assertEquals(cargo1.getOwner().getMaxValue(),cargo.getOwner().getMaxValue());
    }

    @Test
    public void deserializeCargo_LiquidBulkCargoImplDuration() throws BusinessLogiceException {
        JOSandJBP.serializeCargo(0, model);
        Cargo cargo = JOSandJBP.deserializeCargo();
        assertEquals(cargo1.getOwner().getMaxDurationOfStorage(), cargo.getOwner().getMaxDurationOfStorage());
    }

    @Test
    public void deserializeCargo_LiquidBulkCargoImplLatInspectionDate() throws BusinessLogiceException {
        JOSandJBP.serializeCargo(0, model);
        Cargo cargo = JOSandJBP.deserializeCargo();
        assertEquals(cargo1.getLastInspectionDate(), cargo.getLastInspectionDate());
    }

    @Test
    public void deserializeCargo_LiquidBulkCargoImplHazard() throws BusinessLogiceException {
        JOSandJBP.serializeCargo(0, model);
        Cargo cargo = JOSandJBP.deserializeCargo();
        assertEquals(cargo1.getHazards(), cargo.getHazards());
    }

    @Test
    public void deserializeCargo_UnitisedCargoImplCustomer() throws BusinessLogiceException {
        JOSandJBP.serializeCargo(1, model);
        Cargo cargo = JOSandJBP.deserializeCargo();
        assertEquals(cargo2.getOwner().getName(), cargo.getOwner().getName());
    }

    @Test
    public void deserializeCargo_UnitisedCargoImpllValue() throws BusinessLogiceException {
        JOSandJBP.serializeCargo(1, model);
        Cargo cargo = JOSandJBP.deserializeCargo();
        assertEquals(cargo2.getOwner().getMaxValue(), cargo.getOwner().getMaxValue());
    }

    @Test
    public void deserializeCargo_UnitisedCargoImplDuration() throws BusinessLogiceException {
        JOSandJBP.serializeCargo(1, model);
        Cargo cargo = JOSandJBP.deserializeCargo();
        assertEquals(cargo2.getOwner().getMaxDurationOfStorage(), cargo.getOwner().getMaxDurationOfStorage());
    }

    @Test
    public void deserializeCargo_UnitisedCargoImplLastInspectionDate() throws BusinessLogiceException {
        JOSandJBP.serializeCargo(1, model);
        Cargo cargo = JOSandJBP.deserializeCargo();
        assertEquals(cargo2.getLastInspectionDate(), cargo.getLastInspectionDate());
    }

    @Test
    public void deserializeCargo_UnitisedCargoImplHazard() throws BusinessLogiceException {
        JOSandJBP.serializeCargo(1, model);
        Cargo cargo = JOSandJBP.deserializeCargo();
        assertEquals(cargo2.getHazards(), cargo.getHazards());
    }

    @Test
    public void deserializeCargo_MixedCargoLiquidBulkAndUnitisedImplCustomer() throws BusinessLogiceException {
        JOSandJBP.serializeCargo(2, model);
        Cargo cargo = JOSandJBP.deserializeCargo();
        assertEquals(cargo3.getOwner().getName(), cargo.getOwner().getName());
    }

    @Test
    public void deserializeCargo_MixedCargoLiquidBulkAndUnitisedImplValue() throws BusinessLogiceException {
        JOSandJBP.serializeCargo(2, model);
        Cargo cargo = JOSandJBP.deserializeCargo();
        assertEquals(cargo3.getOwner().getMaxValue(), cargo.getOwner().getMaxValue());
    }

    @Test
    public void deserializeCargo_MixedCargoLiquidBulkAndUnitisedImplDuration() throws BusinessLogiceException {
        JOSandJBP.serializeCargo(2, model);
        Cargo cargo = JOSandJBP.deserializeCargo();
        assertEquals(cargo3.getOwner().getMaxDurationOfStorage(), cargo.getOwner().getMaxDurationOfStorage());
    }

    @Test
    public void deserializeCargo_MixedCargoLiquidBulkAndUnitisedImplLastInspectionDate() throws BusinessLogiceException {
        JOSandJBP.serializeCargo(2, model);
        Cargo cargo = JOSandJBP.deserializeCargo();
        assertEquals(cargo3.getLastInspectionDate(), cargo.getLastInspectionDate());
    }

    @Test
    public void deserializeCargo_MixedCargoLiquidBulkAndUnitisedImplHazard() throws BusinessLogiceException {
        JOSandJBP.serializeCargo(2, model);
        Cargo cargo = JOSandJBP.deserializeCargo();
        assertEquals(cargo3.getHazards(), cargo.getHazards());
    }

}