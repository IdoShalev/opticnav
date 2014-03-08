package opticnav.persistence.web;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

public class AccountBroker {
    private java.sql.Connection conn;
    private int accountID;
    
    public AccountBroker(java.sql.Connection conn, int accountID) {
        this.conn = conn;
        this.accountID = accountID;
    }
    
    public int getARD() throws AccountBrokerException {
        try (CallableStatement cs = conn.prepareCall("{? = call getARD(?)}")) {
            cs.setInt(2, accountID);
            cs.registerOutParameter(1, Types.INTEGER);

            cs.execute();
            int ard_id = cs.getInt(1);
            return ard_id;
        } catch (SQLException e) {
            throw new AccountBrokerException(e);
        }
        
    }
    
    public void setARD(int ardID) throws AccountBrokerException {
        try (CallableStatement cs = conn.prepareCall("{call setARD(?, ?)}")) {
            cs.setInt(1, accountID);
            cs.setInt(2, ardID);
            
            cs.execute();
            conn.commit();
        } catch (SQLException e) {
            throw new AccountBrokerException(e);
        }
    }
    
    public void removeARD() throws AccountBrokerException {
        try (CallableStatement cs = conn.prepareCall("{call removeARD(?)}")) {
            cs.setInt(1, accountID);
            
            cs.execute();
            conn.commit();
        } catch (SQLException e) {
            throw new AccountBrokerException(e);
        }
    }
}
