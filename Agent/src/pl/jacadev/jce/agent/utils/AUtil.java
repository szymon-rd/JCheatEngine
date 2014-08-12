package pl.jacadev.jce.agent.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;

/**
 * @author JacaDev
 */
public class AUtil {
    /**
     * @return pid of current jvm.
     */
    public static String getCurrentPid() {
        String vm = ManagementFactory.getRuntimeMXBean().getName();
        return vm.substring(0, vm.indexOf('@'));
    }

    public static byte[] getBytesFromClass(Class<?> clazz) {
        return toBytes(clazz.getClassLoader().getResourceAsStream(clazz.getName().replace('.', '/') + ".class"));
    }

    public static byte[] toBytes(InputStream stream) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            int nRead;
            byte[] data = new byte[16384];
            while ((nRead = stream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toByteArray();
    }
}
