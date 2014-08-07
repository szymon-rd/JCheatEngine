package pl.jacadev.jce.attacher.res;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import pl.jacadev.jce.attacher.AttachUtil;
import pl.jacadev.jce.attacher.JCEAttacher;
import pl.jacadev.jce.attacher.VM;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author JacaDev
 */
public class Controller implements Initializable {

    public static Controller CONTROLLER;

    @FXML
    private TableColumn<VM, String> nameColumn;

    @FXML
    private Button attachBtn;
    Tooltip attachBtnTip;

    @FXML
    private TableView<VM> table;

    @FXML
    private TableColumn<VM, String> pidColumn;

    @FXML
    void handleAttachAction(ActionEvent event) {
        ObservableList<TablePosition> selected = table.getSelectionModel().getSelectedCells();
        if (!selected.isEmpty()) {
            String pid = table.getItems().get(selected.get(0).getRow()).getPid();
            try {
                JCEAttacher.attachTo(pid);
            } catch (Exception e) {
                e.printStackTrace();
            }
            handleRefreshAction(null);
        }
    }

    @FXML
    public void handleRefreshAction(ActionEvent event) {
        table.setItems(AttachUtil.getAttachableVMs());
    }

    @FXML
    public void handleUpdate(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CONTROLLER = this;
        pidColumn.setCellValueFactory(new PropertyValueFactory<>("pid"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        handleRefreshAction(null);
    }


}
