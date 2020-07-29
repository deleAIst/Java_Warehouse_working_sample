package threads;

import model.businessLogice.BusinessLogic;
import model.storageContract.administration.CustomerException;
import model.storageContract.cargo.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @author Dennis Dominik Lehmann
 */
public class Einlagerung implements Runnable {

    private final BusinessLogic[] bl;
    private final ThreadHandler th;
    private final Collection<Hazard> collection = new ConcurrentLinkedDeque<>();
    private final Random randomiserCargo = new Random();
    private final Random randomiserBusiness = new Random();

    public Einlagerung(BusinessLogic[] bl, ThreadHandler th) {
        this.bl = bl;
        this.th = th;
        for (BusinessLogic b : bl) {
            try {
                b.putCustomer("Dennis");
            } catch (CustomerException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void run() {

        boolean agrie = true;
        while (true) {
            try {
                int numBusiness = this.randomiserBusiness.nextInt(this.bl.length);
                int numCargo = this.randomiserCargo.nextInt(2);
                switch (numCargo) {
                    case 0:
                        liquid(numBusiness);
                        break;
                    case 1:
                        mixed(numBusiness);
                        break;
                    case 2:
                        unitis(numBusiness);
                        break;

                }
            } catch (InterruptedException ble) {
                ble.printStackTrace();
            }
        }
    }

    private void unitis(int numBusiness) throws InterruptedException {
        collection.add(Hazard.radioactive);
        collection.add(Hazard.explosive);
        Cargo cargo = new UnitisedCargoImpl(bl[numBusiness].getCustomer("Dennis"), BigDecimal.valueOf(15.5), Duration.ofMinutes(12), collection, true);
        th.put(bl[numBusiness], cargo);
    }

    private void mixed(int numBusiness) throws InterruptedException {
        collection.add(Hazard.flammable);
        collection.add(Hazard.toxic);
        Cargo cargo = new MixedCargoLiquidBulkAndUnitisedImpl(bl[numBusiness].getCustomer("Dennis"), BigDecimal.valueOf(13.9), Duration.ofMinutes(9), collection, true, false);
        th.put(bl[numBusiness], cargo);
    }

    private void liquid(int numBusiness) throws InterruptedException {
        collection.add(Hazard.toxic);
        collection.add(Hazard.explosive);
        Cargo cargo = new LiquidBulkCargoImpl(bl[numBusiness].getCustomer("Dennis"), BigDecimal.valueOf(22), Duration.ofMinutes(43), collection, false);
        th.put(bl[numBusiness], cargo);
    }

}
