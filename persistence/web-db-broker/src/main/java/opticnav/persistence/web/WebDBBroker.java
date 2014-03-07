package opticnav.persistence.web;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

import javax.sql.DataSource;

public class WebDBBroker implements AutoCloseable {
    
    private java.sql.Connection conn;

    public WebDBBroker(DataSource ds) throws WebDBBrokerException {
        try {
            this.conn = ds.getConnection();
        } catch (SQLException e) {
            throw new WebDBBrokerException(e);
        }
    }
    
    public WebDBBroker(java.sql.Connection conn) {
        this.conn = conn;
    }
    
    public boolean registerAccount(String username, String password) throws WebDBBrokerException {
            if(!checkUsername(username) || !checkPassword(password)){
                return false;
            }
            else {
                try (CallableStatement cs = conn.prepareCall("{? = call registerAccount(?, ?)}")) {
                cs.setString(2, username);
                cs.setString(3, password);
                cs.registerOutParameter(1, Types.BOOLEAN);
                
                cs.execute();
                boolean flag = cs.getBoolean(1);
                cs.close();
                
                return flag;
                } catch (SQLException e) {
                    throw new WebDBBrokerException(e);
                }
            }        
    }    
    
    public int verify(String username, String password) throws WebDBBrokerException {
        if(!checkUsername(username) || !checkPassword(password)){
            return 0;
        }
        else{
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
    }

    @Override
    public void close() throws WebDBBrokerException {
        try {
            this.conn.close();
        } catch (SQLException e) {
            throw new WebDBBrokerException(e);
        }
    }
    
    public int findName(String username) throws WebDBBrokerException {
        try (CallableStatement cs = conn.prepareCall("{? = call findAccount(?)}")) {            
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(2, username);
            
            cs.execute();
            int id = cs.getInt(1);
            cs.close();
            
            return id;
        } catch (SQLException e) {
            throw new WebDBBrokerException(e);
        }
    }
    
    public int getARD(int accID) throws WebDBBrokerException {
        
        try (CallableStatement cs = conn.prepareCall("{? = call getARD(?)}")) {
            cs.setInt(2, accID);
            cs.registerOutParameter(1, Types.INTEGER);

            cs.execute();
            int ard_id = cs.getInt(1);
            cs.close();
            return ard_id;
        } catch (SQLException e) {
            throw new WebDBBrokerException(e);
        }
        
    }
    
    public void setARD(int accID, int ardID) throws WebDBBrokerException {
        try (CallableStatement cs = conn.prepareCall("{call setARD(?, ?)}")) {
            cs.setInt(1, accID);
            cs.setInt(2, ardID);
            
            cs.execute();
            cs.close();
        } catch (SQLException e) {
            throw new WebDBBrokerException(e);
        }
    }
    
    public void removeARD(int accID) throws WebDBBrokerException {
        try (CallableStatement cs = conn.prepareCall("{call removeARD(?)}")) {
            cs.setInt(1, accID);
            
            cs.execute();
            cs.close();
        } catch (SQLException e) {
            throw new WebDBBrokerException(e);
        }
    }
    
    private boolean checkUsername(String username){
        boolean flag = true;
        if (username == null || username.equals("")){
            flag = false;
        }
        return flag;
    }
    
    private boolean checkPassword(String password){
        boolean flag = true;
        if (password == null || password.equals("")){
            flag = false;
        }
        return flag;
    }
}
