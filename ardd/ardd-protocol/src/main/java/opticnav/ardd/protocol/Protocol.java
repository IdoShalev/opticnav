package opticnav.ardd.protocol;

public final class Protocol {
    public static final int DEFAULT_ADMIN_PORT = 8888;
    public static final int DEFAULT_ARD_PORT = 4444;
    
    public static final int CONFCODE_BYTES = 4;
    public static final int PASSCODE_BYTES = 16;
    
    public static final class AdminClient {
        public enum Commands {
            REGARD(0, "regard"),
            ARDINFO(1, "ardinfo");
            
            private int code;
            private String command;

            private Commands(int code, String command) {
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
        
        public enum Commands {
            REQCODES(0),
            CONNECT_TO_LOBBY(1);
            
            public final int CODE;

            private Commands(int code) {
                this.CODE = code;
            }
        }

        public static final class Lobby {
            public enum Commands {
                LIST_INSTANCES(0);

                public final int CODE;

                private Commands(int code) {
                    this.CODE = code;
                }
            }
        }
    }
}
