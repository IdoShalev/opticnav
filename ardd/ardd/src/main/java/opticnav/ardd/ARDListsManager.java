package opticnav.ardd;

import opticnav.ardd.protocol.HexCode;
import opticnav.ardd.protocol.Protocol;

import org.apache.commons.math3.random.RandomGenerator;

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
    
    public PassConfCodesWait generatePassConfCodes() {
        byte[] confCodeBytes, passCodeBytes;
        HexCode confCode, passCode;
        PassConfCodes codes;
        
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
            codes = new PassConfCodes(passCode, confCode);
            this.pending.addPassConfCodes(codes);
        }
        
        return new PassConfCodesWait(codes);
    }
    
    public int persistPendingWithConfCode(HexCode confCode) {
        if (confCode.getByteCount() != Protocol.CONFCODE_BYTES) {
            throw new IllegalStateException();
        }
        
        synchronized (this.pending) {
            HexCode passCode;
            int ardID;
            
            passCode = this.pending.getPasscodeAndRemoveFromConfcode(confCode);
            
            if (passCode == null) {
                // could not find an entry with confcode
                return Protocol.ARDClient.NO_ARD;
            }
            
            synchronized (this.persisted) {
                ardID = this.persisted.registerARD(passCode);
            }
            
            return ardID;
        }
    }
}
