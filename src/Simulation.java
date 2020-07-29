import model.businessLogice.BusinessLogic;
import threads.Auslagerung;
import threads.Einlagerung;
import threads.ThreadHandler;
import view.CargoDeletObserver;
import view.Observer;
import view.StorageObserver;
import view.ThreadObserver;

/**
 * @author Dennis Dominik Lehmann
 */
public class Simulation {
    public static void main(String[] args) {
        ThreadHandler th = new ThreadHandler();
        BusinessLogic[] bls = new BusinessLogic[3];
        StorageObserver[] so = new StorageObserver[3];
        CargoDeletObserver[] cdo = new CargoDeletObserver[3];

        for (int i = 0; i < 3; i++) {
            bls[i] = new BusinessLogic(10);
            so[i] = new StorageObserver(bls[i]);
            cdo[i] = new CargoDeletObserver(bls[i]);
            bls[i].login(so[i]);
            bls[i].login(cdo[i]);
        }
        Observer observer = new ThreadObserver(th);
        th.login(observer);

        Thread t1 = new Thread(new Einlagerung(bls, th));
        Thread t2 = new Thread(new Einlagerung(bls, th));
        Thread t3 = new Thread(new Auslagerung(bls, th));
        Thread t4 = new Thread(new Auslagerung(bls, th));
        Thread t5 = new Thread(new Auslagerung(bls, th));
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
    }
}
