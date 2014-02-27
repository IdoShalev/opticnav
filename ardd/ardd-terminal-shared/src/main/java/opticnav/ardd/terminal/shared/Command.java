package opticnav.ardd.terminal.shared;

import java.io.PrintWriter;

public interface Command<E> {
    public void execute(E connection, PrintWriter out, String[] args)
            throws Exception;
}
