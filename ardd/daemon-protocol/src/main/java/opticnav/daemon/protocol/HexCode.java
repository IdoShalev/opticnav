package opticnav.daemon.protocol;

import java.util.Arrays;

/**
 * A fixed-length code represented in hexadecimal.
 * Converts a string of hexadecimal digits to a byte array, and vice-versa.
 * 
 * @author Danny Spencer
 *
 */
public class HexCode {
    private byte[] code;
    
    public HexCode(String stringCode) {
        if (!isStringCodeValid(stringCode)) {
            throw new IllegalArgumentException("String code is not valid");
        }
        
        int len = stringCode.length();
        
        this.code = new byte[len/2];
        
        for (int i = 0; i < len; i += 2) {
            int val = 0;
            for (int j = 0; j < 2; j++) {
                char c = stringCode.charAt(i+j);
                int digit = charToHexDigit(c);
                
                val = (val << 4) | digit;
            }
            code[i/2] = (byte)val;
        }
    }
    
    public HexCode(byte[] code) {
        if (code.length <= 0) {
            throw new IllegalArgumentException();
        }
        
        this.code = new byte[code.length];
        System.arraycopy(code, 0, this.code, 0, code.length);
    }
    
    public String getString() {
        StringBuilder sb = new StringBuilder(this.code.length*2);
        
        for (int i = 0; i < this.code.length; i++) {
            int hi = (this.code[i] & 0xF0) >> 4;
            int lo = this.code[i] & 0x0F;
            
            sb.append(hexDigitToChar(hi));
            sb.append(hexDigitToChar(lo));
        }
        
        return sb.toString();
    }
    
    public byte[] getByteArray() {
        return Arrays.copyOf(this.code, this.code.length);
    }
    
    public int getByteCount() {
        return this.code.length;
    }
    
    @Override
    public String toString() {
        return getString();
    }
    
    @Override
    public int hashCode() {
        return java.util.Arrays.hashCode(this.code);
    }

    @Override
    public boolean equals(Object obj) {
        HexCode hc = (HexCode) obj;

        if (hc.code.length != this.code.length) {
            throw new IllegalArgumentException(
                    "Hex codes are different lengths");
        }
        
        for (int i = 0; i < this.code.length; i++) {
            if (this.code[i] != hc.code[i]) {
                return false;
            }
        }
        
        return true;
    }

    /**
     * String must only include hexadecimal digits, and must have an even number
     * of digits
     */
    public static boolean isStringCodeValid(String stringCode) {
        if (stringCode.length() > 0 && stringCode.length() % 2 == 0) {
            // string has non-empty, even length
            for (int i = 0; i < stringCode.length(); i++) {
                char c = stringCode.charAt(i);
                if (charToHexDigit(c) < 0) {
                    // not a hex digit - invalid
                    return false;
                }
            }
            // didn't complain - valid
            return true;
        } else {
            return false;
        }
    }
    
    public static boolean isStringCodeValid(String stringCode, int bytes) {
        return stringCode.length() == bytes*2 && isStringCodeValid(stringCode);
    }

    private static int charToHexDigit(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        } else if (c >= 'A' && c <= 'F') {
            return c - 'A' + 10;
        } else if (c >= 'a' && c <= 'f') {
            return c - 'a' + 10;
        } else {
            return -1;
        }
    }

    private static char hexDigitToChar(int digit) {
        if (digit >= 0x00 && digit <= 0x09) {
            return (char)('0' + digit);
        } else if (digit <= 0x0F) {
            return (char)('A' + digit - 0x0A);
        } else {
            throw new IllegalStateException();
        }
    }
}
