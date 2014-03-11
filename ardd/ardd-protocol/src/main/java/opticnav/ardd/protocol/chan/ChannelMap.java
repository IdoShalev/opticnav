package opticnav.ardd.protocol.chan;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class ChannelMap {
    private Map<Integer, ChannelMultiplexee> channelMap;
    
    public ChannelMap() {
        this.channelMap = new HashMap<>();
    }
    
    public synchronized boolean isEmpty() {
        return this.channelMap.isEmpty();
    }
    
    public synchronized ChannelMultiplexee put(int channelID,
            ChannelMultiplexee cm) {
        return this.channelMap.put(channelID, cm);
    }
    
    public synchronized ChannelMultiplexee get(int channelID) {
        return this.channelMap.get(channelID);
    }
    
    public synchronized ChannelMultiplexee remove(int channelID)
            throws IOException {
        ChannelMultiplexee cm = this.channelMap.remove(channelID);
        cm.close();
        return cm;
    }
}