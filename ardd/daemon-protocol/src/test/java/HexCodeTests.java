import static org.junit.Assert.*;
import opticnav.daemon.protocol.HexCode;

import org.junit.Test;

public class HexCodeTests {
    @Test
    public void test() {
        String stringCode = "0123456789ABCDEF";
        HexCode hc = new HexCode(stringCode);
        byte[] expectedByteArray = {0x01,0x23,0x45,0x67,(byte)0x89,(byte)0xAB,(byte)0xCD,(byte)0xEF};
        
        assertArrayEquals(expectedByteArray, hc.getByteArray());
        
        assertEquals(stringCode, hc.getString());
    }
    
    @Test(expected = IllegalArgumentException.class) 
    public void emptyCode() {
        byte[] b = {};
        new HexCode(b);
    }
    
    @Test
    public void validCodes() {
        assertTrue(HexCode.isStringCodeValid("fF"));
        assertTrue(HexCode.isStringCodeValid("00000000"));
    }
    
    @Test
    public void invalidCodes() {
        assertFalse(HexCode.isStringCodeValid(""));
        assertFalse(HexCode.isStringCodeValid(" 00"));
        assertFalse(HexCode.isStringCodeValid("a"));
        assertFalse(HexCode.isStringCodeValid("ag"));
        assertFalse(HexCode.isStringCodeValid("00 00 00 00"));
        // What the hell is this?
        // The characters '４' and 'Ｆ' are not what they seem!
        // They're unicode variants. Here are the normal versions side-by-side:
        // ４4 and ＦF. They look slightly different, because they are.
        // Both variants return true in Character.isDigit()
        assertFalse(HexCode.isStringCodeValid("４Ｆ"));
    }
    
    @Test
    public void checkEquality() {
        // we cannot check hash inequality because hashes may collide
        // only hash equality can be checked
        
        HexCode hc1 = new HexCode("0123");
        HexCode hc2 = new HexCode("0123");
        HexCode hc3 = new HexCode("0124");
        
        assertEquals(hc1.hashCode(), hc2.hashCode());
        assertTrue(hc1.equals(hc2));
        assertFalse(hc1.equals(hc3));
    }
}
