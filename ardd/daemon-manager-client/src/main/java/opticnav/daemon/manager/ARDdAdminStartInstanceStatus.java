package opticnav.daemon.manager;

public class ARDdAdminStartInstanceStatus {
    public enum Status {
        IMAGE_TOO_BIG, DEPLOYED 
    }
    
    private final Status status;
    private int instanceID;
    
    public ARDdAdminStartInstanceStatus(Status status) {
        this.status = status;
    }

    public ARDdAdminStartInstanceStatus(int instanceID) {
        this.status = Status.DEPLOYED;
        this.instanceID = instanceID;
    }
    
    public Status getStatus() {
        return this.status;
    }
    
    public int getInstanceID() {
        if (this.status != Status.DEPLOYED) {
            throw new IllegalStateException("Cannot get instance id - Status is " + this.status);
        }
        
        return this.instanceID;
    }

}
