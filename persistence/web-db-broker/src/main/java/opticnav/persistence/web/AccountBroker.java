package opticnav.persistence.web;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import javax.sql.DataSource;

public class AccountBroker implements AutoCloseable{
    private java.sql.Connection conn;
    private int accountID;
    
    public AccountBroker(DataSource dataSource, int accountID)
            throws SQLException {
        this.conn = DBUtil.getConnectionFromDataSource(dataSource);
        this.accountID = accountID;
    }
    
    @Override
    public void close() throws AccountBrokerException {
        try {
            this.conn.close();
        } catch (SQLException e) {
            throw new AccountBrokerException(e);
        }
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
            conn.commit();
            
            return id;
        } catch (SQLException e) {
            throw new AccountBrokerException(e);
        }
    }
    
    public void modifyMap(int id, Map map) throws AccountBrokerException {
        try (CallableStatement cs = conn.prepareCall("{call deleteMarker(?)}")) {
            cs.setInt(1, id);
            cs.execute();
        } catch (SQLException e) {
            throw new AccountBrokerException(e);
        }
        try (CallableStatement cs = conn.prepareCall("{call deleteAnchor(?)}")) {
            cs.setInt(1, id);
            cs.execute();
        } catch (SQLException e) {
            throw new AccountBrokerException(e);
        }
        try (CallableStatement cs = conn.prepareCall("{? = call createMarker(?, ?, ?, ?, ?)}")) {
            List<Marker> ml = map.getMarkers();
            for (int i = 0; i < ml.size(); i++) {
                cs.registerOutParameter(1, Types.INTEGER);
                cs.setString(2, ml.get(i).getName());
                cs.setInt(3, id);
                cs.setInt(4, 0);
                cs.setInt(5, ml.get(i).getLat());
                cs.setInt(6, ml.get(i).getLng());

                cs.execute();
            }
        } catch (SQLException e) {
            throw new AccountBrokerException(e);
        }
        try (CallableStatement cs = conn.prepareCall("{call createAnchor(?, ?, ?, ?, ?)}")) {
            List<Anchor> al = map.getAnchors();
            for (int i = 0; i < al.size(); i++) {
                cs.setInt(1, id);
                cs.setInt(2, al.get(i).getLocalX());
                cs.setInt(3, al.get(i).getLocalY());
                cs.setInt(4, al.get(i).getLat());
                cs.setInt(5, al.get(i).getLng());

                cs.execute();
            }
        } catch (SQLException e) {
            throw new AccountBrokerException(e);
        }
        try {
            conn.commit();
        } catch (SQLException e) {
            throw new AccountBrokerException(e);
        }
    }
    
    public void deleteMap(int id) throws AccountBrokerException {
        try(CallableStatement cs = conn.prepareCall("{call deleteMap(?)}")){
            cs.setInt(1, id);
            cs.execute();
            conn.commit();
        } catch (SQLException e) {
            throw new AccountBrokerException(e);
        }
            
    }

    public Map getMap(int id) throws AccountBrokerException {
        Map map = new Map();
        
        try(CallableStatement cs = conn.prepareCall("{call getAllMarkers(?)}")){
            cs.setInt(1, id);
            
            ResultSet rs = cs.executeQuery();
            @SuppressWarnings("unused")
            Marker marker = null;
            while(rs.next()){
                map.addMarker(marker = new Marker(rs.getInt(5), rs.getInt(4), rs.getString(1), rs.getInt(3)));
            }
            rs.close();
        } catch (SQLException e) {
            throw new AccountBrokerException(e);
        }
        try(CallableStatement cs = conn.prepareCall("{call getAllAnchorss(?)}")){
            cs.setInt(1, id);
            
            ResultSet rs = cs.executeQuery();
            @SuppressWarnings("unused")
            Anchor anchor = null;
            while(rs.next()){
                map.addAnchor(anchor = new Anchor(rs.getInt(5), rs.getInt(4), rs.getInt(2), rs.getInt(3)));;
            }
            rs.close();
        } catch (SQLException e) {
            throw new AccountBrokerException(e);
        }
        
        return map;
    }
    
    public int getMapResource(int id) throws AccountBrokerException {
        try(CallableStatement cs = conn.prepareCall("{? = call getMapResource(?)}")){
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setInt(2, id);
            
            cs.execute();
            id = cs.getInt(1);
            return id;
        } catch (SQLException e) {
            throw new AccountBrokerException(e);
        }
    }
}
