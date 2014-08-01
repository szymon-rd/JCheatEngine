package pl.jacadev.jce.agent.tree.cells;

import javafx.scene.control.TreeCell;
import javafx.scene.input.MouseEvent;
import pl.jacadev.jce.agent.tree.items.Item;


/**
 * @author JacaDev
 */
public class JCETreeCell extends TreeCell<Item> {
    public JCETreeCell() {
        addEventHandler(MouseEvent.MOUSE_CLICKED, event -> getItem().handleClick());
    }

    @Override
    protected void updateItem(Item item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            setText(getTreeItem().getValue().toString());
            setGraphic(getTreeItem().getGraphic());
            getItem().updateCell(this);
        }
    }


}
