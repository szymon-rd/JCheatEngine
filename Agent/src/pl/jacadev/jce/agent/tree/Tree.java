package pl.jacadev.jce.agent.tree;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import pl.jacadev.jce.agent.tree.items.Item;
import pl.jacadev.jce.agent.tree.items.JCETreeCell;
import pl.jacadev.jce.agent.tree.items.PackageItem;
import pl.jacadev.jce.agent.tree.items.TextItem;
import pl.jacadev.jce.agent.utils.AUtil;

import java.util.Optional;

/**
 * @author JacaDev
 */
public class Tree {

    private static TreeItem root;
    private static TreeItem objects;
    private static TreeItem classes;

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
        defaultPack.getChildren().addAll(ApplicationMap.getClassesInDefault());
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
}
