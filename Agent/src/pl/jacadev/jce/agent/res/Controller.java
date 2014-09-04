package pl.jacadev.jce.agent.res;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import pl.jacadev.jce.agent.Agent;
import pl.jacadev.jce.agent.bytecode.mnemonics.Mnemonics;
import pl.jacadev.jce.agent.tree.Tree;
import pl.jacadev.jce.agent.tree.nodes.Node;
import pl.jacadev.jce.agent.tree.nodes.ObjectNode;
import pl.jacadev.jce.agent.utils.FieldValueSetter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * @author JacaDev
 */
public class Controller implements Initializable {
    public static Controller CONTROLLER;

    @FXML
    Label version;

    @FXML
    private TreeView<Node> classesTree;

    @FXML
    private TreeView<Node> objectsTree;

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
    private Label methodModifiers;

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
    private ComboBox<ObjectNode> fieldValueChoice;

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
            Agent.handleException(e);
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
            Agent.handleException(e);
        }
    }

    @FXML
    void handleAddFieldValue(ActionEvent event) {
        try {
            Tree.addObject(openedField.get(openedObject));
        } catch (IllegalAccessException e) {
            Agent.handleException(e);
        }
    }

    private Field openedField;
    private Object openedObject;

    public void openField(Field field, Object obj) throws ReflectiveOperationException {
        Objects.nonNull(field);
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

        boolean isPrimitive = isPrimitive(field);
        boolean isStatic = isStatic(field);
        boolean isObjNull = obj == null;

        fieldValue.setVisible(isPrimitive);
        fieldValueChoice.setVisible(!isPrimitive);
        if (isPrimitive && (isStatic || !isObjNull)) {
            fieldValue.setDisable(false);
            fieldValue.setText(field.get(obj).toString());
            setFieldBtn.setDisable(false);
        }
        if (!isPrimitive && (isStatic || !isObjNull)) {
            fieldValueChoice.setDisable(false);
            ObservableList<ObjectNode> items = Tree.getItems(field, obj);
            fieldValueChoice.setItems(items);
            if (items.size() > 0) {
                fieldValueChoice.setValue(fieldValueChoice.getItems().get(0));
                setFieldBtn.setDisable(false);
            }
            if (field.get(obj) != null)
                addFieldValueBtn.setDisable(false);
        }


    }

    private static boolean isStatic(Field field) {
        return (field.getModifiers() & Modifier.STATIC) != 0;
    }

    private static boolean isPrimitive(Field field) {
        return FieldValueSetter.isParsable(field.getType());
    }

    private Method openedMethod;

    public void openMethodMenu(Method method) {
        Objects.nonNull(method);
        fieldPanel.setVisible(false);
        methodPanel.setVisible(true);

        methodName.setText(method.getName());
        methodModifiers.setText(Modifier.toString(method.getModifiers()));
        codeArea.setText(Mnemonics.getMethodMnemonics(method));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CONTROLLER = this;
        version.setText("v " + Agent.VERSION);
        Tree.createTree(classesTree, objectsTree);
    }

}
