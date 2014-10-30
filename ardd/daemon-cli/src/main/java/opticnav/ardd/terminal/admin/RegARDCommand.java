package opticnav.ardd.terminal.admin;

import java.io.PrintWriter;

import opticnav.ardd.admin.ARDdAdmin;
import opticnav.ardd.admin.ARDdAdminException;
import opticnav.ardd.protocol.ConfCode;
import shared.Command;

public class RegARDCommand implements Command<ARDdAdmin> {
    @Override
    public void execute(ARDdAdmin conn, PrintWriter out, String[] args)
            throws ARDdAdminException {
        if (args.length == 1) {
            String confcodeString = args[0];
            
            if (ConfCode.isStringCodeValid(confcodeString)) {
                ConfCode confcode = new ConfCode(args[0]);
                
                int ard_id = conn.registerARD(confcode);
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
