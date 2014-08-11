package pl.jacadev.jce.agent.bytecode.transformations;

import pl.jacadev.jce.agent.Agent;
import pl.jacadev.jce.agent.utils.AUtil;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.UnmodifiableClassException;
import java.security.ProtectionDomain;

/**
 * @author JacaDev
 */
public class MainTransformer implements ClassFileTransformer {
    public static final MainTransformer MAIN_TRANSFORMER = new MainTransformer();
    static final ClassFileTransformer DEFAULT_TRANSFORMER =
            (loader, className, classBeingRedefined, protectionDomain, classfileBuffer) -> classfileBuffer;

    public static void redefineWith(ClassFileTransformer transformer, Class<?> aClass)
            throws UnmodifiableClassException, ClassNotFoundException {
        MAIN_TRANSFORMER.transformer.set(transformer);
        Agent.INSTRUMENTATION.redefineClasses(new ClassDefinition(aClass, AUtil.getBytesFromClass(aClass)));
    }

    public static void restoreToDefault() {
        MAIN_TRANSFORMER.transformer.set(DEFAULT_TRANSFORMER);
    }

    private final ThreadLocal<ClassFileTransformer> transformer = new ThreadLocal<ClassFileTransformer>() {
        @Override
        protected ClassFileTransformer initialValue() {
            return DEFAULT_TRANSFORMER;
        }
    };

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        return transformer.get().transform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
    }

    private MainTransformer() {
    }
}
