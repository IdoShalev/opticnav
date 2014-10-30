package opticnav.daemon;

import java.util.HashMap;
import java.util.Map;

import opticnav.daemon.protocol.ConfCode;
import opticnav.daemon.protocol.PassCode;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.math3.util.Pair;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 * The ARDPendingList class contains a list of all devices being pended for registration.
 * 
 * All devices in this list were sent a confirmation code, which is intended to later be inputted by a user of the
 * system to take the device out of this list and into {@link ARDPersistedList}.
 * 
 * @author Danny Spencer
 *
 */
public class ARDPendingList {
    private static final XLogger logger = XLoggerFactory.getXLogger(ARDPendingList.class);
    
    private BidiMap<ConfCode, PassCode> confcode_passcode;
    private Map<PassCode, BlockingValue<Integer>> passcode_resultvalue;
    
    /**
     * Construct the pending list. When constructed, there are no pending ARDs.
     */
    public ARDPendingList() {
        this.confcode_passcode = new DualHashBidiMap<>();
        this.passcode_resultvalue = new HashMap<>();
    }
    
    /**
     * Take the ARD identified by a confCode out of the list if it exists.
     * This is used by {@link ARDListsManager} for the registration process.
     * 
     * @param confCode The confirmation code identifying the ARD
     * @return A passCode and BlockingValue pair. The BlockingValue.get() method in the pair will block until the
     *         device is registered, or if there was an error registering.
     */
    public Pair<PassCode, BlockingValue<Integer>>
    getPasscodeWaitAndRemoveByConfcode(ConfCode confCode) {
        PassCode passCode = this.confcode_passcode.remove(confCode);
        
        if (passCode != null) {
            logger.debug("Removed confCode from pending: " + confCode);
            
            BlockingValue<Integer> result;
            result = passcode_resultvalue.remove(passCode);
            assert result != null;
            
            return new Pair<>(passCode, result);
        } else {
            return null;
        }
    }

    /**
     * Check if the specified confirmation code is in the pending list.
     * 
     * @param confCode A confirmation code identifying a device
     * @return True if the confirmation code is in the list, false if not
     */
    public boolean containsConfCode(ConfCode confCode) {
        return this.confcode_passcode.containsKey(confCode);
    }

    /**
     * Check if the specified passCode is in the pending list.
     * 
     * @param passCode A passCode identifying a device
     * @return True if the passCode is in the list, false if not
     */
    public boolean containsPassCode(PassCode passCode) {
        return this.confcode_passcode.containsValue(passCode);
    }

    /**
     * Add a passCode/confCode pair and a blocking value to set when the device is registered to the pending list.
     * This is called when a device connects to the daemon via {@link opticnav.daemon.net.ARDListener}.
     * 
     * @param p The passCode/confCode pair, and the corresponding blocking value to set when the device is registered.
     */
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
