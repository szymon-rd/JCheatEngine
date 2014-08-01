package pl.jacadev.jce.agent.tree.items;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import pl.jacadev.jce.agent.res.Controller;
import pl.jacadev.jce.agent.tree.ApplicationMap;

/**
 * @author JacaDev
 */
public class PackageItem extends Item {
    private static final Image PACK_ICON = new Image(Controller.class.getResourceAsStream("icons/packIcon.png"));
    private final EmptyItem emptyItem = new EmptyItem();
    private final String name;
    private boolean areClassesLoaded = false;

    public PackageItem(String name) {
        this.name = name;
        getChildren().add(emptyItem);
        setGraphic(new ImageView(PACK_ICON));
    }

    public PackageItem(String name, boolean areClassesLoaded) {
        this.areClassesLoaded = areClassesLoaded;
        this.name = name;
        setGraphic(new ImageView(PACK_ICON));
        if(!areClassesLoaded) getChildren().add(emptyItem);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name.substring(name.lastIndexOf('.') + 1);
    }

    public PackageItem[] path() {
        String[] packs = name.split("\\.");
        PackageItem[] path = new PackageItem[packs.length];
        String pName = "";
        for (int i = 0; i < path.length; i++) {
            path[i] = new PackageItem(pName += packs[i]);
            pName += ".";
        }
        return path;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj.equals(this.name);
    }

    public void loadClasses() {
        if (!areClassesLoaded) {
            getChildren().remove(emptyItem);
            getChildren().addAll(ApplicationMap.getClasses(this));
            areClassesLoaded = true;
        }
    }

    @Override
    public void handleBranchExpansion() {
        loadClasses();
    }

    private static class EmptyItem extends Item {

        @Override
        public String toString() {
            return "empty";
        }
    }
}
