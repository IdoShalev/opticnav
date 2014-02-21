package opticnav.ardd.terminal.admin;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import opticnav.ardd.admin.AdminConnectionException;
import opticnav.ardd.broker.admin.AdminBroker;
import opticnav.ardd.protocol.Protocol;

public class AdminTerminalDriver {
    private static boolean run(AdminBroker conn, Scanner in, PrintWriter out,
            Map<String, Command> commandMap) throws AdminConnectionException {
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
                Command cmd = commandMap.get(lineArgs[0]);
                if (cmd != null) {
                    // command found - execute it
                    
                    // pass arguments (excluding command name)
                    String[] a;
                    a = Arrays.copyOfRange(lineArgs, 1, lineArgs.length);
                    cmd.execute(conn, out, a);
                } else {
                    out.println(lineArgs[0] + ": command not found...");
                }
                return true;
            }
        } else {
            return true;
        }
    }

    public static void main(String[] args) throws Exception {
        String host = "localhost";
        int port = Protocol.DEFAULT_ADMIN_PORT;
        Map<String, Command> commandMap = new HashMap<>();
        
        commandMap.put(Protocol.AdminClient.Commands.REGARD.getCommand(),
                new RegARDCommand());
        
        Scanner in = new Scanner(System.in);
        PrintWriter out = new PrintWriter(System.out, true);
        
        Socket socket = new Socket(host, port);
        try (AdminBroker conn = AdminBroker.fromSocket(socket)) {
            boolean isRunning;
            do {
                isRunning = run(conn, in, out, commandMap);
            } while (isRunning);
        }
    }

}
