package opticnav.persistence.web;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedList;

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
    
    //TODO please check=========================================================
    
    public int createMap(String name, int imageResource) throws AccountBrokerException{
        try (CallableStatement cs = conn.prepareCall("{? = call createMap(?, ?, ?)}")){
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(2, name);
            cs.setInt(3, imageResource);
            cs.setInt(4, this.accountID);
            
            cs.execute();
            int id = cs.getInt(1);
            cs.close();
            conn.commit();
            
            return id;
        } catch (SQLException e) {
            throw new AccountBrokerException(e);
        }
    }
    
    public void modifyMap(int id, Map map) throws AccountBrokerException {
        try(CallableStatement cs = conn.prepareCall("{call deleteMarker(?)}")){
            cs.setInt(1, id);
            cs.execute();
            
            //cs = conn.prepareCall("{call deleteAnchor(?)}");
            cs.setInt(1, id);
            cs.execute();
            
            /*
            LinkedList<Marker> ml = map.getMarkerList();            
            for (int i = 0; i < ml.length(); i++){
                cs = conn.prepareCall("{? = call createMarker(?, ?, ?, ?, ?)}");
                cs.registerOutParameter(1, Types.INTEGER);
                cs.setString(2, ml.get(i).getName());
                cs.setInt(3, id);
                cs.setInt(4, 0);
                cs.setInt(5, ml.get(i).getLat);
                cs.setInt(6, ml.get(i).getLong);
                
                cs.execute();
            }
            */
            /*
            LinkedList<Anchor> al = map.getAnchorList();
            for (int i = 0; i < al.length(); i++){
                cs = conn.prepareCall("{call createAnchor(?, ?, ?, ?, ?)}");
                cs.setInt(1, id);
                cs.setInt(2, al.get(i).getLocalX);
                cs.setInt(3, al.get(i).getLocalY);
                cs.setInt(4, al.get(i).getLat);
                cs.setInt(5, al.get(i).getLong);
                
                cs.execute();
            }
            */
            cs.close();
            conn.commit();
            
        } catch (SQLException e) {
            throw new AccountBrokerException(e);
        }
    }
    
    public void deleteMap(int id) throws AccountBrokerException {
        try(CallableStatement cs = conn.prepareCall("{call deleteMap(?)}")){
            cs.setInt(1, id);
            cs.execute();
            cs.close();
            conn.commit();
        } catch (SQLException e) {
            throw new AccountBrokerException(e);
        }
            
    }
    
    //TODO change procedures
    /*
    public Map getMap(int id) throws AccountBrokerException {
        
    }
    */
    
    public int getMapResource(int id) throws AccountBrokerException {
        try(CallableStatement cs = conn.prepareCall("{? = call getMapResource(?)}")){
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(2, id);
            
            cs.execute();
            id = cs.getInt(1);
            cs.close();
            return id;
        } catch (SQLException e) {
            throw new AccountBrokerException(e);
        }
    }
}
