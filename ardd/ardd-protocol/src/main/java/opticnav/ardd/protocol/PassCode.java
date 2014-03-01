package opticnav.ardd.protocol;

public class PassCode extends HexCode {
    public PassCode(String code) {
        super(code);
        checkByteCount();
    }
    
    public PassCode(byte[] code) {
        super(code);
        checkByteCount();
    }
    
    public static boolean isStringCodeValid(String stringCode) {
        return HexCode.isStringCodeValid(stringCode, Protocol.PASSCODE_BYTES);
    }
    
    private void checkByteCount() {
        if (this.getByteCount() != Protocol.PASSCODE_BYTES) {
            throw new IllegalArgumentException(
                    String.format("Passcode must be %d bytes, is %d bytes",
                    Protocol.PASSCODE_BYTES, this.getByteCount()));
        }
    }
}
