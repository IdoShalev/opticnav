package opticnav.ardd.broker.admin;

import java.io.Closeable;
import java.io.IOException;

import opticnav.ardd.admin.AdminConnection;
import opticnav.ardd.admin.AdminConnectionException;
import opticnav.ardd.protocol.HexCode;
import opticnav.ardd.protocol.PrimitiveReader;
import opticnav.ardd.protocol.PrimitiveWriter;
import opticnav.ardd.protocol.Protocol;

public class AdminBroker implements AdminConnection {
    private Closeable closeable;
    private PrimitiveReader input;
    private PrimitiveWriter output;
    
    public AdminBroker(Closeable closeable, PrimitiveReader input,
            PrimitiveWriter output) {
        this.closeable = closeable;
        this.input = input;
        this.output = output;
    }

    /**
     * @return 0 if no ARD was registered, ARD ID (1+) if registered
     */
    @Override
    public int registerARDWithConfCode(HexCode code)
            throws AdminConnectionException {
        if (code.getByteCount() != 4) {
            throw new IllegalArgumentException("Code must be 4 bytes");
        }
        
        try {
            this.output.writeUInt8(Protocol.AdminClient.Commands.REGISTER.getCode());
            this.output.writeFixedBlob(code.getByteArray());
            this.output.flush();
            return this.input.readUInt31();
        } catch (IOException e) {
            throw new AdminConnectionException(e);
        }
    }

    @Override
    public void close() throws IOException {
        this.closeable.close();
    }
}
