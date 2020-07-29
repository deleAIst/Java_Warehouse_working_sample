import model.businessLogice.BusinessLogic;
import model.businessLogice.BusinessLogiceException;
import model.storageContract.administration.Customer;
import model.storageContract.administration.CustomerException;
import model.storageContract.cargo.Cargo;
import model.storageContract.cargo.CargoException;
import model.storageContract.cargo.Hazard;
import model.storageContract.cargo.LiquidBulkCargoImpl;
import serialize.JOSandJBP;
import view.CargoDeletObserver;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Dennis Dominik Lehmann
 */
public class SerializeApp {

    public static void main(String[] args) throws CustomerException, CargoException, BusinessLogiceException {
        Collection<Hazard> hazard = new ArrayList<>();
        hazard.add(Hazard.toxic);
        BusinessLogic businessLogic = new BusinessLogic(2);
        Customer customer = businessLogic.putCustomer("Dennis");
        Cargo cargo= new LiquidBulkCargoImpl(customer, BigDecimal.ZERO, Duration.ofMinutes(23),hazard,true);
        businessLogic.putCargo(cargo);
        JOSandJBP.serializeJOS("test", businessLogic);
        BusinessLogic businessLogic1 = JOSandJBP.deserializeJOS("test");
        CargoDeletObserver cargoDeletObserver = new CargoDeletObserver(businessLogic);
        assert businessLogic1 != null;
        businessLogic1.login(cargoDeletObserver);
        Cargo cargo2 =new LiquidBulkCargoImpl(businessLogic1.putCustomer("Tim"), BigDecimal.valueOf(12.1), Duration.ofMinutes(23),hazard,true);
        businessLogic1.putCargo(cargo2);
        JOSandJBP.serializeJBP( "Hallo",businessLogic1);
        BusinessLogic businessLogic2 = JOSandJBP.deserializeJBP("Hallo");

    }
}
