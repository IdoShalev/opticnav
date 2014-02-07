package opticnav.ardd;

public interface ARDGatekeeper {
    public interface RequestPassConfCode {
        public void passConfCode(PassConfCode code);
    }
    
    public ARDConnection connect(String passcode);
    public boolean requestPassConfCode(RequestPassConfCode a);
}
