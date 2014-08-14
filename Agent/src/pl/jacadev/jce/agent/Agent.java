package pl.jacadev.jce.agent;

import impl.org.controlsfx.i18n.Localization;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.controlsfx.dialog.Dialogs;
import pl.jacadev.jce.agent.bytecode.transformations.MainTransformer;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.Locale;

/**
 * @author JacaDev
 */
public class Agent extends Application {

    public static final String VERSION = "0.2";
    public static final Locale LANGUAGE = Locale.ENGLISH;

    public static final ClassLoader SYSTEM_CLASS_LOADER = ClassLoader.getSystemClassLoader();

    public static Instrumentation INSTRUMENTATION;
    public static Stage STAGE;
    @Override
    public void start(Stage primaryStage) throws Exception {
        STAGE = primaryStage;
        Localization.setLocale(LANGUAGE);
        Parent root = FXMLLoader.load(getClass().getResource("res/agent.fxml"));
        primaryStage.setTitle("JCheatEngine");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void agentmain(String s, Instrumentation i) throws IOException {
        INSTRUMENTATION = i;
        INSTRUMENTATION.addTransformer(MainTransformer.MAIN_TRANSFORMER);
        launch();
    }

    public static void killAgent() {
        INSTRUMENTATION.removeTransformer(MainTransformer.MAIN_TRANSFORMER);
    }

    public static void showError(String message){ //TODO implement
        Dialogs.create()
                .owner(STAGE)
                .title("Error")
                .message(message)
                .showError();
    }
    public static void showWarn(String message){ //TODO implement
        Dialogs.create()
                .owner(STAGE)
                .title("Error")
                .message(message)
                .showWarning();
    }

    public static void main(String... args) {
        launch();
    }
}
