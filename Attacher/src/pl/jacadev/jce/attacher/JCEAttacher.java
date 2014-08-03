package pl.jacadev.jce.attacher;

import java.io.File;
import java.net.URISyntaxException;

/**
 * @author JacaDev
 */
public class JCEAttacher {
    public static final File agent;
    static{
        File toAssign = null;
        try {
            toAssign = new File(pl.jacadev.jce.attacher.res.Controller.class.getResource("Agent.jar").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        agent = toAssign;
    }

    public static void attachTo(String pid){
        Thread agentThread = new Thread(() -> {
            try {
                AttachUtil.makeUnattachable(pid);
                AttachUtil.attachAgentToJVM(pid, agent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        agentThread.setDaemon(true);
        agentThread.start();
    }
}
