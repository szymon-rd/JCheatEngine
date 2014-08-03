package pl.jacadev.jce.agent.tree;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import pl.jacadev.jce.agent.tree.items.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author JacaDev
 */
public class Tree {

    private static Item objects;
    private static Item classes;

    @SuppressWarnings("unchecked")
    public static void createTree(TreeView<Item> classes, TreeView<Item> objects) {
        classes.setCellFactory(itemTreeView -> new JCETreeCell());
        objects.setCellFactory(itemTreeView -> new JCETreeCell());
        Item classesRoot = new TextItem("Loaded classes");
        classesRoot.setExpanded(true);
        Item objectsRoot = new TextItem("Objects");
        Tree.objects = objectsRoot;
        Tree.classes = classesRoot;
        classes.setRoot(classesRoot);
        objects.setRoot(objectsRoot);
        loadPackages();
    }

    private static void loadPackages() {
        for (String s : ApplicationMap.getPackages()) {
            putPackage(s);
        }
        createDefaultPackage();
    }

    private static void createDefaultPackage() {
        TreeItem<Item> defaultPack = new PackageItem("<default>", true);
        defaultPack.getChildren().addAll(ApplicationMap.getClassesInDefaultPack());
        if(!defaultPack.getChildren().isEmpty()) classes.getChildren().add(defaultPack);
    }

    @SuppressWarnings("unchecked")
    private static TreeItem putPackage(String path) {
        PackageItem[] packages = new PackageItem(path).path();
        TreeItem node = classes;
        for (int i = 0; i < packages.length; i++) {
            PackageItem pack = packages[i];
            Optional<TreeItem<?>> packNode = TreeUtil.findEqual(node, pack);
            if (packNode.isPresent()) node = packNode.get();
            else {
                node.getChildren().add(pack);
                node = pack;
            }
        }
        return null;
    }

    public static void reloadClassesTree() {
        classes.getChildren().clear();
        loadPackages();
    }

    public static ObservableList<ObjectItem> getItems(Field field, Object obj) throws ReflectiveOperationException {
        List<ObjectItem> itemList = new ArrayList<>();
        if(field.get(obj) != null) itemList.add(new ObjectItem(field.get(obj)));
        itemList.addAll(Arrays.asList(getItems(field.getType())));
        return FXCollections.observableArrayList(itemList);
    }
    public static ObjectItem[] getItems(Class<?> type){
        return objects.getChildren().stream()
                .filter(a -> ((ObjectItem) a).getType().isAssignableFrom(type)).toArray(ObjectItem[]::new);
    }

    public static void addObject(Object object) {
        objects.getChildren().add(new ObjectItem(object));
    }
    public static void addObject(String name, Object object) {
        objects.getChildren().add(new ObjectItem(name, object));
    }
    public static void remove(ObjectItem item){
        objects.getChildren().remove(item);
    }
}
