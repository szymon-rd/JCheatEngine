package pl.jacadev.jce.agent.utils;

import java.lang.management.ManagementFactory;

/**
 * @author JacaDev
 */
public class AUtil {
    /**
     * @return pid of this jvm.
     */
    public static String getCurrentPid() {
        String vm = ManagementFactory.getRuntimeMXBean().getName();
        return vm.substring(0, vm.indexOf('@'));
    }
}
