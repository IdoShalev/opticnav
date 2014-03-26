package opticnav.ardd;

import java.util.HashMap;
import java.util.Map;

import opticnav.ardd.protocol.ConfCode;
import opticnav.ardd.protocol.PassCode;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.math3.util.Pair;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

public class ARDPendingList {
    private static final XLogger logger = XLoggerFactory.getXLogger(ARDPendingList.class);
    
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
            logger.debug("Removed confCode from pending: " + confcode);
            
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

    public void addPassConfCodes(Pair<Pair<PassCode, ConfCode>, BlockingValue<Integer>> p) {
        logger.debug("Added passConfCodes: "
                    + p.getFirst().getFirst() + ", "
                    + p.getFirst().getSecond());
        
        final Pair<PassCode, ConfCode> codes = p.getFirst();
        final BlockingValue<Integer> resultValue = p.getSecond();
        final PassCode passCode = codes.getFirst();
        final ConfCode confCode = codes.getSecond();
        
        this.confcode_passcode.put(confCode, passCode);
        this.passcode_resultvalue.put(passCode, resultValue);
    }
}
