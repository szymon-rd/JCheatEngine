package pl.jacadev.jce.agent.tree.items;

import javafx.scene.control.TreeItem;
import pl.jacadev.jce.agent.tree.cells.JCETreeCell;

/**
 * @author JacaDev
 */
public abstract class Item extends TreeItem<Item> {

    public Item() {
        addEventHandler(TreeItem.branchExpandedEvent(), objectTreeModificationEvent -> handleBranchExpansion());
        setValue(this);
    }

    public boolean isEditable(){
        return false;
    }

    public void handleClick(){
    }
    public void handleBranchExpansion(){
    }
    public void updateCell(JCETreeCell cell) {
    }
    public void commitEdit(String text) {
    }
}
