package pl.jacadev.jce.agent.bytecode;

import pl.jacadev.jce.agent.Agent;
import pl.jacadev.jce.agent.bytecode.transformations.ClassBytecodeSetter;
import pl.jacadev.jce.agent.utils.AUtil;

import java.lang.instrument.UnmodifiableClassException;
import java.nio.file.Files;

/**
 * @author JacaDev
 */
public class CodeExecutor {
    private static final String RUNNABLE_CODE;
    static{
        RUNNABLE_CODE = AUtil.toBytes(CodeExecutor.class.getResourceAsStream("Runnable.src"))
    }

    public static void run(String code){
        //TODO compiling
        byte[] bytes /* = compiled */ = null;
        try {
            ClassBytecodeSetter.setBytes(RunnableCode.class, bytes);
        } catch (UnmodifiableClassException | ClassNotFoundException e) {
            Agent.handleException(e);
        }
    }
}
