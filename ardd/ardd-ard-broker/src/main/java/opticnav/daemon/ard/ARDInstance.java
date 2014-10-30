package opticnav.daemon.ard;

import java.io.Closeable;
import java.io.IOException;

import opticnav.daemon.protocol.GeoCoordFine;
/**
 * An ARD broker connection in the Instance state.
 * 
 * @author Danny Spencer
 *
 */
public interface ARDInstance extends Closeable {
    public InstanceMap getMap();
    /**
     * Provide a subscriber interface that gets notified when the broker receives marker changes.
     * 
     * @param subscriber
     */
    public void setSubscriber(ARDInstanceSubscriber subscriber);
    
    /**
     * Notify the instance on ARDd that the device has moved.
     * 
     * @param geoCoord The new location
     * @throws ARDInstanceException
     */
    public void move(GeoCoordFine geoCoord) throws ARDInstanceException;
    public void close() throws IOException;
}
