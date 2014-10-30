package opticnav.daemon.ard;

public class ARDConnectionStatus {
    public enum Status {
        NOPASSCODE, ALREADYCONNECTED, CONNECTED
    }
    
    private final Status status;
    private final ARDConnected connection;
    
    public ARDConnectionStatus(Status status) {
        this.status = status;
        this.connection = null;
    }
    
    public ARDConnectionStatus(ARDConnected connection) {
        this.status = Status.CONNECTED;
        this.connection = connection;
    }
    
    public ARDConnected getConnection() {
        if (this.status != Status.CONNECTED) {
            throw new IllegalStateException("Cannot get connection - Status is " + this.status);
        }
        
        return this.connection;
    }
    
    public Status getStatus() {
        return this.status;
    }
}
