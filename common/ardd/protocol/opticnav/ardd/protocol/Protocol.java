package opticnav.ardd.protocol;

public final class Protocol {
    public static final int DEFAULT_ADMIN_PORT = 8888;
    public static final int DEFAULT_ARD_PORT = 4444;
    
    public static final class AdminClient {
        public enum Commands {
            REGISTER(0, "registerARDWithConfCode"),
            ARDINFO(1, "ardInfo");
            
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
