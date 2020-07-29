package control;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GeneralSettingsTest {

    private ControlModel controlModel;
    InputStream sysInBackup;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final String mainText = "Bitte geben sie hier ihre Start ein stellungen an.\n[Lagerkapzität],[Log filename],[Log Sprache en/de]\n";

    @BeforeEach
    public void setup() {
        this.sysInBackup = System.in;
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void setSystemInBack() {
        System.setIn(this.sysInBackup);
        System.setOut(this.originalOut);
    }

    @Test
    public void setupCall_withStorageCapasity() throws IOException {
        //https://stackoverflow.com/questions/6415728/junit-testing-with-simulated-user-input
        ByteArrayInputStream in = new ByteArrayInputStream("20".getBytes());
        System.setIn(in);
        GeneralSettings gs = new GeneralSettings();
        gs.setup();
        assertEquals(this.mainText+"Sie habe die Auswahl zwischen 7 Modusen.\nEinfügemodus :c\nLöschmodus :d\nAnzeigemodus :r\n" +
                "Änderungsmodus :u\nPersistenzmodus :p\nKonfigurationsmodus :config\nBeenden :exit" +
                "\nBitte geben sie jetzt ihren Modus ein mit dem Kürzel\n",
                this.outContent.toString());
    }

    @Test
    public void setupCall_WithStorageCapasityAndLogEnglisch() throws IOException {
        //https://stackoverflow.com/questions/6415728/junit-testing-with-simulated-user-input
        ByteArrayInputStream in = new ByteArrayInputStream("20, test, en".getBytes());
        System.setIn(in);
        GeneralSettings gs = new GeneralSettings();
        gs.setup();
        assertEquals(this.mainText+"Sie habe die Auswahl zwischen 7 Modusen.\nEinfügemodus :c\nLöschmodus :d\nAnzeigemodus :r\n" +
                        "Änderungsmodus :u\nPersistenzmodus :p\nKonfigurationsmodus :config\nBeenden :exit" +
                        "\nBitte geben sie jetzt ihren Modus ein mit dem Kürzel\n",
                this.outContent.toString());
    }

    @Test
    public void setupCall_WithStorageCapasityAndLogEnglischFalse() throws IOException {
        //https://stackoverflow.com/questions/6415728/junit-testing-with-simulated-user-input
        ByteArrayInputStream in = new ByteArrayInputStream("20, en, test".getBytes());
        System.setIn(in);
        GeneralSettings gs = new GeneralSettings();
        gs.setup();
        assertEquals(this.mainText+
                "Es wurde keine der unterstüzten sprachen ausgewählt.\n"+
                this.mainText,
                this.outContent.toString());
    }

    @Test
    public void setupCall_nullArgument() throws IOException {
        //https://stackoverflow.com/questions/6415728/junit-testing-with-simulated-user-input
        ByteArrayInputStream in = new ByteArrayInputStream("".getBytes());
        System.setIn(in);
        GeneralSettings gs = new GeneralSettings();
        gs.setup();
        assertEquals(this.mainText,
                this.outContent.toString());
    }

    @Test
    public void setupCall_falseCapasity1() throws IOException {
        //https://stackoverflow.com/questions/6415728/junit-testing-with-simulated-user-input
        ByteArrayInputStream in = new ByteArrayInputStream(" ".getBytes());
        System.setIn(in);
        GeneralSettings gs = new GeneralSettings();
        gs.setup();
        assertEquals(this.mainText+ "Zu wennig Argumente eingegeben.\n"+this.mainText,
                this.outContent.toString());
    }

    @Test
    public void setupCall_falseCapasity2() throws IOException {
        //https://stackoverflow.com/questions/6415728/junit-testing-with-simulated-user-input
        ByteArrayInputStream in = new ByteArrayInputStream("b".getBytes());
        System.setIn(in);
        GeneralSettings gs = new GeneralSettings();
        gs.setup();
        assertEquals(this.mainText+ "For input string: \"b\"\n"+this.mainText,
                this.outContent.toString());
    }

    @Test
    public void setupCall_false() throws IOException {
        //https://stackoverflow.com/questions/6415728/junit-testing-with-simulated-user-input
        ByteArrayInputStream in = new ByteArrayInputStream("b,".getBytes());
        System.setIn(in);
        GeneralSettings gs = new GeneralSettings();
        gs.setup();
        assertEquals(this.mainText+ "For input string: \"b\"\n"+this.mainText,
                this.outContent.toString());
    }

    @Test
    public void setupCall_falseLog() throws IOException {
        //https://stackoverflow.com/questions/6415728/junit-testing-with-simulated-user-input
        ByteArrayInputStream in = new ByteArrayInputStream("20, test".getBytes());
        System.setIn(in);
        GeneralSettings gs = new GeneralSettings();
        gs.setup();
        assertEquals(this.mainText+ "Zu wennig oder zu viele Argumente Argumente eingegeben.\n"+this.mainText,
                this.outContent.toString());
    }

    @Test
    public void setupCall_negetivNumber() throws IOException {
        //https://stackoverflow.com/questions/6415728/junit-testing-with-simulated-user-input
        ByteArrayInputStream in = new ByteArrayInputStream("-2".getBytes());
        System.setIn(in);
        GeneralSettings gs = new GeneralSettings();
        gs.setup();
        assertEquals(this.mainText + "Die Capacity ist 0 oder Negativ das ist nicht Erlaubt.\n" + this.mainText,
                this.outContent.toString());
    }

}