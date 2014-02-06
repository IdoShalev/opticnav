package opticnav.ardd;

public interface ARDGatekeeper {
    public ARDConnection connect(String passcode);
    public PassConfCode requestPassConfCode();
}
