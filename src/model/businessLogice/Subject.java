package model.businessLogice;

import view.Observer;

/**
 * @author Dennis Dominik Lehmann
 */
public interface Subject {


    void login(Observer observer);

    void logOff(Observer observer);

    void notifySubjects();
}
