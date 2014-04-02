package opticnav.ardd.ard;

import java.io.Closeable;
import java.io.IOException;

import opticnav.ardd.protocol.GeoCoordFine;

public interface ARDInstance extends Closeable {
    public void move(GeoCoordFine geoCoord) throws ARDInstanceException;
    
    public void close() throws IOException;
}
