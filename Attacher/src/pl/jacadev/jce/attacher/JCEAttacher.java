package pl.jacadev.jce.attacher;

import pl.jacadev.jce.agent.Agent;
import pl.jacadev.jce.agent.res.Controller;
import pl.jacadev.jce.agent.tree.ApplicationMap;
import pl.jacadev.jce.agent.tree.Tree;
import pl.jacadev.jce.agent.tree.TreeUtil;
import pl.jacadev.jce.agent.tree.cells.EditableTreeCell;
import pl.jacadev.jce.agent.tree.cells.JCETreeCell;
import pl.jacadev.jce.agent.tree.items.*;
import pl.jacadev.jce.agent.utils.AUtil;
import pl.jacadev.jce.agent.utils.FieldValueSetter;
import pl.jacadev.jce.agent.utils.mnemonics.Mnemonics;

/**
 * @author JacaDev
 */
public class JCEAttacher {
    private static final Class[] classes = {
           Agent.class, Controller.class, FieldValueSetter.class, Mnemonics.class, AUtil.class,
            Item.class, ClassItem.class, MethodItem.class, FieldItem.class, PackageItem.class,
            TextItem.class, ObjectItem.class, Tree.class, TreeUtil.class, ApplicationMap.class,
            JCETreeCell.class, EditableTreeCell.class
    };
    private static final Resource[] resources = {
            new Resource("pl/jacadev/jce/agent/res/agent.fxml", Controller.class.getResourceAsStream("agent.fxml")),
            new Resource("pl/jacadev/jce/agent/res/icons/classIcon.png", Controller.class.getResourceAsStream("icons/classIcon.png")),
            new Resource("pl/jacadev/jce/agent/res/icons/packIcon.png", Controller.class.getResourceAsStream("icons/packIcon.png")),
            new Resource("pl/jacadev/jce/agent/res/icons/fieldIcon.png", Controller.class.getResourceAsStream("icons/fieldIcon.png")),
            new Resource("pl/jacadev/jce/agent/res/icons/methodIcon.png", Controller.class.getResourceAsStream("icons/methodIcon.png")),
            new Resource("pl/jacadev/jce/agent/res/icons/annotationIcon.png", Controller.class.getResourceAsStream("icons/annotationIcon.png")),
            new Resource("pl/jacadev/jce/agent/res/icons/enumIcon.png", Controller.class.getResourceAsStream("icons/enumIcon.png")),
            new Resource("pl/jacadev/jce/agent/res/icons/objectIcon.png", Controller.class.getResourceAsStream("icons/objectIcon.png")),
            new Resource("pl/jacadev/jce/agent/res/icons/interfaceIcon.png", Controller.class.getResourceAsStream("icons/interfaceIcon.png")),
            new Resource("pl/jacadev/jce/agent/res/icons/abstractIcon.png", Controller.class.getResourceAsStream("icons/abstractIcon.png")),
    };
    public static void attachTo(String pid){
        Thread agent = new Thread(() -> {
            try {
                AttachUtil.makeUnattachable(pid);
                AttachUtil.attachAgentToJVM(pid, Agent.class, classes, resources);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        agent.setDaemon(true);
        agent.start();
    }
}
