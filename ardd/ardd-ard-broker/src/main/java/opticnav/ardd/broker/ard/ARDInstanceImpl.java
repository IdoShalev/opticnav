package opticnav.ardd.broker.ard;

import java.io.IOException;

import opticnav.ardd.ard.ARDInstance;
import opticnav.ardd.ard.ARDInstanceException;
import opticnav.ardd.protocol.GeoCoordFine;
import opticnav.ardd.protocol.PrimitiveReader;
import opticnav.ardd.protocol.PrimitiveUtil;
import opticnav.ardd.protocol.PrimitiveWriter;
import opticnav.ardd.protocol.chan.Channel;

public class ARDInstanceImpl implements ARDInstance {
    private final PrimitiveReader input;
    private final PrimitiveWriter output;

    public ARDInstanceImpl(Channel channel) {
        this.input  = PrimitiveUtil.reader(channel);
        this.output = PrimitiveUtil.writer(channel);
    }

    @Override
    public void move(GeoCoordFine geoCoord) throws ARDInstanceException {
        // TODO Auto-generated method stub

    }

    @Override
    public void close() throws IOException {
        this.output.close();
    }
}
