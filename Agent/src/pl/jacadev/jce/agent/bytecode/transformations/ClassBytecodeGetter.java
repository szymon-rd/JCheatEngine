package pl.jacadev.jce.agent.bytecode.transformations;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import pl.jacadev.jce.agent.utils.AUtil;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;

import static org.objectweb.asm.ClassReader.*;

/**
 * @author JacaDev
 */
public class ClassBytecodeGetter implements ClassFileTransformer {
    static final ClassBytecodeGetter BYTECODE_GETTER = new ClassBytecodeGetter();

    private ClassBytecodeGetter() {
    }

    private static final ThreadLocal<byte[]> classBytes = new ThreadLocal<>();

    public static byte[] getBytes(Class aClass) throws UnmodifiableClassException, ClassNotFoundException {
        MainTransformer.redefineWith(BYTECODE_GETTER, aClass);
        return classBytes.get();
    }

    private static final int READ_FLAGS = SKIP_FRAMES & SKIP_DEBUG;
    public static ClassNode toASMNode(Class<?> aClass) throws UnmodifiableClassException, ClassNotFoundException {
        ClassReader classReader = new ClassReader(getBytes(aClass));
        ClassNode classNode = new ClassNode(Opcodes.ASM5);
        classReader.accept(classNode, READ_FLAGS);
        return classNode;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        classBytes.set(classfileBuffer);
        return classfileBuffer;
    }

}
