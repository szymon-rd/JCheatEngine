package pl.jacadev.jce.attacher.res;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pl.jacadev.jce.attacher.AttachUtil;
import pl.jacadev.jce.attacher.JCEAttacher;
import pl.jacadev.jce.attacher.VM;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public static Controller CONTROLLER;

    @FXML
    private TableColumn<VM, String> nameColumn;

    @FXML
    private TableView<VM> table;

    @FXML
    private TableColumn<VM, String> pidColumn;

    @FXML
    void handleAttachAction(ActionEvent event) {
        ObservableList<TablePosition> selected = table.getSelectionModel().getSelectedCells();
        String pid = table.getItems().get(selected.get(0).getRow()).getPid();
        try {
            JCEAttacher.attachTo(pid);
        } catch (Exception e) {
            exceptionOccurred(e);
        }
        handleRefreshAction(null);
    }

    @FXML
    void handleOptionsAction(ActionEvent event) {
    }

    @FXML
    void handleHelpAction(ActionEvent event) {

    }

    @FXML
    public void handleRefreshAction(ActionEvent event) {
        table.setItems(AttachUtil.getAttachableVMs());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CONTROLLER = this;
        pidColumn.setCellValueFactory(new PropertyValueFactory<VM, String>("pid"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<VM, String>("name"));
        handleRefreshAction(null);
    }

    public void exceptionOccurred(Exception e) {
        e.printStackTrace();
    }
}
