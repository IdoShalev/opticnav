package opticnav.ardd.broker.ard;

import java.io.EOFException;
import java.io.InputStream;
import java.util.concurrent.Callable;

import opticnav.ardd.ard.ARDInstanceSubscriber;
import opticnav.ardd.protocol.GeoCoordFine;
import opticnav.ardd.protocol.PrimitiveReader;
import opticnav.ardd.protocol.consts.ARDdARDProtocol.Connected.Instance.SubscriberCommands;

public class ARDInstanceSubscriberListener implements Callable<Void> {
    private final PrimitiveReader input;
    private final ARDInstanceSubscriber subscriber;

    public ARDInstanceSubscriberListener(InputStream input, ARDInstanceSubscriber subscriber) {
        this.input = new PrimitiveReader(input);
        this.subscriber = subscriber;
    }
    
    @Override
    public Void call() throws Exception {
        try {
        while (!Thread.currentThread().isInterrupted()) {
            final int commandCode = this.input.readUInt8();
            
            // TODO - replace with constants
            if (commandCode == SubscriberCommands.CREATE_MARKER) {
                final int id = this.input.readUInt31();
                final String name = this.input.readString();
                final int lng = this.input.readSInt32();
                final int lat = this.input.readSInt32();
                
                this.subscriber.markerCreate(id, name, new GeoCoordFine(lng, lat));
            } else if (commandCode == SubscriberCommands.MOVE_MARKER) {
                final int id = this.input.readUInt31();
                final int lng = this.input.readSInt32();
                final int lat = this.input.readSInt32();
                
                this.subscriber.markerMove(id, new GeoCoordFine(lng, lat));
            } else if (commandCode == SubscriberCommands.REMOVE_MARKER) {
                final int id = this.input.readUInt31();
                
                this.subscriber.markerRemove(id);
            } else {
                throw new IllegalStateException();
            }
        }
        } catch (EOFException e) {
            // Ignore EOF
        }
        return null;
    }
}
