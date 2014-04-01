package opticnav.ardd.clients.ardclient;

import opticnav.ardd.ARDConnection;
import opticnav.ardd.Instance;
import opticnav.ardd.clients.AnnotatedCommandHandler;

public class InstanceCommandHandler extends AnnotatedCommandHandler {
    private final ARDConnection connection;
    private final Instance instance;

    public InstanceCommandHandler(ARDConnection connection, Instance instance) {
        super(InstanceCommandHandler.class);
        this.connection = connection;
        this.instance = instance;
    }
    
    // TODO - actual commands
}
