package pl.jacadev.jce.agent.tree;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import pl.jacadev.jce.agent.tree.nodes.JCETreeCell;
import pl.jacadev.jce.agent.tree.nodes.Node;
import pl.jacadev.jce.agent.tree.nodes.ObjectNode;
import pl.jacadev.jce.agent.tree.nodes.PackageNode;
import pl.jacadev.jce.agent.tree.nodes.TextNode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author JacaDev
 */
public class Tree {

    private static Node objects;
    private static Node classes;

    @SuppressWarnings("unchecked")
    public static void createTree(TreeView<Node> classes, TreeView<Node> objects) {
        classes.setCellFactory(itemTreeView -> new JCETreeCell());
        objects.setCellFactory(itemTreeView -> new JCETreeCell());
        Node classesRoot = new TextNode("Loaded classes");
        classesRoot.setExpanded(true);
        Node objectsRoot = new TextNode("Objects");
        Tree.objects = objectsRoot;
        Tree.classes = classesRoot;
        classes.setRoot(classesRoot);
        objects.setRoot(objectsRoot);
        loadPackages();
    }

    private static void loadPackages() {
        for (String s : ApplicationContent.getPackages()) {
            putPackage(s);
        }
        createDefaultPackage();
    }

    private static void createDefaultPackage() {
        PackageNode defaultPack = new PackageNode("<default>", true);
        defaultPack.getChildren().addAll(ApplicationContent.getClassesInDefaultPackage());
        if(!defaultPack.getChildren().isEmpty()) classes.getChildren().add(defaultPack);
        defaultPack.sort();
    }

    @SuppressWarnings("unchecked")
    private static TreeItem putPackage(String path) {
        PackageNode[] packages = new PackageNode(path).path();
        TreeItem node = classes;
        for (int i = 0; i < packages.length; i++) {
            PackageNode pack = packages[i];
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

    public static ObservableList<ObjectNode> getItems(Field field, Object obj) throws ReflectiveOperationException {
        List<ObjectNode> itemList = new ArrayList<>();
        if(field.get(obj) != null) itemList.add(new ObjectNode(field.get(obj)));
        itemList.addAll(Arrays.asList(getItems(field.getType())));
        return FXCollections.observableArrayList(itemList);
    }
    public static ObjectNode[] getItems(Class<?> type){
        return objects.getChildren().stream()
                .filter(a -> ((ObjectNode) a).getType().isAssignableFrom(type))
                .toArray(ObjectNode[]::new);
    }

    public static void addObject(Object object) {
        objects.getChildren().add(new ObjectNode(object));
    }
    public static void addObject(String name, Object object) {
        objects.getChildren().add(new ObjectNode(name, object));
    }
    public static void remove(ObjectNode item){
        objects.getChildren().remove(item);
    }
}
