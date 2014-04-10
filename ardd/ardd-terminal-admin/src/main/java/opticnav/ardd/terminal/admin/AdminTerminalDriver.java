package opticnav.ardd.terminal.admin;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import opticnav.ardd.admin.ARDdAdmin;
import opticnav.ardd.admin.ARDdAdminBroker;
import opticnav.ardd.protocol.Protocol;
import opticnav.ardd.protocol.consts.ARDdAdminProtocol.Commands;
import opticnav.ardd.protocol.chan.ChannelUtil;
import opticnav.ardd.terminal.shared.Command;
import opticnav.ardd.terminal.shared.TerminalDriver;

public class AdminTerminalDriver {
    public enum CommandsText {
        REGARD(Commands.REGARD, "regard");
        
        private int code;
        private String command;

        private CommandsText(int code, String command) {
            this.code = code;
            this.command = command;
        }
        
        public int getCode() {
            return this.code;
        }

        public String getCommand() {
            return this.command;
        }
    }
    
    public static void main(String[] args) throws Exception {
        String host = "localhost";
        int port = Protocol.DEFAULT_ADMIN_PORT;
        Map<String, Command<ARDdAdmin>> commandMap = new HashMap<>();
        
        commandMap.put(CommandsText.REGARD.getCommand(), new RegARDCommand());
        
        Scanner in = new Scanner(System.in);
        PrintWriter out = new PrintWriter(System.out, true);
        Socket socket = new Socket(host, port);
        
        try (ARDdAdminBroker conn = new ARDdAdminBroker(ChannelUtil.fromSocket(socket))) {
            TerminalDriver<ARDdAdmin> driver;
            driver = new TerminalDriver<>(in, out, commandMap, conn);
            driver.run();
        }
    }
}
