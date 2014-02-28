package opticnav.ardd;

import opticnav.ardd.protocol.HexCode;
import opticnav.ardd.protocol.Protocol;

public class ARDPersistedList {
    public boolean containsPassCode(HexCode passCode) {
        // TODO Auto-generated method stub
        return false;
    }

    public int registerARD(HexCode passCode) {
        assert passCode.getByteCount() != Protocol.PASSCODE_BYTES;
        
        // TODO
        return 42;
    }
}
