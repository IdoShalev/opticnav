package opticnav.ardd.protocol;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import opticnav.ardd.protocol.chan.Channel;

public class PrimitiveUtil {
    public static PrimitiveReader reader(InputStream input) {
        return new PrimitiveReader(input);
    }
    
    public static PrimitiveReader reader(Channel channel) {
        return reader(channel.getInputStream());
    }
    
    public static PrimitiveWriter writer(OutputStream output) {
        return new PrimitiveWriter(new BufferedOutputStream(output));
    }
    
    public static PrimitiveWriter writer(Channel channel) {
        return writer(channel.getOutputStream());
    }
}
