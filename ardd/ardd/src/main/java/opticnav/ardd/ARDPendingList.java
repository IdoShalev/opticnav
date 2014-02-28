package opticnav.ardd;

import opticnav.ardd.protocol.HexCode;
import opticnav.ardd.protocol.Protocol;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

public class ARDPendingList {
    private BidiMap<HexCode, HexCode> confcode_passcode;
    
    public ARDPendingList() {
        this.confcode_passcode = new DualHashBidiMap<>();
    }
    
    public HexCode getPasscodeAndRemoveFromConfcode(HexCode confcode) {
        if (confcode.getByteCount() != Protocol.CONFCODE_BYTES) {
            throw new IllegalStateException();
        }
        
        return this.confcode_passcode.remove(confcode);
    }

    public boolean containsConfCode(HexCode confCode) {
        return this.confcode_passcode.containsKey(confCode);
    }

    public boolean containsPassCode(HexCode passCode) {
        return this.confcode_passcode.containsValue(passCode);
    }

    public void addPassConfCodes(PassConfCodes codes) {
        this.confcode_passcode.put(codes.getConfcode(), codes.getPasscode());
    }
}
