package opticnav.ardd;

import java.util.HashMap;
import java.util.Map;

import opticnav.ardd.protocol.HexCode;
import opticnav.ardd.protocol.Protocol;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.math3.util.Pair;

public class ARDPendingList {
    private BidiMap<HexCode, HexCode> confcode_passcode;
    private Map<HexCode, BlockingValue<Integer>> passcode_resultvalue;
    
    public ARDPendingList() {
        this.confcode_passcode = new DualHashBidiMap<>();
        this.passcode_resultvalue = new HashMap<>();
    }
    
    public Pair<HexCode, BlockingValue<Integer>>
    getPasscodeWaitAndRemoveByConfcode(HexCode confcode) {
        if (confcode.getByteCount() != Protocol.CONFCODE_BYTES) {
            throw new IllegalStateException();
        }
        
        HexCode passCode = this.confcode_passcode.remove(confcode);
        
        if (passCode != null) {
            BlockingValue<Integer> result;
            result = passcode_resultvalue.remove(passCode);
            assert result != null;
            
            return new Pair<>(passCode, result);
        } else {
            return null;
        }
    }

    public boolean containsConfCode(HexCode confCode) {
        return this.confcode_passcode.containsKey(confCode);
    }

    public boolean containsPassCode(HexCode passCode) {
        return this.confcode_passcode.containsValue(passCode);
    }

    public void addPassConfCodes(Pair<PassConfCodes, BlockingValue<Integer>> p) {
        PassConfCodes codes = p.getFirst();
        
        this.confcode_passcode.put(codes.getConfcode(), codes.getPasscode());
        this.passcode_resultvalue.put(codes.getPasscode(), p.getSecond());
    }
}
