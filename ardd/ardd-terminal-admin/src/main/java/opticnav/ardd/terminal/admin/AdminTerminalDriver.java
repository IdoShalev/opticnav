package opticnav.ardd.terminal.admin;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import opticnav.ardd.admin.AdminConnection;
import opticnav.ardd.broker.admin.AdminBroker;
import opticnav.ardd.protocol.Protocol;
import opticnav.ardd.protocol.chan.ChannelUtil;
import opticnav.ardd.terminal.shared.Command;
import opticnav.ardd.terminal.shared.TerminalDriver;
import static opticnav.ardd.protocol.Protocol.AdminClient.Commands.*;

public class AdminTerminalDriver {
    public static void main(String[] args) throws Exception {
        String host = "localhost";
        int port = Protocol.DEFAULT_ADMIN_PORT;
        Map<String, Command<AdminConnection>> commandMap = new HashMap<>();
        
        commandMap.put(REGARD.getCommand(), new RegARDCommand());
        
        Scanner in = new Scanner(System.in);
        PrintWriter out = new PrintWriter(System.out, true);
        Socket socket = new Socket(host, port);
        
        try (AdminBroker conn = new AdminBroker(ChannelUtil.fromSocket(socket))) {
            TerminalDriver<AdminConnection> driver;
            driver = new TerminalDriver<>(in, out, commandMap, conn);
            driver.run();
        }
    }
}
