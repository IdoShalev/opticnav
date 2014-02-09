package opticnav.ardd;

import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import opticnav.ardd.protocol.HexCode;
import opticnav.ardd.protocol.PrimitiveReader;
import opticnav.ardd.protocol.PrimitiveWriter;

public class AdminClientConnection implements Runnable {
    private Closeable closeableStream;
    private PrimitiveReader input;
    private PrimitiveWriter output;
    private Logger logger;
    
    public AdminClientConnection(Closeable closeable,
                InputStream input, OutputStream output) {
        this.closeableStream = closeable;
        this.input = new PrimitiveReader(input);
        this.output = new PrimitiveWriter(output);
        // XXX
        this.logger = Logger.getAnonymousLogger();
    }
    
    @Override
    public void run() {
        try {
            HexCode hc = new HexCode(input.readFixedBlob(4));
            System.out.println(hc);
            output.flush();
        } catch (EOFException e) {
            // The stream has ended. Quietly catch.
        } catch (IOException e) {
            this.logger.log(Level.SEVERE, "IO Exception", e);
        } finally {
            // in all cases, close the stream if available
            
            if (this.closeableStream != null) {
                // quietly close
                try { this.closeableStream.close(); } catch (IOException e) {}
            }
            
            this.logger.info("Admin client closed connection");
        }
    }
}
