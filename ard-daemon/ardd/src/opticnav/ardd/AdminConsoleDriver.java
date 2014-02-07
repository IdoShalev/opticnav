package opticnav.ardd;

/**
 * The Console Driver opens an admin client connection that is bound to
 * standard input/output streams. This class is primarily for testing purposes.
 *
 */
public class AdminConsoleDriver {
    public static void main(String[] args) {
        AdminClientConnection conn;
        conn = new AdminClientConnection(System.in, System.in, System.out);
        
        // run on the same thread
        conn.run();
    }
}
