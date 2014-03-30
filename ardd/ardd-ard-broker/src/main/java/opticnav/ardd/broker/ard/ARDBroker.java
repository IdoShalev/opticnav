package opticnav.ardd.broker.ard;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import opticnav.ardd.ard.ARDConnection;
import opticnav.ardd.ard.ARDConnectionException;
import opticnav.ardd.ard.ARDLobbyConnectionStatus;
import opticnav.ardd.protocol.ConfCode;
import opticnav.ardd.protocol.PassCode;
import opticnav.ardd.protocol.PrimitiveReader;
import opticnav.ardd.protocol.PrimitiveUtil;
import opticnav.ardd.protocol.PrimitiveWriter;
import opticnav.ardd.protocol.Protocol;
import opticnav.ardd.protocol.chan.Channel;
import opticnav.ardd.protocol.chan.ChannelMultiplexer;
import opticnav.ardd.protocol.chan.ChannelMultiplexer.Listener;
import static opticnav.ardd.protocol.consts.ARDdARDProtocol.*;

public class ARDBroker implements ARDConnection {
    private static final XLogger LOG = XLoggerFactory
            .getXLogger(ARDBroker.class);
    
    final private Channel channel;
    final private ChannelMultiplexer mpxr;
    final private ExecutorService threadPool;
    final private Future<Void> listenerResult;
    final private Channel gatekeeperChannel;
    
    public ARDBroker(Channel channel, ExecutorService threadPool) {
        this.channel = channel;
        this.mpxr = new ChannelMultiplexer(channel);
        this.threadPool = threadPool;
        
        // Channel 0 is the pre-established gatekeeper channel
        this.gatekeeperChannel = this.mpxr.createChannel(Channels.GATEKEEPER);
        
        Listener listener = this.mpxr.createListener();
        listenerResult = this.threadPool.submit(listener);
    }
    
    @Override
    public void close() throws IOException {
        try {
            // wait for the listener task to complete
            LOG.debug("Closing listener...");
            // TODO - graceful shutdown so that this doesn't block...
            this.listenerResult.get();
            LOG.debug("Listener closed");
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
            
            output.writeUInt8(Commands.REQCODES);
            output.flush();
            
            confCodeBytes = input.readFixedBlob(Protocol.CONFCODE_BYTES);
            int cancellationChanID = input.readUInt8();
            LOG.debug("Cancellation channel: " + cancellationChanID);
            
            ConfCode confCode = new ConfCode(confCodeBytes);
            cancellationChan = this.mpxr.createChannel(cancellationChanID);
            c.confCode(confCode, new CancellationImpl(cancellationChan));
            
            // waiting for a response...
            
            int response = input.readUInt8();

            if (response == ReqCodes.REGISTERED) {
                // registered an ARD with confcode
                byte[] passCodeBytes;
                passCodeBytes = input.readFixedBlob(Protocol.PASSCODE_BYTES);
                PassCode passCode = new PassCode(passCodeBytes);
                
                c.registered(passCode);
            } else if (response == ReqCodes.COULDNOTREGISTER) {
                // could not register with confcode
                c.couldNotRegister();
            } else if (response == ReqCodes.CANCELLED) {
                // cancelled
                c.cancelled();
            } else {
                throw new IllegalStateException("Invalid response code: " + response);
            }
        } catch (IOException e) {
            throw new ARDConnectionException(e);
        }
    }

    @Override
    public ARDLobbyConnectionStatus connectToLobby(PassCode passCode) throws ARDConnectionException {
        try {
            PrimitiveReader input  = PrimitiveUtil.reader(gatekeeperChannel);
            PrimitiveWriter output = PrimitiveUtil.writer(gatekeeperChannel);

            output.writeUInt8(Commands.CONNECT);
            output.writeFixedBlob(passCode.getByteArray());
            output.flush();

            int response = input.readUInt8();
            if (response == 0) {
                // passcode acknowledged, can connect to lobby
                Channel lobbyChannel = mpxr.createChannel(Channels.LOBBY);
                return new ARDLobbyConnectionStatus(new ARDLobbyConnectionImpl(lobbyChannel));
            } else if (response == 1) {
                // passcode doesn't exist
                return new ARDLobbyConnectionStatus(ARDLobbyConnectionStatus.Status.NOPASSCODE);
            } else if (response == 2) {
                // there's an ongoing lobby connection
                return new ARDLobbyConnectionStatus(ARDLobbyConnectionStatus.Status.ALREADYCONNECTED);
            } else {
                throw new IllegalStateException("Invalid response code: " + response);
            }
        } catch (IOException e) {
            throw new ARDConnectionException(e);
        }
    }
}
