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
        public enum Commands {
            REQCODES(0, "reqcodes");
            
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
}
