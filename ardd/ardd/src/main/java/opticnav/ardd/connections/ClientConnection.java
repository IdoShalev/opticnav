package opticnav.ardd.connections;

import java.io.EOFException;
import java.io.IOException;
import java.util.concurrent.Callable;

import org.apache.commons.io.IOUtils;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import opticnav.ardd.protocol.PrimitiveReader;
import opticnav.ardd.protocol.PrimitiveWriter;
import opticnav.ardd.protocol.chan.Channel;

// TODO - ClientConnect should implement Callable<Void>
public final class ClientConnection implements Callable<Void> {
    private static final XLogger logger = XLoggerFactory.getXLogger(ClientConnection.class);
    
    public interface CommandHandler {
        public void command(int code, PrimitiveReader in, PrimitiveWriter out)
                throws IOException, InterruptedException;
    }
    
    private PrimitiveReader input;
    private PrimitiveWriter output;
    private CommandHandler cmd;
    
    public ClientConnection(Channel channel, CommandHandler cmd) {
        this.input = new PrimitiveReader(channel.getInputStream());
        this.output = new PrimitiveWriter(channel.getOutputStream());
        this.cmd = cmd;
    }
    
    @Override
    public Void call() {
        logger.entry();
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
            logger.catching(e);
        } finally {
            // in all cases, close the stream
            IOUtils.closeQuietly(this.output);
            logger.exit();
        }
        
        return null;
    }
}
