package opticnav.ardd;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * The Console Driver opens an admin client connection that is bound to
 * standard input/output streams. This class is primarily for testing purposes.
 *
 */
public class AdminConsoleDriver {
    public static void main(String[] args) {
        InputStreamReader input = new InputStreamReader(System.in);
        OutputStreamWriter output = new OutputStreamWriter(System.out);
        
        AdminClientConnection conn;
        conn = new AdminClientConnection(System.in, input, output);
        
        // run on the same thread
        conn.run();
    }
}
