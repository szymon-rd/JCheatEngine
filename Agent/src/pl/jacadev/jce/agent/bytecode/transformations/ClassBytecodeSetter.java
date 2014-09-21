package pl.jacadev.jce.agent.bytecode.transformations;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.UnmodifiableClassException;
import java.security.ProtectionDomain;

/**
 * @author JacaDev
 */
public class ClassBytecodeSetter implements ClassFileTransformer {
    static final ClassBytecodeSetter BYTECODE_SETTER = new ClassBytecodeSetter();

    private ClassBytecodeSetter() {
    }

    public static void setBytes(Class aClass, byte[] bytes) throws UnmodifiableClassException, ClassNotFoundException {
        synchronized (aClass) {
            toSet.set(bytes);
            MainTransformer.redefineWith(BYTECODE_SETTER, aClass);
        }
    }

    private static final ThreadLocal<byte[]> toSet = new ThreadLocal<>();

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        return toSet.get();
    }

}
