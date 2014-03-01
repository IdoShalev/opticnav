package opticnav.ardd;

import opticnav.ardd.protocol.ConfCode;
import opticnav.ardd.protocol.PassCode;

public class PassConfCodes {
    private PassCode passcode;
    private ConfCode confcode;

    public PassConfCodes(PassCode passcode, ConfCode confcode) {
        this.passcode = passcode;
        this.confcode = confcode;
    }

    public PassCode getPasscode() {
        return passcode;
    }

    public ConfCode getConfcode() {
        return confcode;
    }
}
