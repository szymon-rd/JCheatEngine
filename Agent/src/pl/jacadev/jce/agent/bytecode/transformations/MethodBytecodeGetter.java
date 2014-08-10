package pl.jacadev.jce.agent.bytecode.transformations;

import jdk.internal.org.objectweb.asm.ClassWriter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import pl.jacadev.jce.agent.utils.AUtil;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;

/**
 * @author JacaDev
 */
public class MethodBytecodeGetter implements ClassFileTransformer {
    static final MethodBytecodeGetter BYTECODE_GETTER = new MethodBytecodeGetter();
    private final ThreadLocal<byte[]> classBytes = new ThreadLocal<>();

    public static byte[] getBytes(Method m) throws UnmodifiableClassException, ClassNotFoundException {
        ClassReader reader = new ClassReader(AUtil.getBytesFromClass(m.getDeclaringClass()));
        ClassNode node = new ClassNode(Opcodes.ASM5);
        MainTransformer.redefineWith(BYTECODE_GETTER, m.getDeclaringClass());
        return new byte[0]; //TODO
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        return classfileBuffer;
    }
}
