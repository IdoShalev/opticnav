package opticnav.ardd;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class AdminClientConnection implements Runnable {
    private Closeable closeableStream;
    private BufferedReader input;
    private Writer output;
    
    public AdminClientConnection(Closeable closeable,
                Reader input, Writer output) {
        this.closeableStream = closeable;
        this.input = new BufferedReader(input);
        this.output = output;
    }
    
    private String readLine() throws IOException {
        String header = input.readLine();
        if (header == null) {
            throw new EOFException();
        } else {
            return header.trim();
        }
    }
    
    @Override
    public void run() {
        try {
            boolean running = true;
            
            while (running) {
                String header = readLine();
                
                if (header.equals("q")) {
                    running = false;
                } else {
                    output.write(header);
                    output.write("\n");
                    output.flush();
                }
            }
        } catch (EOFException e) {
            // The stream has ended. Quietly catch.
        } catch (IOException e) {
            // TODO - log somewhere
            e.printStackTrace();
        } finally {
            // in all cases, close the stream if available
            
            if (this.closeableStream != null) {
                // silently close
                try { this.closeableStream.close(); } catch (IOException e) {}
            }
        }
    }
}
