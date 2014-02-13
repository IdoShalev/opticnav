package opticnav.ardd.terminal.admin;

import java.io.PrintWriter;

import opticnav.ardd.admin.AdminConnection;
import opticnav.ardd.admin.AdminConnectionException;

public interface Command {
    public void execute(AdminConnection conn, PrintWriter out, String[] args)
            throws AdminConnectionException;
}
