package opticnav.ardd.broker.ard;

import java.io.IOException;
import java.io.OutputStream;

import opticnav.ardd.ard.ARDGatekeeper;
import opticnav.ardd.protocol.chan.Channel;

class CancellationImpl implements ARDGatekeeper.Cancellation {
    private OutputStream output;

    public CancellationImpl(OutputStream output) {
        this.output = output;
    }
    
    public CancellationImpl(Channel channel) {
        this(channel.getOutputStream());
    }
    
    @Override
    public void cancel() {
        try {
            /* XXX: there's potential for a race condition
             * eg. if the other side closes the cancellation channel, then if
             * this method is invoked...
             */
            // just write a simple flag
            // note: the other side is responsible for EOF
            this.output.write(1);
            this.output.flush();
        } catch (IOException e) {
            // Something has gone _seriously_ wrong
            // TODO - log this
            e.printStackTrace();
        }
    }
}
