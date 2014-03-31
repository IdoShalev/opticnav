package opticnav.ardd.admin;

import java.io.IOException;

import opticnav.ardd.protocol.ConfCode;

public interface ARDdAdmin extends AutoCloseable {
    /**
     * Registers an ARD using the confirmation code generated for said device.
     * This allows ARDd to authorize the device for future connections.
     * 
     * @param confcode The confirmation code sent to and associated with a device
     * @return The ARD id identifying the device (not the passCode)
     * @throws ARDdAdminException
     */
    public int registerARD(ConfCode confcode) throws ARDdAdminException;
    
    /**
     * Deploys an instance to ARDd
     * 
     * @see InstanceDeploymentBuilder
     * 
     * @param deployment The InstanceDeployment object
     */
    public ARDdAdminStartInstanceStatus deployInstance(InstanceDeployment deployment)
            throws ARDdAdminException;
    
    /**
     * Shuts down any resources used by the connection object.
     */
    public void close() throws IOException;
}
