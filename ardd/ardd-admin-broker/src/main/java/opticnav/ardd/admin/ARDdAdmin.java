package opticnav.ardd.admin;

import java.io.IOException;
import java.util.List;

import opticnav.ardd.protocol.ConfCode;
import opticnav.ardd.protocol.InstanceDeploymentInfo;

public interface ARDdAdmin extends AutoCloseable {
    /**
     * Shuts down any resources used by the connection object.
     */
    public void close() throws IOException;
    
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
     * @param owner An ID number identifying the owner. This is an implementation-defined number as
     *              determined by the host application (ie. web account id).
     * @param deployment The InstanceDeployment object
     */
    public ARDdAdminStartInstanceStatus deployInstance(long owner, InstanceDeployment deployment)
            throws ARDdAdminException;

    /**
     * Get a list of actively running instances associated with an owner identifier
     * @param owner An ID number identifying the owner. This is an implementation-defined number as
     *              determined by the host application (ie. web account id).
     * @throws ARDdAdminException 
     */
    public List<InstanceDeploymentInfo> listInstancesByOwner(long owner) throws ARDdAdminException;
}
