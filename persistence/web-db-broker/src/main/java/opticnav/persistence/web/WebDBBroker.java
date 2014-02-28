package opticnav.persistence.web;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

public class WebDBBroker {
    private java.sql.Connection conn;

    public WebDBBroker(java.sql.Connection conn) {
        this.conn = conn;
    }
    
    public boolean registerAccount(String username, String password) throws WebDBBrokerException {
        try (CallableStatement cs = conn.prepareCall("{call registerAccount(?, ?)}")) {
            cs.setString(1, username);
            cs.setString(2, password);
            
            cs.execute();
            
            // account registered!
            return true;
        } catch (SQLException e) {
            throw new WebDBBrokerException(e);
        }
    }    
    
    public int verify(String username, String password) throws WebDBBrokerException {
        try (CallableStatement cs = conn.prepareCall("{? = call validateUser(?, ?)}")) {
            cs.setString(2, username);
            cs.setString(3, password);
            cs.registerOutParameter(1, Types.INTEGER);
            
            cs.execute();            
            int id = cs.getInt(1);            
            cs.close();            
            return id;            
        } catch (SQLException e) {
            throw new WebDBBrokerException(e);
        }
    }
    
    /*
    public int findName(String username) throws WebDBBrokerException {
        try (CallableStatement cs = conn.prepareCall("{? = call findAccount(?)}")) {
            cs.setString(2, username);
            
            cs.execute();
            
            return cs.getInt(1);
        } catch (SQLException e) {
            throw new WebDBBrokerException(e);
        }
    }
    
    public Boolean checkName(String username) throws WebDBBrokerException {
        try (CallableStatement cs = conn.prepareCall("{? = call checkAccountName(?)}")) {
            cs.setString(2, username);
            
            cs.execute();
            
            return cs.getBoolean(1);
        } catch (SQLException e) {
            throw new WebDBBrokerException(e);
        }
    }
    */
}
