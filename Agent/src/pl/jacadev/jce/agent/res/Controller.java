package pl.jacadev.jce.agent.res;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import pl.jacadev.jce.agent.Agent;
import pl.jacadev.jce.agent.tree.Tree;
import pl.jacadev.jce.agent.tree.items.Item;
import pl.jacadev.jce.agent.tree.items.ObjectItem;
import pl.jacadev.jce.agent.utils.FieldValueSetter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author JacaDev
 */
public class Controller implements Initializable {
    public static Controller CONTROLLER;

    @FXML Label version;

    @FXML
    private TreeView<Item> classesTree;

    @FXML
    private TreeView<Item> objectsTree;

    /**
     * METHOD PANEL
     */

    @FXML
    private AnchorPane methodPanel;

    @FXML
    private WebView codeView;

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
    private ComboBox<ObjectItem> fieldValueChoice;

    @FXML
    private TextField fieldType;

    @FXML
    private TextField fieldModifiers;

    @FXML
    private Button setFieldBtn;

    @FXML
    private Button addFieldValueBtn;

    @FXML
    void handleSetField(ActionEvent event) {
        try {
            if (isPrimitive(openedField)) FieldValueSetter.setField(openedObject, openedField, fieldValue.getText());
            else FieldValueSetter.setField(openedObject, openedField, fieldValueChoice.getValue().getObject());
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
        fieldModifiers.setText(Modifier.toString(field.getModifiers()));

        fieldValue.setText("");
        fieldValueChoice.getItems().clear();
        addFieldValueBtn.setDisable(true);
        setFieldBtn.setDisable(true);

        fieldValue.setDisable(true);
        fieldValueChoice.setDisable(true);
        if (isPrimitive(field)) {
            fieldValue.setVisible(true);
            fieldValueChoice.setVisible(false);
            if (isStatic(field) || obj != null) {
                fieldValue.setDisable(false);
                fieldValue.setText(field.get(obj).toString());
                setFieldBtn.setDisable(false);
            }
        } else {
            fieldValue.setVisible(false);
            fieldValueChoice.setVisible(true);
            if (isStatic(field) || obj != null) {
                fieldValueChoice.setDisable(false);
                ObservableList<ObjectItem> items = Tree.getItems(field, obj);
                fieldValueChoice.setItems(items);
                if(items.size() > 0){
                    fieldValueChoice.setValue(fieldValueChoice.getItems().get(0));
                    setFieldBtn.setDisable(false);
                }
                if(field.get(obj) != null)
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
        version.setText("v " + Agent.VERSION);
        Tree.createTree(classesTree, objectsTree);
    }

}
