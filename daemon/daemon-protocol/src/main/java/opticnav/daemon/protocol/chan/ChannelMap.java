package opticnav.daemon.protocol.chan;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 * The ChannelMap class lists all open channels by their ID and {@link ChannelMultiplexee} object.
 * If a channel is open, it's listed here.
 * 
 * @author Danny Spencer
 *
 */
class ChannelMap {
    private static final XLogger LOG = XLoggerFactory.getXLogger(ChannelMap.class);
    
    private Map<Integer, ChannelMultiplexee> channelMap;
    
    public ChannelMap() {
        this.channelMap = new HashMap<>();
    }
    
    /**
     * 
     * @return True if there are no open channels, false if there is at least 1 open channel
     */
    public synchronized boolean isEmpty() {
        return this.channelMap.isEmpty();
    }
    
    /**
     * Add a {@link ChannelMultiplexee} object and its associated ID to the list
     * 
     * @param channelID The channel ID
     * @param cm The channel multiplexee object
     * @return The same channel multiplexee object, as provided
     */
    public synchronized ChannelMultiplexee put(int channelID,
            ChannelMultiplexee cm) {
        if (cm == null) {
            throw new NullPointerException("Channel multiplexee cannot be null: " + channelID);
        }
        LOG.debug("Add channel multiplexee: " + channelID);
        return this.channelMap.put(channelID, cm);
    }
    
    /**
     * Get a channel multiplexee identified by a channel ID
     * 
     * @param channelID The channel ID of the channel multiplexee being requested
     * @return A ChannelMultiplexee object
     */
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
    public synchronized void remove(int channelID)
            throws IOException {
        LOG.debug("Remove channel multiplexee: " + channelID);
        ChannelMultiplexee cm = this.channelMap.remove(channelID);
        if (cm != null) {
            cm.close();
        }
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