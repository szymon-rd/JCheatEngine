package pl.jacadev.jce.agent.bytecode;

import com.sun.istack.internal.NotNull;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import pl.jacadev.jce.agent.Agent;
import pl.jacadev.jce.agent.utils.AUtil;
import pl.jacadev.jce.agent.utils.SystemClassLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author JacaDev
 */
public class OuterClassesLoader {
    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 8);

    private static class ZipEntryCallableLoader implements Callable<Boolean>{
        private final ZipEntry zipEntry;
        private final ZipFile file;
        private ZipEntryCallableLoader(ZipEntry zipEntry, ZipFile file) {
            this.zipEntry = zipEntry;
            this.file = file;
        }

        @Override
        public Boolean call() {
            try {
                loadClass(AUtil.toBytes(file.getInputStream(zipEntry)));
            } catch (IOException | ReflectiveOperationException e) {
                return false;
            }
            return true;
        }
    }

    public static void loadClass(@NotNull File file) throws IOException {
        Objects.requireNonNull(file);
        if(!file.getName().endsWith(".class"))
            throw new IllegalArgumentException(".class files are only allowed");
        try {
            loadClass(Files.readAllBytes(file.toPath()));
        } catch (ReflectiveOperationException e) {
            Agent.showError("Something went wrong while loading class " + file.getName().replace(".class", "") + ". Check code.");
        }
    }
    public static void loadClasses(@NotNull File file) throws IOException {
        Objects.requireNonNull(file);
        if(!file.isDirectory())
            throw new IllegalArgumentException("directories are only allowed");
        for(File f : file.listFiles())
            if(f.isDirectory()) loadClasses(f);
            else loadClass(f);
    }
    public static void loadClasses(@NotNull ZipFile zipFile) throws IOException {
        Objects.requireNonNull(zipFile);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        Set<ZipEntryCallableLoader> callableLoaders = new HashSet<>();
        while(entries.hasMoreElements()){
            ZipEntry entry = entries.nextElement();
            if(!entry.isDirectory() && entry.getName().endsWith(".class")){
                callableLoaders.add(
                        new ZipEntryCallableLoader(entry, zipFile)
                );
            }
        }
        try {
            for(Future<Boolean> future :
                    EXECUTOR.invokeAll(callableLoaders)) if(!future.get()){
                Agent.showError("Something went wrong while loading classes");
                throw new Error("Something went wrong while loading classes");
            }
        } catch (InterruptedException | ExecutionException e) {
            Agent.handleException(e);
        }
    }

    private static void loadClass(byte[] bytes) throws ReflectiveOperationException {
        ClassReader reader = new ClassReader(bytes);
        ClassNode node = new ClassNode();
        reader.accept(node, 0);
        SystemClassLoader.defineClass(node.name.replace("/", "."), bytes);
    }
}
