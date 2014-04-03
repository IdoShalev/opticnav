package opticnav.ardd.clients;

import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.util.concurrent.Callable;

import org.apache.commons.io.IOUtils;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import opticnav.ardd.protocol.PrimitiveReader;
import opticnav.ardd.protocol.PrimitiveWriter;
import opticnav.ardd.protocol.chan.Channel;

/**
 * ClientCommandDispatcher reads incoming commands from a provided Channel and dispatches
 * command requests to a CommandHandler.
 */
public final class ClientCommandDispatcher implements Callable<Void> {
    private static final XLogger logger = XLoggerFactory.getXLogger(ClientCommandDispatcher.class);
    
    public interface CommandHandler extends Closeable {
        public void command(int code, PrimitiveReader in, PrimitiveWriter out)
                throws IOException, InterruptedException;
        
        public void close() throws IOException;
    }
    
    private PrimitiveReader input;
    private PrimitiveWriter output;
    private CommandHandler cmd;
    
    public ClientCommandDispatcher(Channel channel, CommandHandler cmd) {
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
            IOUtils.closeQuietly(this.cmd);
            logger.exit();
        }
        
        return null;
    }
}
