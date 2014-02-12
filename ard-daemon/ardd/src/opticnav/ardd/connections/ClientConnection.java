package opticnav.ardd.connections;

import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import opticnav.ardd.protocol.PrimitiveReader;
import opticnav.ardd.protocol.PrimitiveWriter;

public final class ClientConnection implements Runnable {
    public interface CommandHandler {
        public void command(int code, PrimitiveReader in, PrimitiveWriter out)
                throws IOException;
    }
    
    private Closeable closeableStream;
    private PrimitiveReader input;
    private PrimitiveWriter output;
    private CommandHandler cmd;
    private Logger logger;
    
    public ClientConnection(Closeable closeable,
                InputStream input, OutputStream output,
                CommandHandler cmd) {
        this.closeableStream = closeable;
        this.input = new PrimitiveReader(input);
        this.output = new PrimitiveWriter(output);
        // XXX
        this.logger = Logger.getAnonymousLogger();
        this.cmd = cmd;
    }
    
    @Override
    public void run() {
        try {
            int code = this.input.readUInt8();
            
            this.cmd.command(code, this.input, this.output);
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
