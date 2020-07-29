package threads;

import model.businessLogice.BusinessLogic;

import java.util.Random;

/**
 * @author Dennis Dominik Lehmann
 *
 */
public class Auslagerung implements Runnable {

    private final BusinessLogic[] bls;
    private final ThreadHandler th;
    private final Random randomiserBusiness = new Random();

    public Auslagerung(BusinessLogic[] bls, ThreadHandler th) {
        this.bls = bls;
        this.th=th;
    }

    @Override
    public void run() {
        while (true) {
            int numBusiness = this.randomiserBusiness.nextInt(this.bls.length);
            try {
                th.relocate(bls[numBusiness]);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
