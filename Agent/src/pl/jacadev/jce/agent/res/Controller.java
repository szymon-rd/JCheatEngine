package pl.jacadev.jce.agent.res;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import pl.jacadev.jce.agent.tree.Tree;
import pl.jacadev.jce.agent.tree.items.Item;
import pl.jacadev.jce.agent.utils.FieldValueSetter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public static Controller CONTROLLER;

    @FXML
    private TreeView<Item> tree;

    /**
     * METHOD PANEL
     */

    @FXML
    private AnchorPane methodPanel;

    @FXML
    private TextArea codeArea;

    @FXML
    private Label methodName;

    @FXML
    private Label methodAccess;

    /**
     * FIELD PANEL
     */

    @FXML
    private AnchorPane fieldPanel;

    @FXML
    private TextField fieldName;

    @FXML
    private TextField fieldValue;

    @FXML
    private TextField fieldType;

    @FXML
    private Button setFieldBtn;

    @FXML
    void handleSetField(ActionEvent event) {
        try {
            FieldValueSetter.setField(openedField, fieldValue.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleRefreshClasses(ActionEvent event){
        Tree.reloadClassesTree();
    }

    @FXML
    void handleRefreshField(ActionEvent event) {
        openFieldMenu(openedField);
    }

    private Field openedField;

    public void openFieldMenu(Field field) {
        methodPanel.setVisible(false);
        fieldPanel.setVisible(true);
        openedField = field;
        field.setAccessible(true);
        fieldName.setText(field.getName());
        fieldType.setText(openedField.getType().getName());

        boolean modifiable = ((field.getModifiers() & Modifier.STATIC) != 0) && FieldValueSetter.isParsable(field.getType());
        setFieldBtn.setDisable(!modifiable);
        fieldValue.setEditable(modifiable);
        try {
            fieldValue.setText(modifiable ? field.get(null).toString() : "");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void openMethodMenu(Method method, byte[] bytecode) {
        fieldPanel.setVisible(false);
        methodPanel.setVisible(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CONTROLLER = this;
        Tree.createTree(tree);
    }

}
