package opticnav.ardd.ard;

public class ARDLobbyConnectionStatus {
    public enum Status {
        NOPASSCODE, ALREADYCONNECTED, CONNECTED
    }
    
    private final Status status;
    private final ARDLobbyConnection connection;
    
    public ARDLobbyConnectionStatus(Status status) {
        this.status = status;
        this.connection = null;
    }
    
    public ARDLobbyConnectionStatus(ARDLobbyConnection connection) {
        this.status = Status.CONNECTED;
        this.connection = connection;
    }
    
    public ARDLobbyConnection getConnection() {
        return this.connection;
    }
    
    public Status getStatus() {
        return this.status;
    }
}
