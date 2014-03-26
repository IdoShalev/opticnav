package opticnav.ardd.protocol;

public final class Protocol {
    public static final int DEFAULT_ADMIN_PORT = 8888;
    public static final int DEFAULT_ARD_PORT = 4444;
    
    public static final int CONFCODE_BYTES = 4;
    public static final int PASSCODE_BYTES = 16;
    
    public static final class AdminClient {
        public static class Commands {
            public static final int REGARD = 0;
            public static final int ARDINFO = 1;
        }
        
        public enum CommandsText {
            REGARD(Commands.REGARD, "regard"),
            ARDINFO(Commands.ARDINFO, "ardinfo");
            
            private int code;
            private String command;

            private CommandsText(int code, String command) {
                this.code = code;
                this.command = command;
            }
            
            public int getCode() {
                return this.code;
            }

            public String getCommand() {
                return this.command;
            }
        }
    }
    
    public static final class ARDClient {
        public static final int CHANNEL_GATEKEEPER = 0;
        public static final int CHANNEL_LOBBY = 1;

        public static final int NO_ARD = 0;

        public static class ReqCodes {
            public static final int REGISTERED = 0;
            public static final int COULDNOTREGISTER = 1;
            public static final int CANCELLED = 2;
        }
        
        public static class Commands {
            public static final int REQCODES = 0;
            public static final int CONNECT = 1;
        }

        public static final class Connected {
            public static class Commands {
                public static final int LIST_INSTANCES = 0;
            }
        }
    }
}
