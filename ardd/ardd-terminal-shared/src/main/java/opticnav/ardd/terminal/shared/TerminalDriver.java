package opticnav.ardd.terminal.shared;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

public class TerminalDriver<E> implements Runnable {
    private Scanner in;
    private PrintWriter out;
    private Map<String, Command<E>> commandMap;
    private E connection;

    public TerminalDriver(Scanner in, PrintWriter out,
            Map<String, Command<E>> commandMap, E connection) {
        this.in = in;
        this.out = out;
        this.commandMap = commandMap;
        this.connection = connection;
    }
    
    @Override
    public void run() {
        try {
            boolean isRunning;
            do {
                isRunning = runC();
            } while (isRunning);
        } catch (Exception e) {
            // TODO - log this
            // maybe let exception bubble up the stack (not use Runnable)
            e.printStackTrace();
        }
    }

    private boolean runC() throws Exception {
        // TODO - better parsing
        out.print("> ");
        out.flush();
        String line = in.nextLine();
        String[] lineArgs = line.split("\\s+");
        
        if (lineArgs.length > 0) {
            if ("exit".equals(lineArgs[0]) || "q".equals(lineArgs[0])) {
                // exit command
                return false;
            } else {
                Command<E> cmd = commandMap.get(lineArgs[0]);
                if (cmd != null) {
                    // command found - execute it
                    
                    // pass arguments (excluding command name)
                    String[] a;
                    a = Arrays.copyOfRange(lineArgs, 1, lineArgs.length);
                    cmd.execute(connection, out, a);
                } else {
                    out.println(lineArgs[0] + ": command not found...");
                }
                return true;
            }
        } else {
            return true;
        }
    }
}
