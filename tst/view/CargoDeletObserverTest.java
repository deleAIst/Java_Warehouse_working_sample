package view;

import control.ControlModel;
import model.businessLogice.BusinessLogic;
import model.businessLogice.BusinessLogiceException;
import model.storageContract.administration.Customer;
import model.storageContract.administration.CustomerException;
import model.storageContract.cargo.CargoException;
import model.storageContract.cargo.Hazard;
import model.storageContract.cargo.LiquidBulkCargoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class CargoDeletObserverTest {

    private ControlModel model;

    @BeforeEach
    public void setup() {
        this.model = new ControlModel(new BusinessLogic(5));
    }


    @Test
    public void cargoDeletObserver_creat() throws IOException {
        CargoDeletObserver observer = new CargoDeletObserver(this.model.getModel());
        assertEquals(CargoDeletObserver.class, observer.getClass());
    }

    @Test
    public void update_good() throws CustomerException, CargoException, BusinessLogiceException {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        InputStream sysInBackup = System.in;
        System.setOut(new PrintStream(outContent));
        CargoDeletObserver observer = new CargoDeletObserver(this.model.getModel());
        this.model.getModel().login(observer);
        Customer customer = this.model.getModel().putCustomer("Dennis");
        model.getModel().putCargo(new LiquidBulkCargoImpl(customer, BigDecimal.ZERO, Duration.ZERO, new HashSet<Hazard>(), true));
        model.getModel().deletCargo(0);
        assertEquals("Es wurde ein Cargo gelöscht. In Lager:"+model.getModel().hashCode()+" hat noch Lagerfläche von:5\n", outContent.toString());

        System.setIn(sysInBackup);

    }

}