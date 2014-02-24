package opticnav.persistence.web;

import java.sql.CallableStatement;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;

public class WebDBBroker {
    private Connection conn;

    public WebDBBroker(Connection conn) {
        this.conn = conn;
    }
    
    public boolean registerAccount(String username, String password) throws WebDBBrokerException {
        try (CallableStatement cs = conn.prepareCall("call whatever")) {
            cs.executeUpdate();
            
            // account registered!
            return true;
        } catch (SQLException e) {
            throw new WebDBBrokerException(e);
        }
    }
    
    public boolean verify(String username, String password) throws WebDBBrokerException {
        return false;
    }
}
