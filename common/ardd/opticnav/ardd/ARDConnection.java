package opticnav.ardd;

import java.util.List;

public interface ARDConnection {
    public void getSessions(List<SessionInfo> sessions);
    public ARDSessionConnection joinSession(int sessionID);
}
