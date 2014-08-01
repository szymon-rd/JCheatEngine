package pl.jacadev.jce.agent.res;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import pl.jacadev.jce.agent.tree.Tree;
import pl.jacadev.jce.agent.tree.items.Item;
import pl.jacadev.jce.agent.tree.items.ObjectItem;
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
    private ChoiceBox<ObjectItem> fieldValueChoice;

    @FXML
    private TextField fieldType;

    @FXML
    private Button setFieldBtn;

    @FXML
    private Button addFieldValueBtn;

    @FXML
    void handleSetField(ActionEvent event) {
        try {
            FieldValueSetter.setField(openedObject, openedField, fieldValue.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleRefreshClasses(ActionEvent event) {
        Tree.reloadClassesTree();
    }

    @FXML
    void handleRefreshField(ActionEvent event) {
        try {
            openField(openedField, openedObject);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleAddFieldValue(ActionEvent event) {
        try {
            Tree.addObject(openedField.get(openedObject));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private Field openedField;
    private Object openedObject;

    public void openField(Field field, Object obj) throws ReflectiveOperationException {
        methodPanel.setVisible(false);
        fieldPanel.setVisible(true);
        openedField = field;
        openedObject = obj;
        field.setAccessible(true);
        fieldName.setText(field.getName());
        fieldType.setText(openedField.getType().getName());

        fieldValue.setText("");
        fieldValueChoice.getItems().clear();
        addFieldValueBtn.setDisable(true);
        if (isPrimitive(field)) {
            fieldValue.setVisible(true);
            fieldValueChoice.setVisible(false);
            if (isStatic(field) || obj != null) fieldValue.setText((String) field.get(obj));
        } else {
            fieldValue.setVisible(false);
            fieldValueChoice.setVisible(true);
            if (isStatic(field) || obj != null){
                fieldValueChoice.setItems(Tree.getItems(field, obj));
                fieldValueChoice.setValue(fieldValueChoice.getItems().get(0));
                addFieldValueBtn.setDisable(false);
            }

        }

    }

    private static boolean isStatic(Field field) {
        return (field.getModifiers() & Modifier.STATIC) != 0;
    }

    private static boolean isPrimitive(Field field) {
        return FieldValueSetter.isParsable(field.getType());
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
