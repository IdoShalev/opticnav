package opticnav.ardd;

public class PassConfCode {
    private String passcode;
    private String confcode;
    
    public PassConfCode(String passcode, String confcode) {
        this.passcode = passcode;
        this.confcode = confcode;
    }
    
    public String getPasscode() {
        return this.passcode;
    }
    
    public String getConfcode() {
        return this.confcode;
    }
}
