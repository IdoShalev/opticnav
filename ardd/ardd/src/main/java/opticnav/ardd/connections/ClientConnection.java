package opticnav.ardd.connections;

import java.io.EOFException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

import opticnav.ardd.protocol.PrimitiveReader;
import opticnav.ardd.protocol.PrimitiveWriter;
import opticnav.ardd.protocol.chan.Channel;

// TODO - ClientConnect should implement Callable<Void>
public final class ClientConnection implements Runnable {
    public interface CommandHandler {
        public void command(int code, PrimitiveReader in, PrimitiveWriter out)
                throws IOException, InterruptedException;
    }
    
    private PrimitiveReader input;
    private PrimitiveWriter output;
    private CommandHandler cmd;
    private Logger logger;
    
    public ClientConnection(Channel channel, CommandHandler cmd) {
        this.input = new PrimitiveReader(channel.getInputStream());
        this.output = new PrimitiveWriter(channel.getOutputStream());
        // XXX
        this.logger = Logger.getAnonymousLogger();
        this.cmd = cmd;
    }
    
    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                int code = this.input.readUInt8();
                
                try {
                    this.cmd.command(code, this.input, this.output);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        } catch (EOFException e) {
            // The stream has ended. Quietly catch.
        } catch (IOException e) {
            this.logger.log(Level.SEVERE, "IO Exception", e);
        } finally {
            // in all cases, close the stream
            IOUtils.closeQuietly(this.output);
            
            this.logger.info("Admin client closed connection");
        }
    }
}
