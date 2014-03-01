package opticnav.ardd.terminal.ard;

import java.io.PrintWriter;

import opticnav.ardd.ard.ARDConnection;
import opticnav.ardd.broker.ard.ARDBroker;
import opticnav.ardd.protocol.PassCode;
import opticnav.ardd.protocol.ConfCode;
import opticnav.ardd.terminal.shared.Command;

public class ReqCodesCommand implements Command<ARDBroker> {
    @Override
    public void execute(ARDBroker conn, final PrintWriter out, String[] args)
            throws Exception {
        ARDConnection.RequestPassConfCodesCallback cb = new ARDConnection.RequestPassConfCodesCallback() {
            @Override
            public void passConfCodes(PassCode passCode, ConfCode confCode) {
                out.println("Passcode: " + passCode);
                out.println("Confcode: " + confCode);
            }
        };
        
        int result = conn.requestPassConfCodes(cb);
        out.println("Result: " + result);
        // TODO - interpret result
    }
}
