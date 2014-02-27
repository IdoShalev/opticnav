package opticnav.ardd.terminal.ard;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import opticnav.ardd.broker.ard.ARDBroker;
import opticnav.ardd.protocol.Protocol;
import opticnav.ardd.terminal.shared.Command;
import opticnav.ardd.terminal.shared.TerminalDriver;

public class ARDTerminalDriver {
    public static void main(String[] args) throws Exception {
        String host = "localhost";
        int port = Protocol.DEFAULT_ARD_PORT;
        Map<String, Command<ARDBroker>> commandMap = new HashMap<>();
        
        Scanner in = new Scanner(System.in);
        PrintWriter out = new PrintWriter(System.out, true);
        Socket socket = new Socket(host, port);
        
        try (ARDBroker conn = ARDBroker.fromSocket(socket)) {
            TerminalDriver<ARDBroker> driver;
            driver = new TerminalDriver<>(in, out, commandMap, conn);
            driver.run();
        }
    }
}
