package opticnav.ardd.protocol;

public class ConfCode extends HexCode {
    public ConfCode(String code) {
        super(code);
        checkByteCount();
    }
    
    public ConfCode(byte[] code) {
        super(code);
        checkByteCount();
    }
    
    public static boolean isStringCodeValid(String stringCode) {
        return HexCode.isStringCodeValid(stringCode, Protocol.CONFCODE_BYTES);
    }
    
    private void checkByteCount() {
        if (this.getByteCount() != Protocol.CONFCODE_BYTES) {
            throw new IllegalArgumentException(
                    String.format("Confcode must be %d bytes, is %d bytes",
                    Protocol.CONFCODE_BYTES, this.getByteCount()));
        }
    }
}
