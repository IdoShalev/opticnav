package opticnav.ardd.clients;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import opticnav.ardd.protocol.PrimitiveReader;
import opticnav.ardd.protocol.PrimitiveWriter;

/**
 * AnnotatedCommandHandler makes implementing commands MUCH nicer. Each command is a method annotated
 * with the @Command annotation.
 * 
 * This is essentially an cleaner alternative to implementing long if/else chains.
 */
public class AnnotatedCommandHandler implements ClientCommandDispatcher.CommandHandler {
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Command {
        int value();
    }
    
    private final Map<Integer, Method> commandMap;
    
    public AnnotatedCommandHandler(Class<? extends AnnotatedCommandHandler> clazz) {
        this.commandMap = new HashMap<>();
        
        for (Method method: clazz.getMethods()) {
            Command command = method.getAnnotation(Command.class);
            if (command != null) {
                if (this.commandMap.put(command.value(), method) != null) {
                    throw new IllegalStateException("Two commands with the same code were declared in " + clazz);
                }
            }
        }
    }

    @Override
    public void command(int code, PrimitiveReader in, PrimitiveWriter out)
            throws IOException, InterruptedException {
        final Method method = this.commandMap.get(code);
        if (method != null) {
            try {
                method.invoke(this, in, out);
            } catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException e) {
                // XXX - fix
                throw new IOException(e);
            }
        } else {
            throw new IOException("No command of the code was found: " + code);
        }
    }
}
