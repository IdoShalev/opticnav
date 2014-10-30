package opticnav.daemon.broker.ard;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import opticnav.daemon.ard.ARDGatekeeper;
import opticnav.daemon.ard.ARDGatekeeperException;
import opticnav.daemon.ard.ARDConnectionStatus;
import opticnav.daemon.protocol.ConfCode;
import opticnav.daemon.protocol.PassCode;
import opticnav.daemon.protocol.PrimitiveReader;
import opticnav.daemon.protocol.PrimitiveUtil;
import opticnav.daemon.protocol.PrimitiveWriter;
import opticnav.daemon.protocol.Protocol;
import opticnav.daemon.protocol.chan.Channel;
import opticnav.daemon.protocol.chan.ChannelMultiplexer;
import opticnav.daemon.protocol.chan.ChannelMultiplexer.Listener;
import static opticnav.daemon.protocol.consts.ARDdARDProtocol.*;

public class ARDGatekeeperBroker implements ARDGatekeeper {
    private static final XLogger LOG = XLoggerFactory
            .getXLogger(ARDGatekeeperBroker.class);
    
    final private ChannelMultiplexer mpxr;
    final private ExecutorService threadPool;
    final private Future<Void> listenerResult;
    final private Channel gatekeeperChannel;
    
    public ARDGatekeeperBroker(Channel channel, ExecutorService threadPool) {
        this.mpxr = new ChannelMultiplexer(channel);
        this.threadPool = threadPool;
        
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
            gatekeeperChannel.getOutputStream().close();
            this.listenerResult.get();
            LOG.debug("Listener closed");
            this.threadPool.shutdown();
        } catch (InterruptedException e) {
            throw new IOException(e);
        } catch (ExecutionException e) {
            throw new IOException(e);
        }
    }
    
    @Override
    public void requestPassConfCodes(RequestPassConfCodesCallback c)
            throws ARDGatekeeperException {
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
            throw new ARDGatekeeperException(e);
        }
    }

    @Override
    public ARDConnectionStatus connect(PassCode passCode) throws ARDGatekeeperException {
        try {
            PrimitiveReader input  = PrimitiveUtil.reader(gatekeeperChannel);
            PrimitiveWriter output = PrimitiveUtil.writer(gatekeeperChannel);

            output.writeUInt8(Commands.CONNECT);
            output.writeFixedBlob(passCode.getByteArray());
            output.flush();

            int response = input.readUInt8();
            if (response == 0) {
                // passcode acknowledged, can connect
                Channel connectedChannel = mpxr.createChannel(Channels.CONNECTED);
                return new ARDConnectionStatus(new ARDConnectedImpl(connectedChannel, this.mpxr, this.threadPool));
            } else if (response == 1) {
                // passcode doesn't exist
                return new ARDConnectionStatus(ARDConnectionStatus.Status.NOPASSCODE);
            } else if (response == 2) {
                // there's an ongoing connection
                return new ARDConnectionStatus(ARDConnectionStatus.Status.ALREADYCONNECTED);
            } else {
                throw new IllegalStateException("Invalid response code: " + response);
            }
        } catch (IOException e) {
            throw new ARDGatekeeperException(e);
        }
    }
}
