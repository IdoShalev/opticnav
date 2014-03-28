package opticnav.ardd.broker.admin;

import java.io.IOException;

import opticnav.ardd.admin.AdminConnection;
import opticnav.ardd.admin.AdminConnectionException;
import opticnav.ardd.admin.InstanceDeployment;
import opticnav.ardd.protocol.ConfCode;
import opticnav.ardd.protocol.PrimitiveReader;
import opticnav.ardd.protocol.PrimitiveWriter;
import opticnav.ardd.protocol.Protocol.AdminClient.Commands;
import opticnav.ardd.protocol.chan.Channel;

public class AdminBroker implements AdminConnection {
    private PrimitiveReader input;
    private PrimitiveWriter output;
    
    public AdminBroker(Channel channel) {
        this.input = new PrimitiveReader(channel.getInputStream());
        this.output = new PrimitiveWriter(channel.getOutputStream());
    }

    /**
     * @return 0 if no ARD was registered, ARD ID (1+) if registered
     */
    @Override
    public int registerARD(ConfCode code)
            throws AdminConnectionException {
        try {
            this.output.writeUInt8(Commands.REGARD);
            this.output.writeFixedBlob(code.getByteArray());
            this.output.flush();
            return this.input.readUInt31();
        } catch (IOException e) {
            throw new AdminConnectionException(e);
        }
    }
    
    @Override
    public int deployInstance(InstanceDeployment deployment) {
        // TODO - do some work!
        return 0;
    }

    @Override
    public void close() throws IOException {
        this.output.close();
    }
}
