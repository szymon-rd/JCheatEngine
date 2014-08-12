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
import pl.jacadev.jce.agent.tree.*;
import pl.jacadev.jce.agent.utils.FieldValueSetter;

import javax.swing.tree.TreeNode;
import java.lang.reflect.*;
import java.util.Arrays;
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
        CLASS(CLASS_ICON, true), ENUM(ENUM_ICON, false), INTERFACE(INTERFACE_ICON, false), ANNOTATION(ANNOTATION_ICON, false),
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

    public ClassNode(Class aClass) {
        this.aClass = aClass;
        Type type = getType(aClass);
        this.constructable = type.constructable;
        setGraphic(new ImageView(type.image));
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

    private void newInstance() {
        if (constructable) {
            Constructor[] constrs = aClass.getDeclaredConstructors();
            ((constrs.length > 1) ?
                    Dialogs.create()
                            .owner(Agent.STAGE)
                            .title("New instance")
                            .message("Select constructor:")
                            .showChoices(Arrays.asList(constrs))
                    : Optional.of(constrs[0]))
                    .ifPresent(constructor -> {
                        final Control[] parameterPools = new Control[constructor.getParameterCount()];

                        Action actionNew = new AbstractAction("New") {
                            public void handle(ActionEvent event) {
                                Dialog d = (Dialog) event.getSource();
                                Optional<String> name = Dialogs.create()
                                        .owner(Agent.STAGE)
                                        .title("New instance")
                                        .message("Enter name:")
                                        .showTextInput();
                                name.ifPresent( a ->
                                        createNew(a, TreeUtil.getValues(parameterPools, constructor.getParameterTypes()))
                                );
                                d.hide();
                            }

                            private void createNew(String name, Object[] parameters) {
                                constructor.setAccessible(true);
                                try {
                                    Tree.addObject(name, constructor.newInstance(parameters));
                                } catch (ReflectiveOperationException e){
                                    e.printStackTrace();
                                }
                            }
                        };

                        Dialog dlg = new Dialog(Agent.STAGE, "New instance");
                        GridPane grid = new GridPane();
                        grid.setHgap(10);
                        grid.setVgap(10);
                        grid.setPadding(new Insets(0, 10, 0, 10));

                        Parameter[] parameters = constructor.getParameters();
                        for (int i = 0; i < parameters.length; i++) {
                            Parameter parameter = parameters[i];
                            grid.add(new Label(parameter.getName()), 0, i);
                            Control parameterIn;
                            if (FieldValueSetter.isParsable(parameter.getType())) {
                                TextField parameterField = new TextField();
                                parameterField.setPromptText(parameter.getType().getSimpleName());
                                parameterIn = parameterField;
                            } else {
                                ComboBox<ObjectNode> combo = new ComboBox<>();
                                combo.setPromptText(parameter.getType().getSimpleName());
                                combo.setItems(FXCollections.observableArrayList(Tree.getItems(parameter.getType())));
                                parameterIn = null;
                            }
                            grid.add(parameterIn, 1, i);
                            parameterPools[i] = parameterIn;
                        }

                        ButtonBar.setType(actionNew, ButtonBar.ButtonType.OK_DONE);
                        dlg.setMasthead("Create new instance of class " + toString());
                        dlg.setContent(grid);
                        dlg.getActions().addAll(actionNew, Dialog.Actions.CANCEL);
                        dlg.show();
                    });
        } else throw new Error("Can not create new instance of this class");
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
