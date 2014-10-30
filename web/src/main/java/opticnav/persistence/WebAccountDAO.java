package opticnav.persistence;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import opticnav.persistence.exceptions.WebAccountDAOException;
import opticnav.persistence.exceptions.WebAccountPublicDAOException;
import opticnav.persistence.map.Anchor;
import opticnav.persistence.map.GetMap;
import opticnav.persistence.map.MapsListEntry;
import opticnav.persistence.map.Marker;
import opticnav.persistence.map.ModifyMap;

/**
 * Handles all requests to the database that are bound to an account
 * such as map management and ARD binding
 * 
 * @author Danny Spencer, Kay Bernhardt
 */
public class WebAccountDAO implements AutoCloseable {
    private java.sql.Connection conn;
    private int accountID;

    /**
     * Constructor
     * sets the account ID that identifies the current user
     * and establishes a connection to the database
     * 
     * @param dataSource The parameters that are needed to connect to the database
     * @param accountID The ID that identifies the current user
     * @throws SQLException Thrown if there was a problem with the sql connection
     * @throws WebAccountDAOException Thrown if there was a problem with connecting to or validating the DAO
     */
    public WebAccountDAO(DataSource dataSource, int accountID)
            throws SQLException, WebAccountDAOException {
        this.conn = DBUtil.getConnectionFromDataSource(dataSource);
        this.accountID = accountID;
        validateUserID();
    }
    
    /**
     * Checks if the user for this broker exists
     * 
     * @throws WebAccountDAOException Thrown if there was a problem with connecting to or validating the DAO
     */
    private void validateUserID() throws WebAccountDAOException {
        try (CallableStatement cs = conn.prepareCall("{? = call validateUserID(?)}")) {
            cs.registerOutParameter(1, Types.BOOLEAN);
            cs.setInt(2, this.accountID);
            cs.execute();
            if(!cs.getBoolean(1))
            {
                throw new WebAccountDAOException("User id does not exist");
            }
        }catch (SQLException e) {
            throw new WebAccountDAOException(e);
        }
    }
    
    @Override
    public void close() throws WebAccountDAOException {
        try {
            this.conn.close();
        } catch (SQLException e) {
            throw new WebAccountDAOException(e);
        }
    }

    /**
     * Get the ID of the currently registered device
     * 
     * @return The ID number identifying the device, or 0 if there is no currently registered device
     * @throws WebAccountDAOException Thrown if there was a problem with connecting to or validating the DAO
     */
    public int getARD() throws WebAccountDAOException {
        try (CallableStatement cs = conn.prepareCall("{? = call getARD(?)}")) {
            cs.setInt(2, accountID);
            cs.registerOutParameter(1, Types.INTEGER);

            cs.execute();
            int ard_id = cs.getInt(1);
            return ard_id;
        } catch (SQLException e) {
            throw new WebAccountDAOException(e);
        }
    }

    /**
     * Does the account have a currently registered device?
     * 
     * @return True if account has a currently registered device, false if not
     * @throws WebAccountDAOException Thrown if there was a problem with connecting to or validating the DAO
     */
    public boolean hasARD() throws WebAccountDAOException {
        return getARD() != 0;
    }

    /**
     * Assign a device to the account.
     * 
     * @param ardID The ID number identifying the device
     * @throws WebAccountDAOException Thrown if there was a problem with connecting to or validating the DAO
     */
    public void setARD(int ardID) throws WebAccountDAOException {
        try (CallableStatement cs = conn.prepareCall("{call setARD(?, ?)}")) {
            cs.setInt(1, this.accountID);
            cs.setInt(2, ardID);

            cs.execute();
            conn.commit();
        } catch (SQLException e) {
            throw new WebAccountDAOException(e);
        }
    }

    /**
     * Remove the currently registered device (ARD) from the account.
     * Note that this will not unregister the device from ardd, but only unbinds it from the web account.
     * 
     * @throws WebAccountDAOException Thrown if there was a problem with connecting to or validating the DAO
     */
    public boolean removeARD() throws WebAccountDAOException {
        try (CallableStatement cs = conn.prepareCall("{? = call deleteARD(?)}")) {
            cs.registerOutParameter(1, Types.BOOLEAN);
            cs.setInt(2, accountID);

            cs.execute();
            if (!cs.getBoolean(1)){
                return false;
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            throw new WebAccountDAOException(e);
        }
    }

    /**
     * Create a new map, initialized with a name and an image resource. No markers or anchors are added here.
     * 
     * @param name The map name
     * @param imageResource The ID number identifying the image resource
     * @return The ID number identifying the newly created map
     * @throws WebAccountDAOException Thrown if there was a problem with connecting to or validating the DAO
     */
    public int createMap(String name, int imageResource)
            throws WebAccountDAOException {
        try (CallableStatement cs = conn
                .prepareCall("{? = call createMap(?, ?, ?)}")) {
            cs.registerOutParameter(1, Types.INTEGER);
            cs.setString(2, name);
            cs.setInt(3, imageResource);
            cs.setInt(4, this.accountID);

            cs.execute();
            conn.commit();
            int id = cs.getInt(1);
            

            return id;
        } catch (SQLException e) {
            throw new WebAccountDAOException(e);
        }
    }

    /**
     * Modifies (replaces) the entirety of a specific map, excluding the map name and image.
     * The map must belong to the account.
     * Note that old marker and anchor data not specified in the ModifyMap object will become lost!
     * 
     * @param id The ID number identifying the map
     * @param map The payload used to replace the map
     * @throws WebAccountDAOException Thrown if there was a problem with connecting to or validating the DAO
     */
    public boolean modifyMap(int id, ModifyMap map) throws WebAccountDAOException {
        try (CallableStatement cs = conn.prepareCall("{? = call deleteMarker(?, ?)}")) {
            cs.registerOutParameter(1, Types.BOOLEAN);
            cs.setInt(2, id);
            cs.setInt(3, this.accountID);
            cs.execute();
            if (!cs.getBoolean(1)){
                return false;
            }
        } catch (SQLException e) {
            throw new WebAccountDAOException(e);
        }
        try (CallableStatement cs = conn.prepareCall("{? = call deleteAnchor(?, ?)}")) {
            cs.registerOutParameter(1, Types.BOOLEAN);
            cs.setInt(2, id);
            cs.setInt(3, this.accountID);
            cs.execute();
            if (!cs.getBoolean(1)){
                return false;
            }
        } catch (SQLException e) {
            throw new WebAccountDAOException(e);
        }
        try (CallableStatement cs = conn
                .prepareCall("{call createMarker(?, ?, ?, ?, ?)}")) {
            List<Marker> ml = map.getMarkers();
            for (int i = 0; i < ml.size(); i++) {
                cs.setString(1, ml.get(i).getName());
                cs.setInt(2, id);
                cs.setInt(3, 0);    //no resource
                cs.setInt(4, ml.get(i).getLat());
                cs.setInt(5, ml.get(i).getLng());

                cs.execute();
            }
        } catch (SQLException e) {
            throw new WebAccountDAOException(e);
        }
        try (CallableStatement cs = conn
                .prepareCall("{call createAnchor(?, ?, ?, ?, ?)}")) {
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
            throw new WebAccountDAOException(e);
        }
        try {
            conn.commit();
        } catch (SQLException e) {
            throw new WebAccountDAOException(e);
        }
        return true;
    }

    /**
     * Delete a specific map.
     * The map must belong to the account.
     * Note that the image resource associated with the map does NOT also get deleted!
     * 
     * @param id The ID number identifying the map
     * @throws WebAccountDAOException Thrown if there was a problem with connecting to or validating the DAO
     */
    public boolean deleteMap(int id) throws WebAccountDAOException {
        try (CallableStatement cs = conn.prepareCall("{? = call deleteMap(?, ?)}")) {
            cs.registerOutParameter(1, Types.BOOLEAN);
            cs.setInt(2, id);
            cs.setInt(3, this.accountID);
            cs.execute();
            if (!cs.getBoolean(1)){
                return false;
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            throw new WebAccountDAOException(e);
        }
    }

    /**
     * Get all information about a specific map (Name, markers, anchors, image).
     * The map must belong to the account, or else null will be returned.
     * 
     * @param id The ID number identifying the map
     * @return A GetMap object, or null if not found
     * @throws WebAccountDAOException Thrown if there was a problem with connecting to or validating the DAO
     */
    public GetMap getMap(int id) throws WebAccountDAOException {
        final String name;
        final int imageResource;
        try {
            try (CallableStatement cs = conn
                    .prepareCall("{? = call getMapName(?, ?)}")) {
                cs.registerOutParameter(1, Types.VARCHAR);
                cs.setInt(2, id);
                cs.setInt(3, this.accountID);

                cs.execute();
                name = cs.getString(1);
            }
            try (CallableStatement cs = conn
                    .prepareCall("{? = call getMapResource(?, ?)}")) {
                cs.registerOutParameter(1, Types.INTEGER);
                cs.setInt(2, id);
                cs.setInt(3, this.accountID);

                cs.execute();
                imageResource = cs.getInt(1);
            }
            
            if (name == null || imageResource == 0) {
                return null;
            }
            
            final GetMap map = new GetMap(name, imageResource);
            try (CallableStatement cs = conn
                    .prepareCall("{call getMapMarkers(?)}")) {
                cs.setInt(1, id);

                try (ResultSet rs = cs.executeQuery()) {
                    while (rs.next()) {
                        Marker marker;
                        marker = new Marker(rs.getInt(5), rs.getInt(4),
                                rs.getString(1), rs.getInt(3));
                        map.addMarker(marker);
                    }
                }
            }
            try (CallableStatement cs = conn
                    .prepareCall("{call getMapAnchors(?)}")) {
                cs.setInt(1, id);

                try (ResultSet rs = cs.executeQuery()) {
                    while (rs.next()) {
                        Anchor anchor;
                        anchor = new Anchor(rs.getInt(5), rs.getInt(4),
                                rs.getInt(2), rs.getInt(3));
                        map.addAnchor(anchor);
                    }
                }
            }
            return map;
        } catch (SQLException e) {
            throw new WebAccountDAOException(e);
        }
    }

    /**
     * Get a list of all maps---by name and map ID---belonging to the account.
     * 
     * @return A list of all maps belonging to the account
     * @throws WebAccountDAOException Thrown if there was a problem with connecting to or validating the DAO
     */
    public List<MapsListEntry> getMapsList() throws WebAccountDAOException {
        List<MapsListEntry> list = new LinkedList<>();
        try {
            try (CallableStatement cs = conn
                    .prepareCall("{call getMapsList(?)}")) {
                cs.setInt(1, accountID);

                try (ResultSet rs = cs.executeQuery()) {
                    while (rs.next()) {
                        MapsListEntry mapE;
                        mapE = new MapsListEntry(rs.getString(3), rs.getInt(1));
                        list.add(mapE);
                    }
                }
            }
        } catch (SQLException e) {
            throw new WebAccountDAOException(e);
        }
        return list;
    }
    
    /**
     * Get the username of the account
     * 
     * @return The username of the account
     * @throws WebAccountDAOException Thrown if there was a problem with connecting to or validating the DAO
     */
    public String getUsername() throws WebAccountPublicDAOException {
        try (CallableStatement cs = conn.prepareCall("{? = call getUsernameByID(?)}")) {
            cs.registerOutParameter(1, Types.VARCHAR);
            cs.setInt(2, this.accountID);
            cs.execute();
            return cs.getString(1);
        } catch (SQLException e) {
            throw new WebAccountPublicDAOException(e);
        }
    }
}
