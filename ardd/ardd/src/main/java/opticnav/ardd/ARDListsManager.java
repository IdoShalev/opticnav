package opticnav.ardd;

import opticnav.ardd.protocol.HexCode;
import opticnav.ardd.protocol.Protocol;

import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.Pair;

public final class ARDListsManager {
    private final ARDPendingList pending;
    private final ARDPersistedList persisted;
    private final RandomGenerator randomGen;

    public ARDListsManager(ARDPendingList pending, ARDPersistedList persisted,
            RandomGenerator randomGen) {
        this.pending = pending;
        this.persisted = persisted;
        this.randomGen = randomGen;
    }
    
    public Pair<PassConfCodes, BlockingValue<Integer>> generatePassConfCodes() {
        byte[] confCodeBytes, passCodeBytes;
        HexCode confCode, passCode;
        
        confCodeBytes = new byte[Protocol.CONFCODE_BYTES];
        passCodeBytes = new byte[Protocol.PASSCODE_BYTES];
        
        synchronized (this.pending) {
            // generate confcode
            // confcode must not already exist in pending list
            do {
                this.randomGen.nextBytes(confCodeBytes);
                confCode = new HexCode(confCodeBytes);
            } while (this.pending.containsConfCode(confCode));

            // generate passcode
            // passcode must not already exist in persisted or pending list
            synchronized (this.persisted) {
                do {
                    this.randomGen.nextBytes(passCodeBytes);
                    passCode = new HexCode(passCodeBytes);
                } while (this.pending.containsPassCode(passCode) ||
                        this.persisted.containsPassCode(passCode));
            }
            
            // add to pending
            Pair<PassConfCodes, BlockingValue<Integer>> codes;
            BlockingValue<Integer> result = new BlockingValue<>();
            
            codes = new Pair<>(new PassConfCodes(passCode, confCode), result);
            this.pending.addPassConfCodes(codes);
            return codes;
        }
    }
    
    public int persistPendingWithConfCode(HexCode confcode) {
        if (confcode.getByteCount() != Protocol.CONFCODE_BYTES) {
            throw new IllegalStateException();
        }
        
        synchronized (this.pending) {
            Pair<HexCode, BlockingValue<Integer>> pw;
            int ardID;
            
            pw = this.pending.getPasscodeWaitAndRemoveByConfcode(confcode);
            
            if (pw == null) {
                // could not find an entry with confcode
                return Protocol.ARDClient.NO_ARD;
            }
            
            synchronized (this.persisted) {
                ardID = this.persisted.registerARD(pw.getFirst());
            }
            
            // TODO - replace 0 with constant variable
            pw.getSecond().set(0);
            
            return ardID;
        }
    }
}
