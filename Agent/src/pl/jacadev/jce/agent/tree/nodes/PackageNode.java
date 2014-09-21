package pl.jacadev.jce.agent.tree.nodes;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import pl.jacadev.jce.agent.res.Controller;
import pl.jacadev.jce.agent.tree.ApplicationContent;

/**
 * @author JacaDev
 */
public class PackageNode extends Node {
    private static final Image PACK_ICON = new Image(Controller.class.getResourceAsStream("icons/packIcon.png"));
    private final EmptyNode emptyItem = new EmptyNode();
    private final String name;
    private boolean areClassesLoaded = false;
    public PackageNode(String name) {
        this.name = name;
        getChildren().add(emptyItem);
        setGraphic(new ImageView(PACK_ICON));
    }

    public PackageNode(String name, boolean areClassesLoaded) {
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

    /**
     * @return all PackageItems that contain this.
     */
    public PackageNode[] path() {
        String[] packs = name.split("\\.");
        PackageNode[] path = new PackageNode[packs.length];
        String pName = "";
        for (int i = 0; i < path.length; i++) {
            path[i] = new PackageNode(pName += packs[i]);
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
            getChildren().addAll(ApplicationContent.getClasses(this));
            areClassesLoaded = true;
            sort();
        }
    }
    public void sort(){
        getChildren().sort((o1, o2) -> {
            if(!((o1 instanceof PackageNode) ^ (o2 instanceof PackageNode))){
                return String.CASE_INSENSITIVE_ORDER.compare(o1.toString(), o2.toString());
            }else if(o1 instanceof PackageNode){
                return 1;
            }else{
                return -1;
            }
        });
    }

    @Override
    public void handleBranchExpansion() {
        loadClasses();
    }

    private static class EmptyNode extends Node {

        @Override
        public String toString() {
            return "empty";
        }
    }
}
