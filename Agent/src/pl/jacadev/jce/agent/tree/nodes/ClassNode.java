package pl.jacadev.jce.agent.tree.nodes;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.controlsfx.control.ButtonBar;
import org.controlsfx.control.action.AbstractAction;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;
import pl.jacadev.jce.agent.Agent;
import pl.jacadev.jce.agent.res.Controller;
import pl.jacadev.jce.agent.tree.Tree;
import pl.jacadev.jce.agent.tree.TreeUtil;
import pl.jacadev.jce.agent.utils.FieldValueSetter;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author JacaDev
 */
public class ClassNode extends Node {

    private static final Image CLASS_ICON = new Image(Controller.class.getResourceAsStream("icons/classIcon.png"));
    private static final Image ENUM_ICON = new Image(Controller.class.getResourceAsStream("icons/enumIcon.png"));
    private static final Image INTERFACE_ICON = new Image(Controller.class.getResourceAsStream("icons/interfaceIcon.png"));
    private static final Image ANNOTATION_ICON = new Image(Controller.class.getResourceAsStream("icons/annotationIcon.png"));
    private static final Image ABSTRACT_ICON = new Image(Controller.class.getResourceAsStream("icons/abstractIcon.png"));

    private static enum Type {
        CLASS(CLASS_ICON, true), ENUM(ENUM_ICON, true), INTERFACE(INTERFACE_ICON, false), ANNOTATION(ANNOTATION_ICON, false),
        ABSTRACT(ABSTRACT_ICON, false);

        private Image image;
        private boolean constructable;

        private Type(Image image, boolean constructable) {
            this.image = image;
            this.constructable = constructable;
        }
    }


    private final Class aClass;
    private boolean isLoaded = false;
    private final boolean constructable;
    private final Type type;

    public ClassNode(Class aClass) {
        this.aClass = aClass;
        this.type = getType(aClass);
        this.constructable = this.type.constructable;
        setGraphic(new ImageView(this.type.image));
    }

    public Class getAClass() {
        return aClass;
    }

    @Override
    public String toString() {
        return aClass.getSimpleName();
    }

    @Override
    public void handleClick() {
        if (!isLoaded) {
            openFields();
            openMethods();
            isLoaded = true;
        }
    }

    @Override
    void setupMenu(ContextMenu menu) {
        if (constructable) {
            MenuItem item = new MenuItem("New instance");
            item.setOnAction(a -> newInstance());
            menu.getItems().add(item);
        }
    }

    public void newInstance() {
        if (constructable) {
            switch (this.type) {
                case CLASS:
                    newClassInstance();
                    break;
                case ENUM:
                    newEnumInstance();
            }
        } else throw new Error("Cannot create new instance of this class");
    }

    private void newEnumInstance() {
        List<Field> fields = Arrays.asList(Arrays.stream(aClass.getDeclaredFields())
                .filter(field -> !Modifier.isStatic(field.getModifiers()) && !field.isEnumConstant())
                .toArray(Field[]::new));
        showMultipleInput(
                fields.stream().map(Field::getName).toArray(String[]::new),
                fields.stream().map(Field::getType).toArray(Class<?>[]::new),
                "New instance", "Insert initial field values")
                .ifPresent(values -> showMultipleInput(
                        new String[]{"Name:", "Ordinal:"},
                        new Class<?>[]{String.class, int.class},
                        "New instance", "Insert enum instance attributes:")
                        .ifPresent(attributes -> {
                            try {
                                Object enumInstance = Agent.UNSAFE.allocateInstance(aClass);
                                Field[] declaredFields = (Field[]) fields.toArray();
                                for (int i = 0; i < declaredFields.length; i++) {
                                    Field field = declaredFields[i];
                                    field.setAccessible(true);
                                    field.set(enumInstance, values[i]);
                                }
                                Field name = Enum.class.getDeclaredField("name");
                                Field ordinal = Enum.class.getDeclaredField("ordinal");
                                name.setAccessible(true);
                                ordinal.setAccessible(true);
                                name.set(enumInstance, attributes[0]);
                                ordinal.set(enumInstance, attributes[1]);
                                Tree.addObject((String) attributes[0], enumInstance);
                            } catch (ReflectiveOperationException e) {
                                Agent.handleException(e);
                            }
                        }));
    }

    private void newClassInstance() {
        Constructor[] constrs = aClass.getDeclaredConstructors();
        ((constrs.length > 1) ?
                Dialogs.create()
                        .owner(Agent.STAGE)
                        .title("New instance")
                        .message("Select constructor:")
                        .showChoices(Arrays.asList(constrs))
                : Optional.of(constrs[0]))
                .ifPresent(constructor ->
                        showMultipleInput(
                                Arrays.stream(constructor.getParameters()).map(Parameter::getName).toArray(String[]::new),
                                constructor.getParameterTypes(),
                                "New instance",
                                "Insert constructor parameters"
                        ).ifPresent(parameters ->
                                Dialogs.create()
                                        .owner(Agent.STAGE)
                                        .title("New instance")
                                        .message("Enter name")
                                        .showTextInput()
                                        .ifPresent(name -> {
                                            try {
                                                Object instance = constructor.newInstance(parameters);
                                                Tree.addObject(name, instance);
                                            } catch (ReflectiveOperationException e) {
                                                Agent.handleException(e);
                                            }
                                        })));
    }

    @SuppressWarnings("unchecked")
    public static Optional<Object[]> showMultipleInput(String[] names, Class<?>[] types, String dialogName, String masthead) {
        final Control[] inputControls = new Control[types.length];
        Dialog dlg = new Dialog(Agent.STAGE, dialogName);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));
        final Optional[] toReturn = {Optional.empty()};

        Action actionOk = new AbstractAction("Ok") {
            @Override
            public void handle(ActionEvent event) {
                toReturn[0] = Optional.of(TreeUtil.getValues(inputControls, types));
                dlg.hide();
            }
        };
        Action actionCancel = new AbstractAction("Cancel") {
            @Override
            public void handle(ActionEvent event) {
                dlg.hide();
            }
        };

        for (int i = 0; i < types.length; i++) {
            Class<?> type = types[i];
            grid.add(new Label(names[i]), 0, i);
            Control input;
            if (FieldValueSetter.isParsable(type)) {
                TextField textInputField = new TextField();
                textInputField.setPromptText(type.getSimpleName());
                input = textInputField;
            } else {
                ComboBox<ObjectNode> combo = new ComboBox<>();
                combo.setPromptText(type.getSimpleName());
                combo.setItems(FXCollections.observableArrayList(Tree.getItems(type)));
                input = combo;
            }
            grid.add(input, 1, i);
            inputControls[i] = input;
        }

        dlg.setMasthead(masthead);
        dlg.setContent(grid);
        ButtonBar.setType(actionOk, ButtonBar.ButtonType.OK_DONE);
        ButtonBar.setType(actionCancel, ButtonBar.ButtonType.CANCEL_CLOSE);
        dlg.getActions().addAll(actionOk, actionCancel);
        dlg.show();

        return toReturn[0];
    }

    private void openFields() {
        for (Field f : aClass.getDeclaredFields()) {
            getChildren().add(new FieldNode(f));
        }
    }

    private void openMethods() {
        for (Method m : aClass.getDeclaredMethods()) {
            getChildren().add(new MethodNode(m));
        }
    }

    private static Type getType(Class<?> aClass) {
        if (aClass.isAnnotation()) return Type.ANNOTATION;
        if (aClass.isEnum()) return Type.ENUM;
        if (aClass.isInterface()) return Type.INTERFACE;
        if ((aClass.getModifiers() & Modifier.ABSTRACT) != 0) return Type.ABSTRACT;
        return Type.CLASS;
    }
}
