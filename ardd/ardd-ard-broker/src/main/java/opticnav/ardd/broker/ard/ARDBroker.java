package opticnav.ardd.broker.ard;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;

import opticnav.ardd.ard.ARDConnection;
import opticnav.ardd.ard.ARDConnectionException;
import opticnav.ardd.protocol.BlockingInputStream;
import opticnav.ardd.protocol.HexCode;
import opticnav.ardd.protocol.PrimitiveReader;
import opticnav.ardd.protocol.PrimitiveWriter;
import opticnav.ardd.protocol.Protocol;
import opticnav.ardd.protocol.Protocol.ARDClient.Commands;

public class ARDBroker implements ARDConnection {
    private Closeable closeable;
    private PrimitiveReader input;
    private PrimitiveWriter output;
    
    public ARDBroker(Closeable closeable, PrimitiveReader input,
            PrimitiveWriter output) {
        this.closeable = closeable;
        this.input = input;
        this.output = output;
    }
    
    @Override
    public void close() throws IOException {
        this.output.flush();
        this.closeable.close();
    }
    
    @Override
    public int requestPassConfCodes(RequestPassConfCodesCallback c)
            throws ARDConnectionException {
        try {
            byte[] passCodeBytes, confCodeBytes;
            
            this.output.writeUInt8(Commands.REQCODES.getCode());
            this.output.flush();
            
            passCodeBytes = this.input.readFixedBlob(Protocol.PASSCODE_BYTES);
            confCodeBytes = this.input.readFixedBlob(Protocol.CONFCODE_BYTES);
            HexCode passCode = new HexCode(passCodeBytes);
            HexCode confCode = new HexCode(confCodeBytes);
            
            c.passConfCodes(passCode, confCode);
            
            int result = this.input.readUInt8();
            
            return result;
        } catch (IOException e) {
            throw new ARDConnectionException(e);
        }
    }

    public static ARDBroker fromSocket(Socket sock) throws IOException {
        BlockingInputStream blocking_in;
        BufferedOutputStream buffered_out;
        blocking_in = new BlockingInputStream(sock.getInputStream());
        buffered_out = new BufferedOutputStream(sock.getOutputStream());
        
        PrimitiveReader in;
        PrimitiveWriter out;
        in = new PrimitiveReader(blocking_in);
        out = new PrimitiveWriter(buffered_out);
        
        sock.setKeepAlive(true);
        return new ARDBroker(sock, in, out);
    }
}
