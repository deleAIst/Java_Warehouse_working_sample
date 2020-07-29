import control.ControlModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.businessLogice.BusinessLogic;
import view.LogObserver;
import view.Observer;
import control.ViewModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * @author Dennis Dominik Lehmann
 */
public class FxApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        boolean again = true;
        while (again) {
            try {
                System.out.println("Bitte geben sie hier ihre Start ein stellungen an.");
                System.out.println("[Lagerkapzität],[Log filename],[Log Sprache en/de]");
                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
                String settings = userInput.readLine();
                settings = settings.toLowerCase().trim();
                if (settings == null) break;
                StringTokenizer parameter = new StringTokenizer(settings, ",");
                if (parameter.countTokens() < 1) {
                    throw new IllegalArgumentException("Zu wennig Argumente eingegeben.");
                }
                int capacity = Integer.parseInt(parameter.nextToken().trim());
                if (capacity <= 0) {
                    throw new IllegalArgumentException("Die Capacity ist 0 oder Negativ das ist nicht Erlaubt.");
                }
                ControlModel controlModel = new ControlModel(new BusinessLogic(capacity));
                if (parameter.countTokens() >= 2) {
                    String filename = parameter.nextToken().trim();
                    String language = parameter.nextToken().trim();
                    if (!language.equals("en") && !language.equals("de")) {
                        throw new IllegalArgumentException("Es wurde keine der unterstüzten sprachen ausgewählt.");
                    }
                    Observer log = new LogObserver(language, filename, controlModel);
                    controlModel.getModel().login(log);
                }
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(FxApp.class.getResource("view/ViewFX.fxml"));
                AnchorPane rootLayout = (AnchorPane) loader.load();
                ViewModel controller = loader.<ViewModel>getController();
                controller.load(controlModel.getModel());
                Scene scene = new Scene(rootLayout);
                primaryStage.setTitle("Hello World");
                primaryStage.setScene(scene);
                primaryStage.show();
                again = false;

            } catch (IllegalArgumentException iae) {
                System.out.println(iae.getMessage());
            } catch (IOException ie) {
                System.out.println(ie.getMessage());
                System.exit(111);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}