package pl.jacadev.jce.agent;

import impl.org.controlsfx.i18n.Localization;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.Locale;

/**
 * @author JacaDev
 */
public class Agent extends Application {
    public static Instrumentation INSTRUMENTATION;
    public static Stage primaryStage;
    @Override
    public void start(Stage primaryStage) throws Exception {
        Agent.primaryStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("res/agent.fxml"));
        primaryStage.setTitle("JCheatEngine");
        primaryStage.setScene(new Scene(root));
        Localization.setLocale(Locale.ENGLISH);
        primaryStage.show();
    }


    public static void agentmain(String s, Instrumentation i) throws IOException {
        INSTRUMENTATION = i;
        launch();
    }

    public static void killAgent() {
        //Removing transformer (still TODO)
    }

    public static void main(String... args) {
        launch();
    }
}
