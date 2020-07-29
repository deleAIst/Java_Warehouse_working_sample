package view;

import model.storageContract.cargo.Hazard;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * @author Dennis Dominik Lehmann
 * Matrikelnummer 568827
 */
public class ViewCLI {

    private final PrintStream ps;

    public ViewCLI(PrintStream ps) {
        this.ps = ps;
    }

    public void cliInstructionsAndNotes(String flags) {
        if (flags.equals("Falsche Eingabe")) {
            ps.println("Das eingegeben Kommentar ist Falsch.");
        }if (flags.equals("Insert")) {
            ps.println("Sie sind im Einfügemodus:");
            ps.println("Wollen sie einen Customer Eingben dan 1 oder ein Cargo dan 2 oder exit");
        }if (flags.equals("Mode")) {
            ps.println("Sie habe die Auswahl zwischen 7 Modusen.");
            ps.println("Einfügemodus :c");
            ps.println("Löschmodus :d");
            ps.println("Anzeigemodus :r");
            ps.println("Änderungsmodus :u");
            ps.println("Persistenzmodus :p");
            ps.println("Konfigurationsmodus :config");
            ps.println("Beenden :exit");
            ps.println("Bitte geben sie jetzt ihren Modus ein mit dem Kürzel");
        }if (flags.equals("CustomerI")) {
            ps.println("Sie wollen einen neuen Customer hinzufügen Bitte geben sie den Name des Customers an.");
        }if (flags.equals("delete")) {
            ps.println("Sie sind im Löschenmodus:");
            ps.println("Wollen sie einen Customer Löschen dan 1 oder ein Cargo dan 2");
        }if (flags.equals("delete Customer")) {
            ps.println("Geben sie den Namen des Customers an den sie Löschen wollen.");
        }if (flags.equals("delete Cargo")) {
            ps.println("Geben sie den Lagerplatz ein des Cargos was sie Löschen wollen.");
        }if (flags.equals("Inspetions Datum")) {
            ps.println("Sie sind im Änderungsmodus:");
            ps.println("Bitte geben sie den Lagerplatz an wo das Inspektionsdatum auf den aktuellen Zeitpunkt gesetzt werden soll.");
        }if (flags.equals("display")) {
            ps.println("Sie sind im Anzeigemodus:");
            ps.println("Mit der 1 Anzeige der Kunden mit der Anzahl eingelagerter Frachtstücke");
            ps.println("Mit der 2 Anzeige der Frachtstücke");
            ps.println("Mit der 3 Anzeige der vorhandenen bzw. nicht vorhandenen Gefahrenstoffe");
            ps.println("Mit exit kommen sie zurück in das Haupmenü");
        }if (flags.equals("Cargo FrachtType Display")) {
            ps.println("Bitte einer der drei Fracht Typen angben oder keinen um Alles Anzuzeigen. Unitised, Liquid, Mixed");
        }if (flags.equals("SaveJOS")) {
            ps.println("Bitte geben sie den Filename für das Speichern in JOS an");
        }if (flags.equals("SaveJBP")) {
            ps.println("Bitte geben sie den Filename für das Speichern in JBP an");
        }if (flags.equals("LoadJOS")) {
            ps.println("Bitte geben sie den Filename für das Laden in JOS an");
        }if (flags.equals("LoadJBP")) {
            ps.println("Bitte geben sie den Filename für das Laden in JBP an");
        }if (flags.equals("Persistence")) {
            ps.println("Sie sind im Persistenenzmodus:");
            ps.println("Mit der 1 Speichern mit JOS");
            ps.println("Mit der 2 Speichern mit JBP");
            ps.println("Mit der 3 Laden mit JOS");
            ps.println("Mit der 4 Laden mit JBP");
            ps.println("Mit der 5 Speichern eines Cargos");
            ps.println("Mit der 6 Laden eines Cargos");
            ps.println("Mit exit kommen sie zurück in das Haupmenü");
        }if (flags.equals("IO Fehler")) {
            ps.println("cannot read from input stream");
        }if (flags.equals("Config")) {
            ps.println("Sie sind im Configmodus:");
            ps.println("Mit der 1 add von Beobachtern");
            ps.println("Mit der 2 remove von Beobachtern");
            ps.println("Mit exit kommen sie zurück in das Haupmenü");
        }if (flags.equals("insertCargo")) {
            ps.println("Bitte geben sie das Cargo in dieser Form an:");
            ps.println("[Frachttyp] [Kundenname] [Wert] [Einlagerungsdauer in Sekunden] " +
                    "[kommaseparierte Gefahrenstoffe, einzelnes Komma für keine] " +
                    "[[zerbrechlich (y/n)] [unter Druck (y/n)] [fest (y/n)]] ");
        }if (flags.equals("Hazard Display")) {
            ps.println("Bitte geben sie True ein um die Enthaltende Harzard sich anzeigen zu lassen oder False um die nicht enthaltenden.");
        }if(flags.equals("delete Customer final")){
            ps.println("Der Customer wurde gelöscht und die dazugehörigen Cargos auch.");
        }if(flags.equals("delete Cargo final")){
            ps.println("Cargo wurde gelöscht.");
        }if(flags.equals("customer display")){
            ps.println("Customer mit eingelagerter Cargo Anzahl.");
        }if(flags.equals("Inspetions Datum final")){
            ps.println("Das Inspation Datum wurde auf die Aktuelle Zeit gesetzt.");
        }if(flags.equals("Add")){
            ps.println("Bitte geben sie einen der Observer an(die Zahl), den sie Einhängen wollen: CargoDelet(1), Hazard(2), Storage(3), StorageSpace(4)");
        }if(flags.equals("Remove")){
            ps.println("Bitte geben sie einen der Observer an(die Zahl), den sie Aushängen wollen: CargoDelet(1), Hazard(2), Storage(3), StorageSpace(4)");
        }if(flags.equals("SaveCargo")){
            ps.println("Bitte geben sie den Platz an wo das Cargo liegt das sie Speichern möchten.");
        }if(flags.equals("LoadCargo")){
            ps.println("Bitte geben sie den Platz an wo das Cargo liegt soll das geladen werden soll.");
        }
    }

    public void printError(String error) {
        ps.println(error);
    }

    public void typeDisplay(String display) {
        ps.println(display);
    }

    public void hazardInclude(HashSet<Hazard> hazards) {
        ps.println("In den Cargos enthalten sind:");
        List<Hazard> list = new ArrayList<Hazard>(hazards);
        Collections.sort(list);
        for (Hazard h : list) {
            ps.println(h + ",");
        }
        ps.println();
    }

    public void hazardNotInclude(HashSet<Hazard> hazards) {
        ps.println("In den Cargos nicht enthalten sind:");
        List<Hazard> list = new ArrayList<Hazard>(hazards);
        Collections.sort(list);
        for (Hazard h : list) {
            ps.println(h + ",");
        }
        ps.println();
    }

}
