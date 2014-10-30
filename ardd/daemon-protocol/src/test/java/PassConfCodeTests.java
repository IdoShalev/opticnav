import static org.junit.Assert.*;
import opticnav.daemon.protocol.ConfCode;
import opticnav.daemon.protocol.PassCode;
import opticnav.daemon.protocol.Protocol;

import org.junit.Test;

public class PassConfCodeTests {
    @Test
    public void byteCount() {
        byte[] pc = new byte[Protocol.PASSCODE_BYTES];
        byte[] cc = new byte[Protocol.CONFCODE_BYTES];
        assertEquals(Protocol.PASSCODE_BYTES, new PassCode(pc).getByteCount());
        assertEquals(Protocol.CONFCODE_BYTES, new ConfCode(cc).getByteCount());
    }
    
    @Test
    public void validCodes() {
        assertTrue(PassCode.isStringCodeValid("0123456789ABCDEF0123456789ABCDEF"));
        assertTrue(PassCode.isStringCodeValid("00000000000000000000000000000000"));
        assertTrue(PassCode.isStringCodeValid("fFFFFfFFFFFFFFfffFFFFFFFFFFFfffF"));
        
        assertTrue(ConfCode.isStringCodeValid("01234567"));
        assertTrue(ConfCode.isStringCodeValid("00000000"));
        assertTrue(ConfCode.isStringCodeValid("fFFFFfFF"));
    }
    
    @Test
    public void invalidCodes() {
        assertFalse(PassCode.isStringCodeValid("  01234 56789ABCDEF01234 56789 ABCDEF   "));
        
        assertFalse(ConfCode.isStringCodeValid("  01234567  "));
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidPassCodeException1() {
        new PassCode("  01234 56789ABCDEF01234 56789 ABCDEF   ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidPassCodeException2() {
        new PassCode(new byte[Protocol.PASSCODE_BYTES+1]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidConfCodeException1() {
        new PassCode("  01234567  ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidConfCodeException2() {
        new ConfCode(new byte[Protocol.CONFCODE_BYTES+1]);
    }
}
