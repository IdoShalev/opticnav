package opticnav.ardd.broker.ard;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import opticnav.ardd.ard.ARDConnection;
import opticnav.ardd.ard.ARDConnectionException;
import opticnav.ardd.ard.ARDLobbyConnection;
import opticnav.ardd.protocol.ConfCode;
import opticnav.ardd.protocol.PassCode;
import opticnav.ardd.protocol.PrimitiveReader;
import opticnav.ardd.protocol.PrimitiveUtil;
import opticnav.ardd.protocol.PrimitiveWriter;
import opticnav.ardd.protocol.Protocol;
import opticnav.ardd.protocol.chan.Channel;
import opticnav.ardd.protocol.chan.ChannelMultiplexer;
import opticnav.ardd.protocol.chan.ChannelMultiplexer.Listener;
import static opticnav.ardd.protocol.Protocol.ARDClient.*;

public class ARDBroker implements ARDConnection {
    private ChannelMultiplexer mpxr;
    private ExecutorService threadPool;
    private Future<Void> listenerResult;
    private Channel gatekeeperChannel;
    
    public ARDBroker(Channel channel, ExecutorService threadPool) {
        this.mpxr = new ChannelMultiplexer(channel);
        this.threadPool = threadPool;
        
        // Channel 0 is the pre-established gatekeeper channel
        this.gatekeeperChannel = this.mpxr.createChannel(CHANNEL_GATEKEEPER);
        
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
            
            output.writeUInt8(Commands.REQCODES.CODE);
            output.flush();
            
            confCodeBytes = input.readFixedBlob(Protocol.CONFCODE_BYTES);
            int cancellationChanID = input.readUInt8();
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
    public ARDLobbyConnection connectToLobby(PassCode passCode) throws ARDConnectionException {
        try {
            PrimitiveReader input  = PrimitiveUtil.reader(gatekeeperChannel);
            PrimitiveWriter output = PrimitiveUtil.writer(gatekeeperChannel);

            output.writeUInt8(Commands.CONNECT_TO_LOBBY.CODE);
            output.writeFixedBlob(passCode.getByteArray());
            output.flush();

            int response = input.readUInt8();
            if (response == 0) {
                // passcode acknowledged, can connect to lobby
                Channel lobbyChannel = mpxr.createChannel(CHANNEL_LOBBY);
                return new ARDLobbyConnectionImpl(lobbyChannel);
            } else if (response == 1) {
                // passcode doesn't exist
                return null;
            } else if (response == 2) {
                // there's an ongoing lobby connection
                return null;
            } else {
                throw new IllegalStateException("Invalid response code: " + response);
            }
        } catch (IOException e) {
            throw new ARDConnectionException(e);
        }
    }
}
