package opticnav.persistence.web;

import java.sql.CallableStatement;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;

public class WebDBBroker {
    private java.sql.Connection conn;

    public WebDBBroker(java.sql.Connection conn) {
        this.conn = conn;
    }
    
    public boolean registerAccount(String username, String password) throws WebDBBrokerException {
        try (CallableStatement cs = conn.prepareCall("{call registerAccount(?, ?}")) {
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
        try (CallableStatement cs = conn.prepareCall("{? = call validateUser(?, ?}")) {
            cs.setString(2, username);
            cs.setString(3, password);
            
            cs.execute();
            
            return cs.getInt(1);
        } catch (SQLException e) {
            throw new WebDBBrokerException(e);
        }
    }
    
    public int findName(String username) throws WebDBBrokerException {
        try (CallableStatement cs = conn.prepareCall("{? = call findAccount(?}")) {
            cs.setString(2, username);
            
            cs.execute();
            
            return cs.getInt(1);
        } catch (SQLException e) {
            throw new WebDBBrokerException(e);
        }
    }
    
    public Boolean checkName(String username) throws WebDBBrokerException {
        try (CallableStatement cs = conn.prepareCall("{? = call checkAccountName(?}")) {
            cs.setString(2, username);
            
            cs.execute();
            
            return cs.getBoolean(1);
        } catch (SQLException e) {
            throw new WebDBBrokerException(e);
        }
    }
}
