package pl.jacadev.jce.attacher;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sun.jvmstat.monitor.MonitorException;
import sun.tools.attach.WindowsAttachProvider;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

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

