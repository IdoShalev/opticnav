package opticnav.ardd;

public class SessionInfo {
    private int sessionID;
    private String name;

    public SessionInfo(int sessionID, String name) {
        this.sessionID = sessionID;
        this.name = name;
    }
    
    public int getSessionID() {
        return this.sessionID;
    }
    public String getName() {
        return this.name;
    }
}
