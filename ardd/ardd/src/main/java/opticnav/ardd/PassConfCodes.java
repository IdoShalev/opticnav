package opticnav.ardd;

import opticnav.ardd.protocol.HexCode;
import opticnav.ardd.protocol.Protocol;

public class PassConfCodes {
    private HexCode passcode, confcode;

    public PassConfCodes(HexCode passcode, HexCode confcode) {
        if (passcode.getByteCount() != Protocol.PASSCODE_BYTES) {
            throw new IllegalStateException();
        }
        if (confcode.getByteCount() != Protocol.CONFCODE_BYTES) {
            throw new IllegalStateException();
        }
        
        this.passcode = passcode;
        this.confcode = confcode;
    }

    public HexCode getPasscode() {
        return passcode;
    }

    public HexCode getConfcode() {
        return confcode;
    }
}
