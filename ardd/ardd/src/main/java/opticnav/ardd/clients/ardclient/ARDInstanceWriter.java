package opticnav.ardd.clients.ardclient;

import java.io.OutputStream;

import opticnav.ardd.instance.EntitySubscriber;
import opticnav.ardd.instance.GeoCoordFine;
import opticnav.ardd.protocol.PrimitiveWriter;

public class ARDInstanceWriter implements EntitySubscriber {
    private final PrimitiveWriter output;

    public ARDInstanceWriter(OutputStream outputStream) {
        this.output = new PrimitiveWriter(outputStream);
    }

    @Override
    public void newMarker(int markerID, String name, GeoCoordFine geoCoord) {
        // TODO Another thread and PROPER exception handling, goddammit
    }

    @Override
    public void moveMarker(int markerID, GeoCoordFine geoCoord) {
        // TODO Another thread and PROPER exception handling, goddammit
    }

    @Override
    public void removeMarker(int markerID) {
        // TODO Another thread and PROPER exception handling, goddammit
    }
}
