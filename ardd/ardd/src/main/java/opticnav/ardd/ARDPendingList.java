package opticnav.ardd;

import java.util.HashMap;
import java.util.Map;

import opticnav.ardd.protocol.ConfCode;
import opticnav.ardd.protocol.PassCode;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.math3.util.Pair;

public class ARDPendingList {
    private BidiMap<ConfCode, PassCode> confcode_passcode;
    private Map<PassCode, BlockingValue<Integer>> passcode_resultvalue;
    
    public ARDPendingList() {
        this.confcode_passcode = new DualHashBidiMap<>();
        this.passcode_resultvalue = new HashMap<>();
    }
    
    public Pair<PassCode, BlockingValue<Integer>>
    getPasscodeWaitAndRemoveByConfcode(ConfCode confcode) {
        PassCode passCode = this.confcode_passcode.remove(confcode);
        
        if (passCode != null) {
            BlockingValue<Integer> result;
            result = passcode_resultvalue.remove(passCode);
            assert result != null;
            
            return new Pair<>(passCode, result);
        } else {
            return null;
        }
    }

    public boolean containsConfCode(ConfCode confCode) {
        return this.confcode_passcode.containsKey(confCode);
    }

    public boolean containsPassCode(PassCode passCode) {
        return this.confcode_passcode.containsValue(passCode);
    }

    public void addPassConfCodes(Pair<PassConfCodes, BlockingValue<Integer>> p) {
        PassConfCodes codes = p.getFirst();
        
        this.confcode_passcode.put(codes.getConfcode(), codes.getPasscode());
        this.passcode_resultvalue.put(codes.getPasscode(), p.getSecond());
    }
}
