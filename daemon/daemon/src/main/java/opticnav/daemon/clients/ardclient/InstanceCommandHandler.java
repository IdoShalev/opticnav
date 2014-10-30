package opticnav.daemon.clients.ardclient;

import java.io.IOException;

import opticnav.daemon.ARDConnection;
import opticnav.daemon.BlockingValue;
import opticnav.daemon.clients.AnnotatedCommandHandler;
import opticnav.daemon.instance.Entity;
import opticnav.daemon.protocol.GeoCoordFine;
import opticnav.daemon.protocol.PrimitiveReader;
import opticnav.daemon.protocol.PrimitiveWriter;
import opticnav.daemon.protocol.consts.ARDdARDProtocol.Connected.Instance.Commands;

/**
 * Handles all commands coming in from the ARDClient "Instance" channel.
 * 
 * @author Danny Spencer
 *
 */
public class InstanceCommandHandler extends AnnotatedCommandHandler {
    private final BlockingValue<Entity> entity;

    public InstanceCommandHandler(ARDConnection connection, BlockingValue<Entity> entity) {
        super(InstanceCommandHandler.class);
        this.entity = entity;
    }

    @Override
    public void close() throws IOException {
        try {
            entity.get().close();
        } catch (InterruptedException e) {
            throw new IOException(e);
        }
    }   
    
    @Command(Commands.MOVE)
    public void move(PrimitiveReader in, PrimitiveWriter out) throws Exception {
        final int lng = in.readSInt32();
        final int lat = in.readSInt32();
        final GeoCoordFine geoCoord = new GeoCoordFine(lng, lat);
        
        entity.get().move(geoCoord);
    }
}
