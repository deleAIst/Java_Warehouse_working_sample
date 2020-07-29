package threads;

import model.businessLogice.BusinessLogic;
import model.businessLogice.BusinessLogiceException;
import model.businessLogice.Subject;
import model.storageContract.cargo.*;
import view.Observer;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Dennis Dominik Lehmann
 */
public class ThreadHandler implements Subject {
    private final Lock lock = new ReentrantLock();
    private final Condition full = this.lock.newCondition();
    private final Condition empty = this.lock.newCondition();
    private BusinessLogic businessLogic;
    private Boolean isFull = true;
    private String status;
    private final List<Observer> observerList = new LinkedList<Observer>();

    /**
     * @author Dennis Dominik Lehmann
     * Matrikelnummer 568827
     */
    public void put(BusinessLogic businessLogic, Cargo cargo) throws InterruptedException {
        this.lock.lock();

        this.businessLogic = businessLogic;
        try {
            if (this.businessLogic.isFull()) {
                this.full.signal();
                this.isFull = false;
            }
            while (!isFull) this.empty.await();
            this.status = "putLook";
            //this.notifySubjects();
            this.businessLogic.putCargo(cargo);
        } catch (BusinessLogiceException | CargoException e) {
            e.printStackTrace();
        } finally {
            this.status = "putUnlook";
            // this.notifySubjects();
            this.lock.unlock();

        }
    }

    public void relocate(BusinessLogic businessLogicRelocate) throws InterruptedException {
        // Boolean deletFail;
        Cargo cargoOld = null;
        this.lock.lock();
        while (this.isFull) this.full.await();
        //this.notifySubjects();
        this.status = "Aufgewacht";
        if (this.businessLogic == businessLogicRelocate) {
            this.lock.unlock();
            this.status = "Verlassen";
            // this.notifySubjects();
            return;
        }
        Cargo[] cargoStore = this.businessLogic.getAlleCargo();
        for (int i = 0; i < cargoStore.length; i++) {
            if (i == 0) {
                cargoOld = cargoStore[i];
            }
            if (cargoStore[i] != null) {
                assert cargoOld != null;
                if (cargoStore[i] != null & cargoStore[i].getLastInspectionDate().after(cargoOld.getLastInspectionDate())) {
                    cargoOld = cargoStore[i];
                }
            }
        }
        try {
            businessLogicRelocate.putCargo(cargoOld);

            if (cargoOld instanceof LiquidBulkCargoImpl) {
                LiquidBulkCargoImpl liquidBulkCargo = (LiquidBulkCargoImpl) cargoOld;
                this.businessLogic.deletCargo(liquidBulkCargo.getStorageSize());
            } else if (cargoOld instanceof UnitisedCargoImpl) {
                UnitisedCargoImpl unitisedCargo = (UnitisedCargoImpl) cargoOld;
                this.businessLogic.deletCargo(unitisedCargo.getStorageSize());
            } else if (cargoOld instanceof MixedCargoLiquidBulkAndUnitisedImpl) {
                MixedCargoLiquidBulkAndUnitisedImpl mixedCargoLiquidBulkAndUnitised = (MixedCargoLiquidBulkAndUnitisedImpl) cargoOld;
                this.businessLogic.deletCargo(mixedCargoLiquidBulkAndUnitised.getStorageSize());
            }
            this.empty.signal();
            this.isFull = true;
        } catch (BusinessLogiceException bl) {
            bl.getStackTrace();
        } catch (CargoException ce) {
            ce.getStackTrace();
        } finally {

            this.lock.unlock();
            this.status="Verlassen";
            //this.notifySubjects();

        }
    }

    public String getStatus() {
        return status;
    }

    @Override
    public void login(Observer observer) {
        this.observerList.add(observer);
    }

    @Override
    public void logOff(Observer observer) {
        this.observerList.remove(observer);
    }

    @Override
    public void notifySubjects() {
        for (Observer observer : observerList) {
            observer.update();
        }
    }
}
