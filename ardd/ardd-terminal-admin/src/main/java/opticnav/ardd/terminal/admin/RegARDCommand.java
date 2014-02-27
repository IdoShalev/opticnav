package opticnav.ardd.terminal.admin;

import java.io.PrintWriter;

import opticnav.ardd.admin.AdminConnection;
import opticnav.ardd.admin.AdminConnectionException;
import opticnav.ardd.protocol.HexCode;
import opticnav.ardd.protocol.Protocol;
import opticnav.ardd.terminal.shared.Command;

public class RegARDCommand implements Command<AdminConnection> {
    @Override
    public void execute(AdminConnection conn, PrintWriter out, String[] args)
            throws AdminConnectionException {
        if (args.length == 1) {
            String confcodeString = args[0];
            
            if (HexCode.isStringCodeValid(confcodeString, Protocol.AdminClient.CONFCODE_BYTES)) {
                HexCode confcode = new HexCode(args[0]);
                
                int ard_id = conn.registerARDWithConfCode(confcode);
                boolean registered = ard_id != 0;
                
                if (registered) {
                    out.println("ARD ID: " + ard_id);
                } else {
                    out.println("Could not register ARD");
                }
            } else {
                out.println("Confcode is invalid");
            }
        } else {
            out.println("Must have 1 argument (confcode)");
        }
    }
}
