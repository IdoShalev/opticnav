package opticnav.ardd.ard;

public class ARDInstanceJoinStatus {
    public enum Status {
        NOINSTANCE, ALREADYJOINED, JOINED
    }
    
    private final Status status;
    private final ARDInstance instance;
    
    public ARDInstanceJoinStatus(Status status) {
        this.status = status;
        this.instance = null;
    }
    
    public ARDInstanceJoinStatus(ARDInstance instance) {
        this.status = Status.JOINED;
        this.instance = instance;
    }
    
    public ARDInstance getInstance() {
        if (this.status != Status.JOINED) {
            throw new IllegalStateException("Cannot get instance - Status is " + this.status);
        }
        
        return this.instance;
    }
    
    public Status getStatus() {
        return this.status;
    }
}
