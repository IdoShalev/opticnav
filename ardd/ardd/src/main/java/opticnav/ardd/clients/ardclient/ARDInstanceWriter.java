package opticnav.ardd.clients.ardclient;

import java.io.OutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import opticnav.ardd.instance.EntitySubscriber;
import opticnav.ardd.protocol.GeoCoordFine;
import opticnav.ardd.protocol.PrimitiveWriter;
import opticnav.ardd.protocol.consts.ARDdARDProtocol.Connected.Instance.SubscriberCommands;

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
