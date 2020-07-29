package view;

import control.ControlModel;
import model.storageContract.cargo.Cargo;

import java.io.*;
import java.util.Map;

/**
 * @author Dennis Dominik Lehmann
 * Matrikelnummer 568827
 */
public class LogObserver implements Observer, Serializable {

    private final String language;
    private final ControlModel model;
    private int beforeCargo = 0;
    private int beforeCustomer = 0;
    private final File file;

    public LogObserver(String language, String filename, ControlModel cm) {
        this.language = language;
        this.file = new File(filename.concat(".txt"));
        this.model = cm;
    }

    @Override
    public void update() {
        try {
            int cargosSize = 0;
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            Map customers = model.getModel().getAlleCustomer();
            Cargo[] cargos = model.getModel().getAlleCargo();
            for (Cargo cargo : cargos) {
                if (cargo != null) {
                    cargosSize += 1;
                }
            }

            if (beforeCustomer > customers.size()) {
                if (language.equals("en")) {
                    this.writeToLog("A customer was deleted from the model", bw);
                } else {
                    this.writeToLog("Ein Kunde wurde aus dem Model gelöscht", bw);
                }
            } else if (beforeCustomer < customers.size()) {
                if (language.equals("en")) {
                    this.writeToLog("A customer was added from the model", bw);
                } else {
                    this.writeToLog("Ein Kunde wurde dem Model hinzugefügt", bw);
                }
            }
            if (beforeCargo > cargosSize) {
                if (language.equals("en")) {
                    this.writeToLog("A cargo was deleted from the model", bw);
                } else {
                    this.writeToLog("Eine Fracht wurde aus dem Model gelöscht", bw);
                }
            } else if (beforeCargo < cargosSize) {
                if (language.equals("en")) {
                    this.writeToLog("A cargo was added from the model", bw);
                } else {
                    this.writeToLog("Eine Fracht wurde dem Model hinzugefügt", bw);
                }
            }

            this.beforeCargo = cargosSize;
            this.beforeCustomer = customers.size();
            bw.flush();
            bw.close();
            fw.close();
        } catch (IOException i) {
            System.exit(0);
        }
    }

    private void writeToLog(String text, BufferedWriter bw) throws IOException {
        bw.write(new java.util.Date().toString() + " " + text);
        bw.newLine();
    }
}
