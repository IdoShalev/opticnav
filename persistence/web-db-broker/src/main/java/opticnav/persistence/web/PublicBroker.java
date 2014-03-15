package opticnav.persistence.web;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

public class PublicBroker {
    private java.sql.Connection conn;
    
    public PublicBroker(java.sql.Connection conn) {
        this.conn = conn;
    }

    public boolean registerAccount(String username, String password) throws PublicBrokerException {
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
                conn.commit();
                cs.close();
                
                return flag;
            } catch (SQLException e) {
                throw new PublicBrokerException(e);
            }
        }        
    }    
    
    public int verify(String username, String password) throws PublicBrokerException {
        if(!checkUsername(username) || !checkPassword(password)) {
            return 0;
        }
        else {
            try (CallableStatement cs = conn.prepareCall("{? = call validateUser(?, ?)}")) {
                cs.setString(2, username);
                cs.setString(3, password);
                cs.registerOutParameter(1, Types.INTEGER);
                
                cs.execute();
                int id = cs.getInt(1);
                return id;            
            } catch (SQLException e) {
                throw new PublicBrokerException(e);
            }
        }
    }

    public int findName(String username) throws PublicBrokerException {
        try (CallableStatement cs = conn.prepareCall("{? = call findAccount(?)}")) {            
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(2, username);
            
            cs.execute();
            int id = cs.getInt(1);
            
            return id;
        } catch (SQLException e) {
            throw new PublicBrokerException(e);
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
