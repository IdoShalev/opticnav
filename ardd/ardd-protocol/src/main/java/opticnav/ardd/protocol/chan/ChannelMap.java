package opticnav.ardd.protocol.chan;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

class ChannelMap {
    private static final XLogger LOG = XLoggerFactory.getXLogger(ChannelMap.class);
    
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
    
    /**
     * Remove a channel multiplexee, thus closing its channel.
     * 
     * @param channelID The channel ID to be removed
     * @return The removed multiplexee
     * @throws IOException Thrown when there's a problem closing the channel
     */
    public synchronized ChannelMultiplexee remove(int channelID)
            throws IOException {
        LOG.debug("Remove channel multiplexee: " + channelID);
        ChannelMultiplexee cm = this.channelMap.remove(channelID);
        if (cm != null) {
            cm.close();
        }
        return cm;
    }
    
    /**
     * Remove all channel multiplexees, thus closing all their channels.
     * This is called when the main multiplexer channel is closed, for instance.
     */
    public synchronized void removeAll() {
        LOG.debug("Remove all channel multiplexees: " + this.channelMap.keySet());
        
        for (ChannelMultiplexee cm: this.channelMap.values()) {
            try {
                cm.close();
            } catch (IOException e) {
                LOG.catching(e);
            }
        }
        this.channelMap.clear();
    }
}