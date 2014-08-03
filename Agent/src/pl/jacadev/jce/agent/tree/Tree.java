package pl.jacadev.jce.agent.tree;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import pl.jacadev.jce.agent.tree.items.JCETreeCell;
import pl.jacadev.jce.agent.tree.items.Item;
import pl.jacadev.jce.agent.tree.items.ObjectItem;
import pl.jacadev.jce.agent.tree.items.PackageItem;
import pl.jacadev.jce.agent.tree.items.TextItem;
import pl.jacadev.jce.agent.utils.AUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author JacaDev
 */
public class Tree {

    private static TreeItem<Item> root;
    private static TreeItem<Item> objects;
    private static TreeItem<Item> classes;

    @SuppressWarnings("unchecked")
    public static void createTree(TreeView<Item> tree) {
        tree.setCellFactory(itemTreeView -> new JCETreeCell());
        root = new TextItem("App: " + AUtil.getCurrentPid());
        objects = new TextItem("Objects");
        classes = new TextItem("Loaded classes");
        root.getChildren().addAll(objects, classes);
        root.setExpanded(true);
        tree.setRoot(root);
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
        classes.getChildren().add(defaultPack);
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
        itemList.add(new ObjectItem(field.get(obj)));
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
}
