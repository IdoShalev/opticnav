package opticnav.ardd.admin;

import opticnav.ardd.protocol.HexCode;

public interface AdminConnection {
    public int registerARDWithConfCode(HexCode confcode) throws AdminConnectionException;
}
