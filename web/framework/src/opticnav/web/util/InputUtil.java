package opticnav.web.util;

public final class InputUtil {
    public static boolean isEntered(String... args) {
        for (String s: args) {
            if (s == null || s.isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isInteger(String... args) {
        try {
            for (String s: args) {
                Integer.parseInt(s);
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
