package pl.jacadev.jce.attacher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sun.jvmstat.monitor.MonitorException;

import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;

/**
 * @author JacaDev
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("res/attacher.fxml"));
        primaryStage.setTitle("JCheatEngine");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String... args) throws InterruptedException, URISyntaxException, MonitorException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        launch();
    }

}

