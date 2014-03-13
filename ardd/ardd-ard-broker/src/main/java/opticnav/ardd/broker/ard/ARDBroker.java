package opticnav.ardd.broker.ard;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import opticnav.ardd.ard.ARDConnection;
import opticnav.ardd.ard.ARDConnectionException;
import opticnav.ardd.protocol.ConfCode;
import opticnav.ardd.protocol.PassCode;
import opticnav.ardd.protocol.PrimitiveReader;
import opticnav.ardd.protocol.PrimitiveUtil;
import opticnav.ardd.protocol.PrimitiveWriter;
import opticnav.ardd.protocol.Protocol;
import opticnav.ardd.protocol.Protocol.ARDClient.Commands;
import opticnav.ardd.protocol.chan.Channel;
import opticnav.ardd.protocol.chan.ChannelMultiplexer;
import opticnav.ardd.protocol.chan.ChannelMultiplexer.Listener;

public class ARDBroker implements ARDConnection {
    private ChannelMultiplexer mpxr;
    private ExecutorService threadPool;
    private Future<Void> listenerResult;
    private Channel gatekeeperChannel;
    
    public ARDBroker(Channel channel, ExecutorService threadPool) {
        this.mpxr = new ChannelMultiplexer(channel);
        this.threadPool = threadPool;
        
        // Channel 0 is the pre-established gatekeeper channel
        this.gatekeeperChannel = this.mpxr.createChannel(0);
        
        Listener listener = this.mpxr.createListener();
        listenerResult = this.threadPool.submit(listener);
    }
    
    @Override
    public void close() throws IOException {
        try {
            // wait for the listener task to complete
            this.listenerResult.get();
        } catch (InterruptedException e) {
            throw new IOException(e);
        } catch (ExecutionException e) {
            throw new IOException(e);
        }
    }
    
    @Override
    public void requestPassConfCodes(RequestPassConfCodesCallback c)
            throws ARDConnectionException {
        try {
            PrimitiveReader input  = PrimitiveUtil.reader(gatekeeperChannel);
            PrimitiveWriter output = PrimitiveUtil.writer(gatekeeperChannel);
            
            byte[] confCodeBytes;
            Channel cancellationChan;
            
            output.writeUInt8(Commands.REQCODES.getCode());
            output.flush();
            
            confCodeBytes = input.readFixedBlob(Protocol.CONFCODE_BYTES);
            int cancellationChanID = input.readUInt8();
            ConfCode confCode = new ConfCode(confCodeBytes);
            cancellationChan = this.mpxr.createChannel(cancellationChanID);
            c.confCode(confCode, new CancellationImpl(cancellationChan));
            
            // waiting for a response...
            
            int response = input.readUInt8();
            
            // TODO - replace with constants
            if (response == 0) {
                // registered an ARD with confcode
                int ardID = input.readUInt31();
                byte[] passCodeBytes;
                passCodeBytes = input.readFixedBlob(Protocol.PASSCODE_BYTES);
                PassCode passCode = new PassCode(passCodeBytes);
                
                c.registered(passCode, ardID);
            } else if (response == 1) {
                // could not register with confcode
                c.couldnotregister();
            } else if (response == 2) {
                // cancelled
                c.cancelled();
            }
        } catch (IOException e) {
            throw new ARDConnectionException(e);
        }
    }
}
