package opticnav.ardd.admin;

import java.io.IOException;

import opticnav.ardd.protocol.ConfCode;

public interface AdminConnection extends AutoCloseable {
    /**
     * Registers an ARD using the confirmation code generated for said device.
     * This allows ARDd to authorize the device for future connections.
     * 
     * @param confcode The confirmation code sent to and associated with a device
     * @return The ARD id identifying the device (not the passCode)
     * @throws AdminConnectionException
     */
    public int registerARD(ConfCode confcode) throws AdminConnectionException;
    
    /**
     * Deploys an instance to ARDd
     * 
     * @see InstanceDeploymentBuilder
     * 
     * @param deployment The InstanceDeployment object
     * @return The instance ID as returned by the server
     */
    public int deployInstance(InstanceDeployment deployment) throws AdminConnectionException;
    
    /**
     * Shuts down any resources used by the connection object.
     */
    public void close() throws IOException;
}
