package view;

import control.ControlModel;
import model.businessLogice.BusinessLogic;
import model.businessLogice.BusinessLogiceException;
import model.storageContract.administration.Customer;
import model.storageContract.administration.CustomerException;
import model.storageContract.cargo.CargoException;
import model.storageContract.cargo.Hazard;
import model.storageContract.cargo.LiquidBulkCargoImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LogObserverTest {

    private ControlModel model;

    @BeforeEach
    public void setup(){
        this.model=new ControlModel(new BusinessLogic(5));
    }

    @AfterEach
    public void deleteFile() throws IOException {
        Files.delete(Paths.get("testLog.txt"));
    }

    @Test
    public void LogObserver_creat() throws IOException {
        LogObserver observer = new LogObserver("en", "testLog", this.model);
        observer.update();
        assertEquals(LogObserver.class, observer.getClass());
    }

    @Test
    public void update_englishLogCargoAdd() throws CustomerException, IOException, CargoException, BusinessLogiceException {
        Customer customer =model.getModel().putCustomer("Dennis");
        LogObserver observer = new LogObserver("en", "testLog", this.model);
        model.getModel().login(observer);
        model.getModel().putCargo(new LiquidBulkCargoImpl(customer, BigDecimal.ZERO, Duration.ZERO, new HashSet<Hazard>(), true));
        BufferedReader bf = new BufferedReader(new FileReader("testLog.txt"));
        bf.readLine();
        assertEquals(new java.util.Date().toString() + " " + "A cargo was added from the model", bf.readLine());
    }

    @Test
    public void update_englishLogCargoDelete() throws CustomerException, IOException, CargoException, BusinessLogiceException {
        Customer customer =model.getModel().putCustomer("Dennis");
        LogObserver observer = new LogObserver("en", "testLog", this.model);
        model.getModel().login(observer);
        model.getModel().putCargo(new LiquidBulkCargoImpl(customer, BigDecimal.ZERO, Duration.ZERO, new HashSet<Hazard>(), true));
        model.getModel().deletCargo(0);
        BufferedReader bf = new BufferedReader(new FileReader("testLog.txt"));
        bf.readLine();
        bf.readLine();
        assertEquals(new java.util.Date().toString() + " " + "A cargo was deleted from the model", bf.readLine());
    }

    @Test
    public void update_englishLogCustomerAdd() throws CustomerException, IOException, CargoException, BusinessLogiceException {
        LogObserver observer = new LogObserver("en", "testLog", this.model);
        model.getModel().login(observer);
        model.getModel().putCustomer("Dennis");
        BufferedReader bf = new BufferedReader(new FileReader("testLog.txt"));
        assertEquals(new java.util.Date().toString() + " " + "A customer was added from the model", bf.readLine());
    }

    @Test
    public void update_englishLogCustomerDelete() throws CustomerException, IOException, CargoException, BusinessLogiceException {
        LogObserver observer = new LogObserver("en", "testLog", this.model);
        model.getModel().login(observer);
        model.getModel().putCustomer("Dennis");
        model.getModel().deletCustomer("Dennis");
        BufferedReader bf = new BufferedReader(new FileReader("testLog.txt"));
        bf.readLine();
        assertEquals(new java.util.Date().toString() + " " + "A customer was deleted from the model", bf.readLine());
    }

    @Test
    public void update_deutschLogCargoAdd() throws CustomerException, IOException, CargoException, BusinessLogiceException {
        Customer customer =model.getModel().putCustomer("Dennis");
        LogObserver observer = new LogObserver("de", "testLog", this.model);
        model.getModel().login(observer);
        model.getModel().putCargo(new LiquidBulkCargoImpl(customer, BigDecimal.ZERO, Duration.ZERO, new HashSet<Hazard>(), true));
        BufferedReader bf = new BufferedReader(new FileReader("testLog.txt"));
        bf.readLine();
        assertEquals(new java.util.Date().toString() + " " + "Eine Fracht wurde dem Model hinzugefügt", bf.readLine());
    }

    @Test
    public void update_deutschLogCargoDelete() throws CustomerException, IOException, CargoException, BusinessLogiceException {
        Customer customer =model.getModel().putCustomer("Dennis");
        LogObserver observer = new LogObserver("de", "testLog", this.model);
        model.getModel().login(observer);
        model.getModel().putCargo(new LiquidBulkCargoImpl(customer, BigDecimal.ZERO, Duration.ZERO, new HashSet<Hazard>(), true));
        model.getModel().deletCargo(0);
        BufferedReader bf = new BufferedReader(new FileReader("testLog.txt"));
        bf.readLine();
        bf.readLine();
        assertEquals(new java.util.Date().toString() + " " + "Eine Fracht wurde aus dem Model gelöscht", bf.readLine());
    }

    @Test
    public void update_deutschLogCustomerAdd() throws CustomerException, IOException, CargoException, BusinessLogiceException {
        LogObserver observer = new LogObserver("de", "testLog", this.model);
        model.getModel().login(observer);
        model.getModel().putCustomer("Dennis");
        BufferedReader bf = new BufferedReader(new FileReader("testLog.txt"));
        assertEquals(new java.util.Date().toString() + " " + "Ein Kunde wurde dem Model hinzugefügt", bf.readLine());
    }

    @Test
    public void update_deutschLogCustomerDelete() throws CustomerException, IOException, CargoException, BusinessLogiceException {
        LogObserver observer = new LogObserver("de", "testLog", this.model);
        model.getModel().login(observer);
        model.getModel().putCustomer("Dennis");
        model.getModel().deletCustomer("Dennis");
        BufferedReader bf = new BufferedReader(new FileReader("testLog.txt"));
        bf.readLine();
        assertEquals(new java.util.Date().toString() + " " + "Ein Kunde wurde aus dem Model gelöscht", bf.readLine());
    }



}