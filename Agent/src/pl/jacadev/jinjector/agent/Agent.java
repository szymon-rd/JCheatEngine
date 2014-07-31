package pl.jacadev.jinjector.agent;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.instrument.Instrumentation;

public class Agent extends Application {
    public static Instrumentation INSTRUMENTATION;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("res/agent.fxml"));
        primaryStage.setTitle("JInjector");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void agentmain(String s, Instrumentation i) throws IOException {
        INSTRUMENTATION = i;
        launch();
    }

    public static void killAgent() {
        //INSTRUMENTATION.removeTransformer(transformer);
    }

    public static void main(String... args) {
        launch();
    }
}
