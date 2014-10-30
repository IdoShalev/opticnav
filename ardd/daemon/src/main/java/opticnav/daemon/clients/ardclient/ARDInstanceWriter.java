package opticnav.daemon.clients.ardclient;

import java.io.OutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import opticnav.daemon.instance.EntitySubscriber;
import opticnav.daemon.protocol.GeoCoordFine;
import opticnav.daemon.protocol.PrimitiveWriter;
import opticnav.daemon.protocol.consts.ARDdARDProtocol.Connected.Instance.SubscriberCommands;

/**
 * Serializes subscriber events over an Output stream. This is used to write marker changes over the network.
 * 
 * @author Danny Spencer
 *
 */
public class ARDInstanceWriter implements EntitySubscriber, AutoCloseable {
    private final PrimitiveWriter output;
    private final ExecutorService executor;

    public ARDInstanceWriter(OutputStream outputStream) {
        this.output = new PrimitiveWriter(outputStream);
        this.executor = Executors.newSingleThreadExecutor();
    }

    @Override
    public void close() throws Exception {
        this.executor.shutdown();
    }

    @Override
    public void newMarker(final int markerID, final String name, final GeoCoordFine geoCoord) {
        this.executor.submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                output.writeUInt8(SubscriberCommands.CREATE_MARKER);
                output.writeUInt31(markerID);
                output.writeString(name);
                output.writeSInt32(geoCoord.getLongitudeInt());
                output.writeSInt32(geoCoord.getLatitudeInt());
                return null;
            }
        });
    }

    @Override
    public void moveMarker(final int markerID, final GeoCoordFine geoCoord) {
        this.executor.submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                output.writeUInt8(SubscriberCommands.MOVE_MARKER);
                output.writeUInt31(markerID);
                output.writeSInt32(geoCoord.getLongitudeInt());
                output.writeSInt32(geoCoord.getLatitudeInt());
                return null;
            }
        });
    }

    @Override
    public void removeMarker(final int markerID) {
        this.executor.submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                output.writeUInt8(SubscriberCommands.REMOVE_MARKER);
                output.writeUInt31(markerID);
                return null;
            }
        });
    }
}
