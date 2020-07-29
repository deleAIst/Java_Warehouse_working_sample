package view;

import threads.ThreadHandler;

/**
 * @author Dennis Dominik Lehmann
 * Matrikelnummer 568827
 */
public class ThreadObserver implements Observer{

    private final ThreadHandler threadHandler;

    public ThreadObserver(ThreadHandler threadHandler){
        this.threadHandler=threadHandler;
    }

    @Override
    public void update() {
        String status = threadHandler.getStatus();
        switch (status){
            case "putLook":
                System.out.println("Ein Einlagerungsthread hat den Sicherenbereich betreten.");
                break;
            case "putUnlook":
                System.out.println("Ein Einlagerungsthread hat den Sicherenbereich verlassen.");
                break;
            case "Aufgewacht":
                System.out.println("Ein Auslagerungthread Ist aufgewacht.");
                break;
            case "Verlassen":
                System.out.println("Ein Auslagerungthread hat den Sicherenbereich verlassen.");
                break;
        }

    }
}
